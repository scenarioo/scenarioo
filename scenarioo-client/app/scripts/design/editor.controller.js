/*scenarioo-client
 Copyright (C) 2015, scenarioo.org Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/* global SVG:false */


angular.module('scenarioo.controllers').controller('EditorCtrl', function ($rootScope, $scope, $location, $filter, $interval, $routeParams, $route,
                                                                           GlobalHotkeysService, SelectedBranchAndBuild, ToolBox, DrawShapeService,
                                                                           DrawingPadService, SketchStep, SketchStepResource, IssueResource, Issue,
                                                                           ScenarioSketchResource, ScenarioSketch, ContextService, $log, $window, localStorageService) {

    var AUTHOR_LOCAL_STORAGE_KEY = 'issue_author',
        MODE_CREATE = 'create',
        MODE_EDIT = 'edit';

    // Controller initialisation method, according to John Papa style guide
    function activate() {
        var drawingPad = SVG('drawingPad').spof();
        DrawingPadService.setDrawingPad(drawingPad);

        $scope.mode = $location.search().mode;
        $scope.currentTool = null;
        $scope.tools = ToolBox;
        $scope.activateTool($scope.tools[0]);

        if($scope.mode === MODE_EDIT) {
            initEditMode();
        }

        $('body').addClass('sc-sketcher-bg-color-light');

        setAuthorFromLocalStorageIfAvailable();
    }

    function initEditMode() {
        $scope.issueId = ContextService.issueId;
        $scope.scenarioSketchId = ContextService.scenarioSketchId;
        $scope.sketchStepName = parseInt(ContextService.sketchStepName);

        if(ContextService.issueId) {
            Issue.load(ContextService.issueId);
        }
    }

    function setAuthorFromLocalStorageIfAvailable() {
        if($scope.mode !== MODE_CREATE) {
            return;
        }

        var author = localStorageService.get(AUTHOR_LOCAL_STORAGE_KEY);

        if(!angular.isString(author) || author.length === 0) {
            return;
        }

        $scope.issueAuthor = author;
    }

    function storeAuthorInLocalStorage() {
        localStorageService.set(AUTHOR_LOCAL_STORAGE_KEY, $scope.issueAuthor);
    }

    $rootScope.$on(Issue.ISSUE_LOADED_EVENT, function (event, result) {
        $scope.currentIssue = result.issue;
        $scope.issueName = $scope.currentIssue.name;
        $scope.issueDescription = $scope.currentIssue.description;
        $scope.issueAuthor = $scope.currentIssue.author;
    });


    $scope.activateTool = function (tool) {
        if ($scope.currentTool) {
            $scope.currentTool.deactivate();
        }
        $scope.currentTool = tool;
        tool.activate();

        DrawingPadService.unSelectAllShapes();

        $('.tooltip').hide().delay( 100 );
    };

    $scope.exitSketcher = function() {
        $window.history.back();
    };

    $scope.$on('$locationChangeStart', function(event) {
        if ( $route.current.originalPath === '/editor'){
            if (!confirm('Unsaved data will be lost!')) {
                event.preventDefault();
            }
        }
    });

    angular.element($window).on('beforeunload', function(event){
        if( $route.current.originalPath === '/editor'){
            return 'Unsaved data will be lost!';
        }
    });

    // TODO extract all saving related methods into a service
    $scope.saveSketcherData = function () {

        DrawingPadService.unSelectAllShapes();

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;

        var issue = new IssueResource({
            branchName: $routeParams.branch,
            name: $scope.issueName,
            description: $scope.issueDescription,
            author: $scope.issueAuthor
        });

        if ($scope.mode === MODE_CREATE) {
            issue.usecaseContextName = ContextService.usecaseName;
            issue.usecaseContextLink = ContextService.usecaseLink;
            issue.scenarioContextName = ContextService.scenarioName;
            issue.scenarioContextLink = ContextService.scenarioLink;
            issue.stepContextLink = ContextService.stepLink;
        }

        // TODO why this check? looks like this is always 0
        if ($scope.issueSaved === 0) {
            ++$scope.issueSaved;

            if ($scope.issueId && $scope.issueId !== undefined) {
                issue.issueId = $scope.issueId;

                Issue.saveIssue(issue, function (updatedIssue) {
                    $log.log('UPDATE Issue', updatedIssue.issueId);
                    $scope.issueId = updatedIssue.issueId;
                    $rootScope.$broadcast('IssueSaved', {issueId: updatedIssue.issueId});
                }, function (error) {
                    sketchSavedWithError(error);
                });
            } else {
                Issue.saveIssue(issue, function (savedIssue) {
                    $log.log('SAVE Issue', savedIssue.issueId);
                    $scope.issueId = savedIssue.issueId;
                    $rootScope.$broadcast('IssueSaved', {issueId: savedIssue.issueId});
                }, function (error) {
                    sketchSavedWithError(error);
                });
            }
        }
    };

    $rootScope.$on('IssueSaved', function (event, args) {

        var scenarioSketchName = 'undefined';

        var scenarioSketch = new ScenarioSketchResource({
            branchName: $routeParams.branch,
            scenarioSketchName: scenarioSketchName,
            author: $scope.issueAuthor,
            scenarioSketchStatus: 'Draft',
            issueId: args.issueId
        }, {});


        if ($scope.issueSaved === 1) {
            ++$scope.scenarioSketchSaved;

            if ($scope.scenarioSketchId && $scope.scenarioSketchId !== undefined) {
                scenarioSketch.scenarioSketchId = $scope.scenarioSketchId;

                ScenarioSketch.saveScenarioSketch(scenarioSketch, function (updatedScenarioSketch) {
                    $log.log('UPDATE ScenarioSketch', updatedScenarioSketch.scenarioSketchId);
                    $rootScope.$broadcast('ScenarioSketchSaved', {
                        issueId: updatedScenarioSketch.issueId,
                        scenarioSketchId: updatedScenarioSketch.scenarioSketchId
                    });
                }, function (error) {
                    sketchSavedWithError(error);
                });
            } else {
                ScenarioSketch.saveScenarioSketch(scenarioSketch, function (savedScenarioSketch) {
                    $log.log('SAVE ScenarioSketch', savedScenarioSketch.scenarioSketchId);
                    $rootScope.$broadcast('ScenarioSketchSaved', {
                        issueId: savedScenarioSketch.issueId,
                        scenarioSketchId: savedScenarioSketch.scenarioSketchId
                    });
                }, function (error) {
                    sketchSavedWithError(error);
                });
            }
        }
    });

    $rootScope.$on('ScenarioSketchSaved', function (event, args) {

        var exportedSVG = DrawingPadService.exportDrawing();

        var sketchStep = new SketchStepResource({
            branchName: $routeParams.branch,
            sketch: exportedSVG,
            issueId: args.issueId,
            scenarioSketchId: args.scenarioSketchId
        }, {});

        if ($scope.mode === MODE_CREATE) {
            sketchStep.usecaseContextName = ContextService.usecaseName;
            sketchStep.usecaseContextLink = ContextService.usecaseLink;
            sketchStep.scenarioContextName = ContextService.scenarioName;
            sketchStep.scenarioContextLink = ContextService.scenarioLink;
            sketchStep.stepContextLink = ContextService.stepLink;
            sketchStep.contextInDocu = ContextService.screenshotURL;
        }


        if ($scope.scenarioSketchSaved === 1) {

            if ($scope.sketchStepName && $scope.sketchStepName !== undefined) {
                sketchStep.sketchStepName = $scope.sketchStepName;

                SketchStep.saveSketchStep(sketchStep, function (updatedSketchStep) {
                    $log.log('UPDATE SketchStep', updatedSketchStep.sketchStepName);
                    sketchSuccessfullySaved();
                }, function (error) {
                    sketchSavedWithError(error);
                });
            } else {
                SketchStep.saveSketchStep(sketchStep, function (savedSketchStep) {
                    $log.log('SAVE SketchStep', savedSketchStep.sketchStepName);

                    $scope.scenarioSketchId = args.scenarioSketchId;
                    $scope.sketchStepName = savedSketchStep.sketchStepName;
                    $scope.mode = MODE_EDIT;

                    sketchSuccessfullySaved();
                }, function (error) {
                    sketchSavedWithError(error);
                });
            }
        }
    });

    function sketchSuccessfullySaved() {
        $scope.addAlert('success', 'saveSketchSuccessfulMessage', 'Sketch successfully saved');

        storeAuthorInLocalStorage();
        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;
    }

    function sketchSavedWithError(error) {
        $scope.addAlert('danger', 'saveSketchFailedMessage', 'The sketch has not been saved. Reason: ' + error);

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;

        if ($scope.mode === MODE_CREATE) {
            if($scope.issueId) {
                Issue.deleteSketcherData($scope.issueId);
            }

            $scope.issueId = null;
            $scope.scenarioSketchId = null;
            $scope.sketchStepName = null;
        }
    }

    $rootScope.$on('drawingEnded', function (scope, shape) {
        // $scope.$apply is used to make sure that the button disabled directive updates in the view
        $scope.$apply($scope.activateTool($scope.tools[0]));

        shape.selectToggle();
        DrawingPadService.setSelectedShape(shape);
    });

    $rootScope.$on(DrawShapeService.SHAPE_SELECTED_EVENT, function (scope, shape) {
        DrawingPadService.unSelectAllShapes();
        shape.selectToggle();
        DrawingPadService.setSelectedShape(shape);
    });

    $rootScope.$on(DrawingPadService.DRAWINGPAD_CLICKED_EVENT, function () {
        $log.log(DrawingPadService.DRAWINGPAD_CLICKED_EVENT);
        DrawingPadService.unSelectAllShapes();
    });

    $scope.sendSelectedShapeToBack = function () {
        DrawingPadService.sendSelectedShapeToBack();
    };

    $scope.sendSelectedShapeToFront = function () {
        DrawingPadService.sendSelectedShapeToFront();
    };

    $scope.sendSelectedShapeBackward = function () {
        DrawingPadService.sendSelectedShapeBackward();
    };

    $scope.sendSelectedShapeForward = function () {
        DrawingPadService.sendSelectedShapeForward();
    };

    $scope.contextBreadcrumbs = function () {
        var uc, sc, stepName;

        if(ContextService && ContextService.usecaseName) {
            uc = ContextService.usecaseName;
            sc = ContextService.scenarioName;
        } else if($scope.currentIssue && $scope.currentIssue.usecaseContextName) {
            uc = $scope.currentIssue.usecaseContextName;
            sc = $scope.currentIssue.scenarioContextName;
        }
        if (ContextService && ContextService.stepName){
            stepName = ContextService.stepName;
        } else if (ContextService && ContextService.sketchStepName){
            stepName = ContextService.sketchStepName;
        }

        if(uc) {
            return 'Use Case: ' + uc +
                ' > Scenario: ' + sc +
                ' > Step: ' + stepName;
        } else {
            return '';
        }
    };

    $scope.$on('$destroy', function () {
        DrawingPadService.destroy();
        $scope.issueId = null;
        $scope.scenarioSketchId = null;
        $scope.sketchStepName = null;
        $('body').removeClass('sc-sketcher-bg-color-light');
    });

    // Alerts to give feedback whether saving the sketch was successful or not

    $scope.alerts = [];

    $scope.addAlert = function(type, id, message) {
        var alertEntry = {type: type, id: id, message: message};
        $scope.alerts.push(alertEntry);
        // We have to set our own timeout using the $interval service because using $timeout provokes issues
        // with protractor. See:
        // - https://github.com/angular-ui/bootstrap/pull/3982
        // - https://github.com/angular/protractor/issues/169
        $interval(function() {
            $scope.closeAlert(alertEntry);
        }, 5000, 1);
    };

    $scope.closeAlert = function(alertEntry) {
        var index = $scope.alerts.indexOf(alertEntry);
        $scope.alerts.splice(index, 1);
    };

    activate();

});
