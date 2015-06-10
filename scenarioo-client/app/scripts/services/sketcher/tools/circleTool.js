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


angular.module('scenarioo.controllers').factory('CircleTool', function (AbstractShapeTool) {
    var tool = AbstractShapeTool.get;

    tool.name = 'Circle Tool';
    tool.icon = null;
    tool.tooltip = 'This tool is used to draw circles.';

    var mousePosLastX = 0;
    var mousePosLastY = 0;


    tool.onmousedown = function (event) {
        tool.onmousedownTemplate(event);
        tool.component = tool.drawingPad.circle(0);

        tool.component.attr({
            cx: tool.originalX,
            cy: tool.originalY,
            fill: '#f60'
        });
        mousePosLastX = tool.originalX;
        mousePosLastY = tool.originalY;
    };

    tool.onmouseup = function (event) {
        tool.onmouseupTemplate(event);
        mousePosLastX = 0;
        mousePosLastY = 0;
    };

    tool.onmousedrag = function (event) {
        if (!tool.mousedown) {
            return;
        }

        var mousePoint = tool.drawingPad.getOffset(event);
        var mouseX = mousePoint.x;
        var mouseY = mousePoint.y;

        var delta = 0;
        var offsetToOriginX = mouseX - tool.originalX;
        var offsetToOriginY = mouseX - tool.originalY;
        var dx = 0;
        var dy = 0;

        if (offsetToOriginX > 0 && offsetToOriginY > 0) {
            dx = mouseX - mousePosLastX;
            dy = mouseY - mousePosLastY;
        } else if (offsetToOriginX > 0 && offsetToOriginY < 0) {
            dx = mouseX - mousePosLastX;
            dy = mousePosLastY - mouseY;
        } else if (offsetToOriginX < 0 && offsetToOriginY < 0) {
            dx = mousePosLastX - mouseX;
            dy = mousePosLastY - mouseY;
        } else {
            dx = mousePosLastX - mouseX;
            dy = mouseY - mousePosLastY;
        }

        delta = ((dx + dy) / 2);
        if (delta > 10) {
            delta = 10;
        } else if (delta < -20) {
            delta = -20;
        }

        var rx = tool.component.attr('rx');
        if (delta < (rx * -1)) {
            delta = rx * -1;
        }

        tool.component.attr({
            rx: tool.component.attr('rx') + delta,
            ry: tool.component.attr('ry') + delta
        });

        mousePosLastX = mouseX;
        mousePosLastY = mouseY;
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
