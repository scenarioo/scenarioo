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


angular.module('scenarioo.controllers').controller('EditorCtrl', function ($rootScope, $scope, $location, $filter, $timeout, $routeParams, $route, GlobalHotkeysService, SelectedBranchAndBuild, Tool, SelectTool, RectTool, EllipseTool, NoteTool, DrawingPadService, SketchStep, SketchStepResource, IssueResource, Issue, ScenarioSketchResource, ScenarioSketch) {

    var drawingPad;
    $scope.currentTool = null;


    $scope.activateTool = function (tool) {
        if ($scope.currentTool) {
            Tool.deactivate($scope.currentTool);
        }
        $scope.currentTool = tool;
        Tool.activate(tool);
    };

    $scope.isButtonDisabled = function (tool) {
        if (tool) {
            return Tool.isButtonDisabled(tool);
        }
    };

    // exporting svg drawing
    $scope.updateSketchStep = function () {

        $scope.successfullyUpdatedSketchStep = false;

        var issue = new IssueResource({
            branchName: $routeParams.branch,
            name: $scope.issueName,
            description: $scope.issueDescription,
            author: $scope.issueAuthor
        });

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

        var scenarioSketch = new ScenarioSketchResource({
            branchName: $routeParams.branch,
            scenarioSketchName: $scope.scenarioSketchName,
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
            issueId: args.issueId
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

        console.log(exportedSVG);
    });

    var loadBackgroundImage = function () {
        if ($routeParams.screenshotURL) {
            var img = drawingPad.image(decodeURIComponent($routeParams.screenshotURL)).loaded(function (loader) {
                drawingPad.attr({
                    width: loader.width
                });
                drawingPad.attr({
                    height: loader.height
                });
            });
            img.attr({
                id: 'sketcher-original-screenshot',
                draggable: false
            });

            document.getElementById('sketcher-original-screenshot').ondragstart = function() { return false; };
        }
    };


    $scope.init = function() {

        $timeout(function () {
            return DrawingPadService.get;
        }, 1000).then(function (result) {
            drawingPad = result;
            loadBackgroundImage();
        });

        $scope.tools = [SelectTool, RectTool, EllipseTool, NoteTool];
        $scope.activateTool($scope.tools[0]);
    };
    $scope.init();

});
