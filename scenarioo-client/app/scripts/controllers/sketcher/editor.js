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
/* eslint no-console:0*/


angular.module('scenarioo.controllers').controller('EditorCtrl', function ($rootScope, $scope, $location, $filter, $timeout, $routeParams, $route,
                                                                           GlobalHotkeysService, SelectedBranchAndBuild, ToolBox, DrawShapeService,
                                                                           DrawingPadService, SketchStep, SketchStepResource, IssueResource, Issue,
                                                                           ScenarioSketchResource, ScenarioSketch, ContextService) {


    var mode = $location.search().mode;
    $scope.currentTool = null;

    console.log(mode);

    $scope.issueSaved = 0;
    $scope.scenarioSketchSaved = 0;
    $scope.sketchStepSaved = 0;


    $scope.activateTool = function (tool) {
        if ($scope.currentTool) {
            $scope.currentTool.deactivate();
        }
        $scope.currentTool = tool;
        tool.activate();

        DrawingPadService.unSelectAllShapes();
    };

    // exporting svg drawing
    $scope.updateSketchStep = function () {

        DrawingPadService.unSelectAllShapes();

        $scope.successfullyUpdatedSketchStep = false;

        if (mode === 'edit') {
            $scope.issueId = ContextService.issueId;
            $scope.scenarioSketchId = ContextService.scenarioSketchId;
            $scope.sketchStepName = parseInt(ContextService.sketchStepName);
            $scope.sketcherButtonName = 'Update';
        }

        var issue = new IssueResource({
            branchName: $routeParams.branch,
            name: $scope.issueName,
            description: $scope.issueDescription,
            author: $scope.issueAuthor
        });

        if (mode === 'create') {
            issue.usecaseContextName = ContextService.usecaseName;
            issue.usecaseContextLink = ContextService.usecaseLink;
            issue.scenarioContextName = ContextService.scenarioName;
            issue.scenarioContextLink = ContextService.scenarioLink;
            issue.stepContextLink = ContextService.stepLink;
        }

        if($scope.issueSaved === 0) {
            $scope.issueSaved++;

            if ($scope.issueId && $scope.issueId !== undefined) {
                issue.issueId = $scope.issueId;

                Issue.updateIssue(issue, function (updatedIssue) {
                    console.log('UPDATE Issue', updatedIssue.issueId);
                    $rootScope.$broadcast('IssueSaved', {issueId: updatedIssue.issueId});
                });
            } else {
                Issue.updateIssue(issue, function (savedIssue) {
                    console.log('SAVE Issue', savedIssue.issueId);
                    $rootScope.$broadcast('IssueSaved', {issueId: savedIssue.issueId});
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


        if($scope.issueSaved === 1) {
            $scope.scenarioSketchSaved++;

            if ($scope.scenarioSketchId && $scope.scenarioSketchId !== undefined) {
                scenarioSketch.scenarioSketchId = $scope.scenarioSketchId;

                ScenarioSketch.updateScenarioSketch(scenarioSketch, function (updatedScenarioSketch) {
                    console.log('UPDATE ScenarioSketch', updatedScenarioSketch.scenarioSketchId);
                    $rootScope.$broadcast('ScenarioSketchSaved', {
                        issueId: updatedScenarioSketch.issueId,
                        scenarioSketchId: updatedScenarioSketch.scenarioSketchId
                    });
                });
            } else {
                ScenarioSketch.updateScenarioSketch(scenarioSketch, function (savedScenarioSketch) {
                    console.log('SAVE ScenarioSketch', savedScenarioSketch.scenarioSketchId);
                    $rootScope.$broadcast('ScenarioSketchSaved', {
                        issueId: savedScenarioSketch.issueId,
                        scenarioSketchId: savedScenarioSketch.scenarioSketchId
                    });
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

        if (mode === 'create') {
            sketchStep.usecaseContextName = ContextService.usecaseName;
            sketchStep.usecaseContextLink = ContextService.usecaseLink;
            sketchStep.scenarioContextName = ContextService.scenarioName;
            sketchStep.scenarioContextLink = ContextService.scenarioLink;
            sketchStep.contextInDocu = decodeURIComponent($location.search().url);
        }


        if($scope.scenarioSketchSaved === 1) {

            if ($scope.sketchStepName && $scope.sketchStepName !== undefined) {
                sketchStep.sketchStepName = $scope.sketchStepName;

                SketchStep.updateSketchStep(sketchStep, function (updatedSketchStep) {
                    console.log('UPDATE SketchStep', updatedSketchStep.sketchStepName);
                    sketchSuccessfullyUpdated();
                });
            } else {
                SketchStep.updateSketchStep(sketchStep, function (savedSketchStep) {
                    console.log('SAVE SketchStep', savedSketchStep.sketchStepName);

                    $scope.issueId = args.issueId;
                    $scope.scenarioSketchId = args.scenarioSketchId;
                    $scope.sketchStepName = savedSketchStep.sketchStepName;

                    sketchSuccessfullyUpdated();
                });
            }
        }
        //console.log(exportedSVG);
    });

    function sketchSuccessfullyUpdated() {
        $scope.successfullyUpdatedSketchStep = true;
        $scope.sketcherButtonName = 'Update';

        $scope.issueSaved = 0;
        $scope.scenarioSketchSaved = 0;
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
        console.log(DrawingPadService.DRAWINGPAD_CLICKED_EVENT);
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

        $scope.tools = ToolBox;
        $scope.activateTool($scope.tools[0]);
    };
    $scope.init();


    $scope.$on('$destroy', function () {
        DrawingPadService.destroy();
        $scope.issueId = null;
        $scope.scenarioSketchId = null;
        $scope.sketchStepName = null;
        console.log('editor scope destroyed');
    });

});
