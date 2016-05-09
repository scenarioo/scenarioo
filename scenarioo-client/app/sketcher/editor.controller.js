/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* global SVG:false */

angular
    .module('scenarioo.controllers')
    .controller('EditorCtrl', function ($rootScope, $scope, $location, $filter, $interval, $routeParams, $route,
        GlobalHotkeysService, SelectedBranchAndBuild, ToolBox, DrawShapeService, DrawingPadService, StepSketch,
        StepSketchResource, IssueResource, Issue, ScenarioSketchResource, ContextService, $log, $window,
        scLocalStorage, ZoomPanService, $timeout, HostnameAndPort) {

    var AUTHOR_LOCAL_STORAGE_KEY = 'issue_author',
        MODE_CREATE = 'create',
        MODE_EDIT = 'edit';

    $scope.savingSketch = false;

    function activate() {
        $scope.mode = $location.search().mode;
        if ($scope.mode === MODE_EDIT) {
            initEditMode();
        } else {
            setAuthorFromLocalStorageIfAvailable();
            initializeDrawingPad();
        }
    }

    function initEditMode() {
        $scope.issueId = $routeParams.issueId;
        $scope.scenarioSketchId = $routeParams.scenarioSketchId;
        $scope.stepSketchId = $routeParams.stepSketchId;

        IssueResource.get(
            {
                'branchName': $routeParams.branch,
                'issueId': $scope.issueId
            },
            function onSuccess(result) {
                $scope.currentIssue = result.issue;
                $scope.issueName = $scope.currentIssue.name;
                $scope.issueDescription = $scope.currentIssue.description;
                $scope.issueAuthor = $scope.currentIssue.author;
                DrawingPadService.setSvgUrl(getSVGUrl());
                initializeDrawingPad();
            });
    }

    function initializeDrawingPad() {
        // The drawingPad is initialized here because we had issues
        // when initializing it in DrawingPadService.
        var drawingPad = SVG('drawingPad').spof();
        DrawingPadService.setDrawingPad(drawingPad);

        $scope.currentTool = null;
        $scope.toolBox = ToolBox;
        $scope.activateTool($scope.toolBox[0]);

        $('body').addClass('sc-sketcher-bg-color-light');
    }

    function getSVGUrl () {
        if (angular.isUndefined($scope.currentIssue)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuild.selected();
        return HostnameAndPort.forLink() + 'rest/branch/' + selected.branch + '/issue/' + $scope.currentIssue.issueId
            + '/scenariosketch/' + $scope.scenarioSketchId + '/stepsketch/' + $scope.stepSketchId + '/svg/1';
    }

    function setAuthorFromLocalStorageIfAvailable() {
        if ($scope.mode !== MODE_CREATE) {
            return;
        }

        var author = scLocalStorage.get(AUTHOR_LOCAL_STORAGE_KEY);

        if (!angular.isString(author) || author.length === 0) {
            return;
        }

        $scope.issueAuthor = author;
    }

    function storeAuthorInLocalStorage() {
        scLocalStorage.set(AUTHOR_LOCAL_STORAGE_KEY, $scope.issueAuthor);
    }

    $scope.activateTool = function (tool) {
        if ($scope.currentTool) {
            $scope.currentTool.deactivate();
        }
        $scope.currentTool = tool;
        tool.activate();

        DrawingPadService.unSelectAllShapes();

        $('.tooltip').hide().delay(100);
    };

    $scope.getExitSketcherPath = function () {
        if($scope.issueId && $scope.scenarioSketchId && $scope.stepSketchId) {
            return '#/stepsketch/' + $scope.issueId + '/' + $scope.scenarioSketchId + '/' + $scope.stepSketchId;
        } else {
            return undefined;
        }
    };

    // TODO extract all saving related methods into a service
    $scope.saveSketcherData = function () {

        DrawingPadService.unSelectAllShapes();

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;
        $scope.savingSketch = true;

        var issue = new IssueResource({
            branchName: $routeParams.branch,
            name: $scope.issueName,
            description: $scope.issueDescription,
            author: $scope.issueAuthor
        });

        if ($scope.mode === MODE_CREATE) {
            issue.relatedStep = ContextService.stepIdentifier;
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

        if ($scope.issueSaved !== 1) {
            return;
        }

        var scenarioSketch = {
            branchName: $routeParams.branch,
            author: $scope.issueAuthor,
            issueId: args.issueId
        };

        ++$scope.scenarioSketchSaved;

        if ($scope.scenarioSketchId && $scope.scenarioSketchId !== undefined) {
            scenarioSketch.scenarioSketchId = $scope.scenarioSketchId;
            updateExistingScenarioSketch(scenarioSketch);
        } else {
            saveNewScenarioSketch(scenarioSketch);
        }

        function saveNewScenarioSketch(newScenarioSketch) {
            saveScenarioSketch(newScenarioSketch, function (savedScenarioSketch) {
                $log.log('SAVE ScenarioSketch', savedScenarioSketch.scenarioSketchId);
                $rootScope.$broadcast('ScenarioSketchSaved', {
                    issueId: savedScenarioSketch.issueId,
                    scenarioSketchId: savedScenarioSketch.scenarioSketchId
                });
            }, function (error) {
                sketchSavedWithError(error);
            });
        }

        function updateExistingScenarioSketch(newVersionOfScenarioSketch) {
            saveScenarioSketch(newVersionOfScenarioSketch, function (updatedScenarioSketch) {
                $log.log('UPDATE ScenarioSketch', updatedScenarioSketch.scenarioSketchId);
                $rootScope.$broadcast('ScenarioSketchSaved', {
                    issueId: updatedScenarioSketch.issueId,
                    scenarioSketchId: updatedScenarioSketch.scenarioSketchId
                });
            }, function (error) {
                sketchSavedWithError(error);
            });
        }

        function saveScenarioSketch(scenarioSketchToSend, successCallback, errorCallback) {
            ScenarioSketchResource.save(scenarioSketchToSend,
                function (updatedScenarioSketch) {
                    if (successCallback) {
                        successCallback(updatedScenarioSketch);
                    }
                },
                function (error) {
                    $log.error(error);
                    if (errorCallback) {
                        errorCallback('ScenarioSketch could not be saved');
                    }
                });
        }

    });

    $rootScope.$on('ScenarioSketchSaved', function (event, args) {

        var exportedSVG = DrawingPadService.exportDrawing();

        var stepSketch = new StepSketchResource({
            branchName: $routeParams.branch,
            svgXmlString: exportedSVG,
            issueId: args.issueId,
            scenarioSketchId: args.scenarioSketchId
        }, {});

        if ($scope.mode === MODE_CREATE) {
            stepSketch.relatedStep = ContextService.stepIdentifier;
        }

        if ($scope.scenarioSketchSaved === 1) {
            if ($scope.stepSketchId && $scope.stepSketchId !== undefined) {
                stepSketch.stepSketchId = $scope.stepSketchId;

                StepSketch.saveStepSketch(stepSketch, function (updatedStepSketch) {
                    $log.log('UPDATE StepSketch', updatedStepSketch.stepSketchId);
                    sketchSuccessfullySaved();
                }, function (error) {
                    sketchSavedWithError(error);
                });
            } else {
                StepSketch.saveStepSketch(stepSketch, function (savedStepSketch) {
                    $log.log('SAVE StepSketch', savedStepSketch.stepSketchId);

                    $scope.scenarioSketchId = args.scenarioSketchId;
                    $scope.stepSketchId = savedStepSketch.stepSketchId;
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
        $scope.savingSketch = false;
    }

    function sketchSavedWithError(error) {
        $scope.addAlert('danger', 'saveSketchFailedMessage', 'The sketch has not been saved. Reason: ' + error);

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;

        if ($scope.mode === MODE_CREATE) {
            //if ($scope.issueId) {
            //    Issue.deleteSketcherData($scope.issueId);
            //}

            $scope.issueId = null;
            $scope.scenarioSketchId = null;
            $scope.stepSketchId = null;
        }
        $scope.savingSketch = false;
    }

    $rootScope.$on('drawingEnded', function (scope, shape) {
        // $scope.$apply is used to make sure that the button disabled directive updates in the view
        $scope.$apply($scope.activateTool($scope.toolBox[0]));

        shape.selectToggle();
        DrawingPadService.setSelectedShape(shape);
        $log.log('drawing ended');
    });

    // Mark form as dirty if the drawing is changed
    $rootScope.$on('edit_drawing_event', function() {
        if($scope.sketcherForm.unsavedDrawingChanges) {
            $scope.sketcherForm.unsavedDrawingChanges.$setViewValue($scope.sketcherForm.unsavedDrawingChanges.$viewValue);
        }
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
        var relatedStep;

        if ($scope.mode === MODE_EDIT && $scope.currentIssue) {
            relatedStep = $scope.currentIssue.relatedStep;
        } else if($scope.mode === MODE_CREATE && ContextService.stepIdentifier) {
            relatedStep = ContextService.stepIdentifier;
        } else {
            return '';
        }

        var humanReadableFilter = $filter('scHumanReadable');

        return 'Use Case: ' + humanReadableFilter(relatedStep.usecaseName) + ' / Scenario: ' + humanReadableFilter(relatedStep.scenarioName)
            + ' / Step: ' + relatedStep.pageName + '/' + relatedStep.pageOccurrence + '/' + relatedStep.stepInPageOccurrence;
    };

    $scope.zoomFactor = ZoomPanService.getZoomFactor();

    $rootScope.$on(ZoomPanService.ZOOM_FACTOR_CHANGED, function() {
        $scope.zoomFactor = ZoomPanService.getZoomFactor();
        $timeout(function() {
            $scope.$apply();
        });
    });

    $scope.$on('$destroy', function () {
        DrawingPadService.destroy();
        $scope.issueId = null;
        $scope.scenarioSketchId = null;
        $scope.stepSketchId = null;
        $('body').removeClass('sc-sketcher-bg-color-light');
    });

    // Alerts to give feedback whether saving the sketch was successful or not

    $scope.alerts = [];

    $scope.addAlert = function (type, id, message) {
        var alertEntry = {type: type, id: id, message: message};
        $scope.alerts.push(alertEntry);
        // We have to set our own timeout using the $interval service because using $timeout provokes issues
        // with protractor. See:
        // - https://github.com/angular-ui/bootstrap/pull/3982
        // - https://github.com/angular/protractor/issues/169
        $interval(function () {
            $scope.closeAlert(alertEntry);
        }, 5000, 1);
    };

    $scope.closeAlert = function (alertEntry) {
        var index = $scope.alerts.indexOf(alertEntry);
        $scope.alerts.splice(index, 1);
    };

    activate();

});
