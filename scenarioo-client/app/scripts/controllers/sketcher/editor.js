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


angular.module('scenarioo.controllers').controller('EditorCtrl', function ($rootScope, $scope, $location, $filter, $timeout, $routeParams, $route,
                                                                           GlobalHotkeysService, SelectedBranchAndBuild, ToolBox, DrawShapeService,
                                                                           DrawingPadService, SketchStep, SketchStepResource, IssueResource, Issue,
                                                                           ScenarioSketchResource, ScenarioSketch, ContextService, $log, $window) {

    function initEditMode() {

        $scope.issueId = ContextService.issueId;
        $scope.scenarioSketchId = ContextService.scenarioSketchId;
        $scope.sketchStepName = parseInt(ContextService.sketchStepName);

        if(ContextService.issueId) {
            Issue.load(ContextService.issueId);
        }
    }

    $rootScope.$on(Issue.ISSUE_LOADED_EVENT, function (event, result) {
        var currentIssue = result.issue;
        $scope.issueName = currentIssue.name;
        $scope.issueDescription = currentIssue.description;
        $scope.issueAuthor = currentIssue.author;
    });


    $scope.activateTool = function (tool) {
        if ($scope.currentTool) {
            $scope.currentTool.deactivate();
        }
        $scope.currentTool = tool;
        tool.activate();

        DrawingPadService.unSelectAllShapes();
    };

    $scope.exitSketcher = function() {
        $window.history.back();
    }

    $scope.saveSketcherData = function () {

        DrawingPadService.unSelectAllShapes();

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;
        $scope.successfullyUpdatedSketchStep = false;

        var issue = new IssueResource({
            branchName: $routeParams.branch,
            name: $scope.issueName,
            description: $scope.issueDescription,
            author: $scope.issueAuthor
        });

        if ($scope.mode === 'create') {
            issue.usecaseContextName = ContextService.usecaseName;
            issue.usecaseContextLink = ContextService.usecaseLink;
            issue.scenarioContextName = ContextService.scenarioName;
            issue.scenarioContextLink = ContextService.scenarioLink;
            issue.stepContextLink = ContextService.stepLink;
        }

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

        if ($scope.mode === 'create') {
            sketchStep.usecaseContextName = ContextService.usecaseName;
            sketchStep.usecaseContextLink = ContextService.usecaseLink;
            sketchStep.scenarioContextName = ContextService.scenarioName;
            sketchStep.scenarioContextLink = ContextService.scenarioLink;
            sketchStep.stepContextLink = ContextService.stepLink;
            sketchStep.contextInDocu = decodeURIComponent($location.search().url);
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

                    sketchSuccessfullySaved();
                }, function (error) {
                    sketchSavedWithError(error);
                });
            }
        }
    });

    function sketchSuccessfullySaved() {
        $scope.successfullyUpdatedSketchStep = true;

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;

        $timeout(function() {
            $scope.successfullyUpdatedSketchStep = false;
        }, 5000);
    }

    function sketchSavedWithError(error) {
        $scope.notSuccessfullyUpdatedSketch = true;
        $scope.sketchErrorMsg = error;

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;

        if ($scope.mode === 'create') {
            $log.info('about to send the delete request');
            if($scope.issueId) {
                $log.info('NOW!');
                Issue.deleteSketcherData($scope.issueId);
            }

            $scope.issueId = null;
            $scope.scenarioSketchId = null;
            $scope.sketchStepName = null;
        }

        $timeout(function() {
            $scope.notSuccessfullyUpdatedSketch = false;
        }, 5000);
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


    $scope.init = function () {
        var drawingPad = SVG('drawingPad').spof();
        DrawingPadService.setDrawingPad(drawingPad);

        $scope.mode = $location.search().mode;
        $scope.currentTool = null;
        $scope.tools = ToolBox;
        $scope.activateTool($scope.tools[0]);

        if($scope.mode === 'edit') {
            initEditMode();
        }
    };
    $scope.init();


    $scope.$on('$destroy', function () {
        DrawingPadService.destroy();
        $scope.issueId = null;
        $scope.scenarioSketchId = null;
        $scope.sketchStepName = null;
    });

});
