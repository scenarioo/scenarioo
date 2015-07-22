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
                                                                           GlobalHotkeysService, SelectedBranchAndBuild, ToolBox,
                                                                           DrawingPadService, SketchStep, SketchStepResource, IssueResource, Issue,
                                                                           ScenarioSketchResource, ScenarioSketch, ContextService) {

    $scope.currentTool = null;


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

        var issue = new IssueResource({
            branchName: $routeParams.branch,
            name: $scope.issueName,
            description: $scope.issueDescription,
            author: $scope.issueAuthor,
            usecaseContextName: ContextService.usecaseName,
            usecaseContextLink: ContextService.usecaseLink,
            scenarioContextName: ContextService.scenarioName,
            scenarioContextLink: ContextService.scenarioLink,
            stepContextLink: ContextService.stepLink
        });
        console.log(issue);

        if ($scope.issueId) {
            issue.issueId = $scope.issueId;

            Issue.updateIssue(issue, function (updatedIssue) {
                console.log('UPDATE Issue', updatedIssue.issueId);
                $rootScope.$broadcast('IssueSaved', {issueId: updatedIssue.issueId});
            });
        } else {
            Issue.saveIssue(issue, function (savedIssue) {
                console.log('SAVE Issue', savedIssue.issueId);
                $rootScope.$broadcast('IssueSaved', {issueId: savedIssue.issueId});
            });
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

        if ($scope.scenarioSketchId) {
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
    });

    $rootScope.$on('ScenarioSketchSaved', function (event, args) {

        var exportedSVG = DrawingPadService.exportDrawing();

        var sketchStep = new SketchStepResource({
            branchName: $routeParams.branch,
            scenarioSketchId: args.scenarioSketchId,
            sketchStepName: 1,
            sketch: exportedSVG,
            issueId: args.issueId,
            contextInDocu: decodeURIComponent($routeParams.screenshotURL)
        }, {});

        if ($scope.sketchStepId) {
            sketchStep.sketchStepId = $scope.sketchStepId;

            SketchStep.updateSketchStep(sketchStep, function (updatedSketchStep) {
                console.log('UPDATE SketchStep', updatedSketchStep.sketchStepId);
            });
        } else {
            SketchStep.updateSketchStep(sketchStep, function (savedSketchStep) {
                console.log('SAVE SketchStep', savedSketchStep.sketchStepId);

                $scope.successfullyUpdatedSketchStep = true;
                $scope.sketcherButtonName = 'Update';
                $scope.issueId = args.issueId;
                $scope.scenarioSketchId = args.scenarioSketchId;
                $scope.sketchStepId = args.sketchStepId;
            });
        }
        //console.log(exportedSVG);
    });

    $rootScope.$on('drawingEnded', function (scope, shape) {
        // $scope.$apply is used to make sure that the button disabled directive updates in the view
        $scope.$apply($scope.activateTool($scope.tools[0]));

        shape.selectToggle();
        DrawingPadService.setSelectedShape(shape);
    });

    $rootScope.$on('shapeSelected', function (scope, shape) {
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
        //DrawingPadService.init();
        var drawingPad = SVG('drawingPad').spof();
        DrawingPadService.setDrawingPad(drawingPad);

        $scope.tools = ToolBox;
        $scope.activateTool($scope.tools[0]);
    };
    $scope.init();


    $scope.$on('$destroy', function () {
        console.log(DrawingPadService.getDrawingPad());
        DrawingPadService.destroy();
        console.log('destroyed');
    });

});
