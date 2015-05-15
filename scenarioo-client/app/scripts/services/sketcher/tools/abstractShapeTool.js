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

angular.module('scenarioo.controllers').factory('AbstractShapeTool', function (Tool) {
    var tool = Tool.get;

    tool.cursor = 'crosshair';

    tool.component = null;
    tool.originalX = 0;
    tool.originalY = 0;
    tool.mousedown = false;


    tool.onmousedownTemplate = function (event) {
        tool.mousedown = true;
        tool.originalX = tool.drawingPad.getOffsetX(event.x);
        tool.originalY = tool.drawingPad.getOffsetY(event.y);
    };

    tool.onmouseupTemplate = function (event) {
        tool.mousedown = false;
        tool.originalX = 0;
        tool.originalY = 0;
        tool.component.attr('fill', '#0f3');
    };

    tool.onmousedragTemplate = function (event) {
        tool.mouseX = tool.drawingPad.getOffsetX(event.x);
        tool.mouseY = tool.drawingPad.getOffsetY(event.y);

        tool.anchorX = Math.min(tool.originalX, tool.mouseX);
        tool.anchorY = Math.min(tool.originalY, tool.mouseY);

        tool.cornerX = Math.max(tool.originalX, tool.mouseX);
        tool.cornerY = Math.max(tool.originalY, tool.mouseY);
    };

    return {

        get: tool,
        name: tool.name,
        icon: tool.icon,
        tooltip: tool.tooltip,
        cursor: tool.cursor,

        onmouseup: tool.onmouseup,
        onmousedown: tool.onmousedown,
        onmousedrag: tool.onmousedrag

    };

});
