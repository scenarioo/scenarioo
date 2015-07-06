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
/* eslint no-console:0*/


angular.module('scenarioo.controllers').controller('EditorCtrl', function ($rootScope, $scope, $location, $filter, $timeout, $routeParams, $route, GlobalHotkeysService, SelectedBranchAndBuild, Tool, SelectTool, RectTool, EllipseTool, NoteTool, BasicShapeTool, BorderShapeTool, DrawingPadService, SketchStep, SketchStepResource, IssueResource, Issue, ScenarioSketchResource, ScenarioSketch) {

    var drawingPad;
    $scope.currentTool = null;


    $scope.activateTool = function (tool) {
        if ($scope.currentTool) {
            Tool.deactivate($scope.currentTool);
        }
        $scope.currentTool = tool;
        Tool.activate(tool);

        DrawingPadService.unSelectAllShapes(DrawingPadService.get);
    };

    $scope.isButtonDisabled = function (tool) {
        if (tool) {
            return Tool.isButtonDisabled(tool);
        }
    };

    // exporting svg drawing
    $scope.updateSketchStep = function () {

        DrawingPadService.unSelectAllShapes(DrawingPadService.get);

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

        if($scope.scenarioSketchName == null) {
            $scope.scenarioSketchName = 'undefined';
        }

        var scenarioSketch = new ScenarioSketchResource({
            branchName: $routeParams.branch,
            scenarioSketchName: $scope.scenarioSketchName,
            description: $scope.scenarioSketchDescription,
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

        console.log(exportedSVG);
    });

    $rootScope.$on(Tool.DRAWING_ENDED_EVENT, function (scope, shape) {
        // $scope.$apply is used to make sure that the button disabled directive updates in the view
        $scope.$apply($scope.activateTool($scope.tools[0]));

        shape.selectToggle();
    });

    $rootScope.$on(Tool.SHAPE_SELECTED_EVENT, function (scope, shape) {
        DrawingPadService.unSelectAllShapes(DrawingPadService.get);
        shape.selectToggle();
    });

    $rootScope.$on(DrawingPadService.DRAWINGPAD_CLICKED_EVENT, function () {
        console.log(DrawingPadService.DRAWINGPAD_CLICKED_EVENT);
        DrawingPadService.unSelectAllShapes(DrawingPadService.get);
    });



    var loadBackgroundImage = function () {
        if ($routeParams.screenshotURL) {
            convertImgToBase64URL(decodeURIComponent($routeParams.screenshotURL), function(base64Img){
                var img = DrawingPadService.get.image(base64Img).loaded(function (loader) {
                    drawingPad.attr({
                        width: loader.width,
                        height: loader.height
                    });
                });
                img.attr({
                    id: DrawingPadService.backgroundImageId,
                    draggable: false
                });

                if(document.getElementById('sketcher-original-screenshot')) {
                    document.getElementById('sketcher-original-screenshot').ondragstart = function() { return false; };
                }
            });

        }
    };

    function convertImgToBase64URL(url, callback, outputFormat){
        var img = new Image();
        img.crossOrigin = 'Anonymous';
        img.onload = function(){
            var canvas = document.createElement('CANVAS'),
                ctx = canvas.getContext('2d'), dataURL;
            canvas.height = this.height;
            canvas.width = this.width;
            ctx.drawImage(this, 0, 0);
            dataURL = canvas.toDataURL(outputFormat);
            callback(dataURL);
            canvas = null; //TODO: Does this destroy the canvas element? Does it matter if not?
        };
        img.src = url;
    }


    $scope.init = function() {

        $timeout(function () {
            return DrawingPadService.root;
        }, 1000).then(function (result) {
            drawingPad = result;
            loadBackgroundImage();
        });

        //var panZoom = svgPanZoom('#' + DrawingPadService.drawingPadNodeId);

        $scope.tools = [SelectTool, RectTool, EllipseTool, NoteTool, BasicShapeTool, BorderShapeTool];
        $scope.activateTool($scope.tools[0]);
    };
    $scope.init();


});
