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

angular.module('scenarioo.controllers').factory('CircleTool', function (Tool) {
    var tool = Tool.get;

    tool.name = 'Circle Tool';
    tool.icon = null;
    tool.tooltip = 'This tool is used to draw circles.';
    tool.cursor = 'crosshair';
    var newCircle = null;
    var mousePosX1 = 0;
    var mousePosY1 = 0;
    var mousePosLastX = 0;
    var mousePosLastY = 0;
    var mousedown = false;


    tool.onmousedown = function (event) {
        mousedown = true;
        newCircle = tool.drawingPad.circle(0);


        mousePosX1 = event.layerX;
        mousePosY1 = event.layerY;

        newCircle.attr({
            cx: mousePosX1,
            cy: mousePosY1,
            fill: '#f60'
        });

        mousePosLastX = mousePosX1;
        mousePosLastY = mousePosY1;
    };

    tool.onmouseup = function (event) {
        mousedown = false;

        newCircle.attr('fill', '#0f3');
        mousePosX1 = 0;
        mousePosY1 = 0;
        mousePosLastX = 0;
        mousePosLastY = 0;
    };

    tool.onmousedrag = function (event) {
        if (!mousedown) {
            return;
        }

        var delta = 0;
        var offsetToOriginX = event.layerX - mousePosX1;
        var offsetToOriginY = event.layerY - mousePosY1;
        var dx = 0;
        var dy = 0;

        if (offsetToOriginX > 0 && offsetToOriginY > 0) {
            dx = event.layerX - mousePosLastX;
            dy = event.layerY - mousePosLastY;
        } else if (offsetToOriginX > 0 && offsetToOriginY < 0) {
            dx = event.layerX - mousePosLastX;
            dy = mousePosLastY - event.layerY;
        } else if (offsetToOriginX < 0 && offsetToOriginY < 0) {
            dx = mousePosLastX - event.layerX;
            dy = mousePosLastY - event.layerY;
        } else {
            dx = mousePosLastX - event.layerX;
            dy = event.layerY - mousePosLastY;
        }

        delta = ((dx + dy) / 2);
        if (delta > 10) {
            delta = 10;
        } else if (delta < -20) {
            delta = -20;
        }

        var rx = newCircle.attr('rx');
        if (delta < (rx * -1)) {
            delta = rx * -1;
        }

        newCircle.attr({
            rx: newCircle.attr('rx') + delta,
            ry: newCircle.attr('ry') + delta
        });

        mousePosLastX = event.layerX;
        mousePosLastY = event.layerY;
    };

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
