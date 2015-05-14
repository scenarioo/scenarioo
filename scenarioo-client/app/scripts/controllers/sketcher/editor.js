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

angular.module('scenarioo.controllers').controller('EditorCtrl', function ($scope, $location, $filter, $routeParams, GlobalHotkeysService, SelectedBranchAndBuild, Tool, SelectTool, RectTool, CircleTool, EllipseTool, DrawingPadService, SketchStep, SketchStepResource) {

    var drawingPad = DrawingPadService.get;
    var image = null;
    if ($routeParams.screenshotURL) {
        image = drawingPad.image($routeParams.screenshotURL).loaded(function (loader) {
            image.attr({
                width: drawingPad.width()
            });
            drawingPad.attr({
                height: image.height()
            });
        });
    }

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
        console.log($scope.currentTool);
        $scope.currentTool = tool;
        console.log($scope.currentTool);
        Tool.activate(tool);
    };

    $scope.activateTool($scope.tools[0]);

    $scope.isButtonDisabled = function (tool) {
        if (tool) {
            return Tool.isButtonDisabled(tool);
        }
    };

    // exporting svg drawing
    $scope.updateDrawing = function () {
        var exportedSVG = DrawingPadService.exportDrawing();
        console.log(exportedSVG);

        $scope.successfullyUpdatedSketchStep = false;

        var changedSketchStep = new SketchStepResource({
            branchName: $routeParams.branch,
            issueName: 'test issue',
            proposalName: 'test proposal',
            sketchStepName: 1,
            sketch: exportedSVG
        });

        SketchStep.updateSketchStep(changedSketchStep, function () {
            $scope.successfullyUpdatedSketchStep = true;
        });
    };

});
