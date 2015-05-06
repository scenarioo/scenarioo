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

angular.module('scenarioo.sketcher.controllers').controller('EditorCtrl', function ($scope, $location, $filter, GlobalHotkeysService, SelectedBranchAndBuild) {

  var drawingPad = SVG('drawingPad').size('100%', '100%').fixSubPixelOffset();

  var currentTool = null;
  var listOfTools = null; // TODO: Service which loads tools from an extension directory?

  $scope.rectTool = RectTool();
  $scope.selectTool = SelectTool();

  function SelectTool(){
    var that = {};
    that.name = 'Select Tool';
    that.icon = null;
    that.tooltip = 'This will be the tool tip.';
    that.cursor = null;
    that.onmousedown = function(event){

    };

    that.onmouseup = function(event){
      console.log(event.target.id);
      var element = SVG.get(event.target.id);
      element.fill('#f06');
    };

    that.onmousedrag = function(event){

    };

    return that;
  }

  $scope.activateTool = function (tool){
    currentTool = tool;
    console.log("Activated tool: " + currentTool.name);
    drawingPad.on('mousedown', currentTool.onmousedown);
    drawingPad.on('mouseup', currentTool.onmouseup);
    drawingPad.on('mousemove', currentTool.onmousedrag);
  }

  function toolDeactivated(){
    console.log("Tool deactivated");
    $scope.activateTool(SelectTool);
  }

  $scope.activateTool(SelectTool());

  function RectTool(){
    var that = {};
    that.name = 'Rectangle Tool';
    that.icon = null;
    that.tooltip = 'This tool is used to draw rectangles.';
    that.cursor = null;

    var newRect = null;
    var mousePosX1 = 0;
    var mousePosY1 = 0;
    var mousePosLastX = 0;
    var mousePosLastY = 0;
    var mousedown = false;
    that.onmousedown = function(event){
      mousedown = true;
      newRect = drawingPad.rect(0,0,0,0).draggable();

      mousePosX1 = event.offsetX;
      mousePosY1 = event.offsetY;

      newRect.attr({
        x: mousePosX1,
        y: mousePosY1,
        fill: '#f60',
        ngMouseMove: 'paperMousemove'
      });

      mousePosLastX = mousePosX1;
      mousePosLastY = mousePosY1;
    };

    that.onmouseup = function(event){
      mousedown = false;

      newRect.attr("fill", "#0f3");
      mousePosX1 = 0;
      mousePosY1 = 0;
      mousePosLastX = 0;
      mousePosLastY = 0;

      toolDeactivated();
    };

    that.onmousedrag = function(event){
      if (!mousedown) {
        return;
      }

      var dx = event.offsetX - mousePosLastX;
      var dy = event.offsetY - mousePosLastY;

      var originX = Math.min(mousePosX1, event.offsetX);
      var originY = Math.min(mousePosY1, event.offsetY);

      newRect.attr({
        width: newRect.attr('width') + Math.abs(dx),
        height: newRect.attr('height') + Math.abs(dy),
        x: originX,
        y: originY
      });

      //console.log(newRect.attr('width'), newRect.attr('height'), dx, dy);

      mousePosLastX = event.offsetX;
      mousePosLastY = event.offsetY;
    };

    return that;
  }


});
