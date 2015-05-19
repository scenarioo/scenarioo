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

'use strict';

angular.module('scenarioo.controllers').controller('EditorCtrl', function ($scope, $location, $filter, $routeParams, $route, GlobalHotkeysService, SelectedBranchAndBuild, Tool, SelectTool, RectTool, CircleTool, EllipseTool, DrawingPadService, SketchStep, SketchStepResource, IssueResource, Issue, ScenarioSketchResource, ScenarioSketch) {

    //var drawingPad = DrawingPadService.get;
    var drawingPad = SVG('drawingPad').size('100%', '100%').fixSubPixelOffset();
    $scope.currentTool = null;

    $scope.tools = new Array();
    $scope.tools[0] = SelectTool;
    $scope.tools[1] = RectTool;
    $scope.tools[2] = CircleTool;
    $scope.tools[3] = EllipseTool;

    $scope.activateTool = function (tool) {
        if ($scope.currentTool) {
            Tool.deactivate($scope.currentTool);
        }
        $scope.currentTool = tool;
        Tool.activate(tool);
    };

    $scope.activateTool($scope.tools[0]);

    $scope.isButtonDisabled = function (tool) {
        if (tool) {
            return Tool.isButtonDisabled(tool);
        }
    };

    // exporting svg drawing
    $scope.updateSketchStep = function () {
        var exportedSVG = DrawingPadService.exportDrawing();

        $scope.successfullyUpdatedSketchStep = false;

        var issue = new IssueResource({
            branchName: $routeParams.branch,
            name: $scope.issueName,
            description: $scope.issueDescription,
            author: $scope.issueAuthor
        });

        var scenarioSketch = new ScenarioSketchResource({
            branchName: $routeParams.branch,
            scenarioSketchName: $scope.scenarioSketchName
        }, {});

        var sketchStep = new SketchStepResource({
            branchName: $routeParams.branch,
            scenarioSketchName: $scope.scenarioSketchName,
            sketchStepName: 1,
            sketch: exportedSVG
        }, {});

        if ($scope.issueId) {
            issue.issueId = $scope.issueId;

            Issue.updateIssue(issue, function (updatedIssue) {
                console.log('UPDATE', updatedIssue);

                ScenarioSketch.updateScenarioSketch(scenarioSketch, function (updatedScenarioSketch) {
                    console.log(updatedScenarioSketch);

                    SketchStep.updateSketchStep(sketchStep, function (updatedSketchStep) {
                        console.log(updatedSketchStep);
                        $scope.successfullyUpdatedSketchStep = true;
                    });
                });
            });
        } else {
            Issue.saveIssue(issue, function (savedIssue) {
                console.log('SAVE', savedIssue);
                sketchStep.issueId = savedIssue.id;
                scenarioSketch.issueId = savedIssue.id;
                $scope.issueId = savedIssue.id;

                ScenarioSketch.updateScenarioSketch(scenarioSketch, function (savedScenarioSketch) {
                    console.log(savedScenarioSketch);

                    SketchStep.updateSketchStep(sketchStep, function (savedSketchStep) {
                        console.log(savedSketchStep);
                        $scope.successfullyUpdatedSketchStep = true;
                        $scope.sketcherButtonName = 'Update';
                    });
                });
            });
        }
    };

    $scope.initSVGBackgroundImage = function () {
        var image = null;

        if ($routeParams.screenshotURL) {
            image = drawingPad.image($routeParams.screenshotURL).loaded(function (loader) {
                drawingPad.attr({
                    width: image.width()
                });
                drawingPad.attr({
                    height: image.height()
                });
            });
        }
    };
    $scope.initSVGBackgroundImage();


});
