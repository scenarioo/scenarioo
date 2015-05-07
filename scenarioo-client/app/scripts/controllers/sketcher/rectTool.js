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

angular.module('scenarioo.controllers').factory('RectTool', function(Tool) {
    //var tool = Object.create(Tool);
    var tool = Tool.get;

    tool.name = 'Rectangle Tool';
    tool.icon = null;
    tool.tooltip = 'This tool is used to draw rectangles.';
    tool.cursor = 'crosshair';
    var newRect = null;
    var mousePosX1 = 0;
    var mousePosY1 = 0;
    var mousePosLastX = 0;
    var mousePosLastY = 0;
    var mousedown = false;



    tool.onmousedown = function(event) {
        mousedown = true;
        newRect = tool.drawingPad.rect(0,0,0,0);

        mousePosX1 = event.offsetX;
        mousePosY1 = event.offsetY;

        newRect.attr({
            x: mousePosX1,
            y: mousePosY1,
          fill: '#f60'
        });

        mousePosLastX = mousePosX1;
        mousePosLastY = mousePosY1;
      }

    tool.onmouseup = function(event) {
        mousedown = false;

        newRect.attr('fill', '#0f3');
        mousePosX1 = 0;
        mousePosY1 = 0;
        mousePosLastX = 0;
        mousePosLastY = 0;
      }

    tool.onmousedrag = function(event) {
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
      }

    return {

      name: tool.name,
      icon: tool.icon,
      tooltip: tool.tooltip,
      cursor: tool.cursor,

      onmouseup: tool.onmouseup,
      onmousedown: tool.onmousedown,
      onmousedrag: tool.onmousedrag


    };

});
