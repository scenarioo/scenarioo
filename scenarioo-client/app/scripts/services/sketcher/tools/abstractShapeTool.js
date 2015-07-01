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


angular.module('scenarioo.controllers').factory('AbstractShapeTool', function ($rootScope, Tool) {
    var tool = Tool.get;

    tool.cursor = 'crosshair';

    tool.shape = null;
    tool.originalX = 0;
    tool.originalY = 0;
    tool.mousedown = false;


    tool.onmousedownTemplate = function (event) {
        tool.mousedown = true;

        var mousePoint = tool.drawingPad.getOffset(event);
        tool.originalX = mousePoint.x;
        tool.originalY = mousePoint.y;

        tool.pauseEvent(event);
    };

    tool.onmouseupTemplate = function () {
        tool.mousedown = false;
        tool.originalX = 0;
        tool.originalY = 0;
        //tool.shape.attr('fill', '#0f3');


        if(tool.shape != null) {
            tool.shape.on('mouseup', function () {
                $rootScope.$broadcast(tool.SHAPE_SELECTED_EVENT, this);
            }, false);
        }

        $rootScope.$broadcast(tool.DRAWING_ENDED_EVENT, tool.shape);
        tool.pauseEvent(event);
    };

    tool.onmousedragTemplate = function (event) {
        var mousePoint = tool.drawingPad.getOffset(event);
        tool.mouseX = mousePoint.x;
        tool.mouseY = mousePoint.y;

        tool.anchorX = Math.min(tool.originalX, tool.mouseX);
        tool.anchorY = Math.min(tool.originalY, tool.mouseY);

        tool.cornerX = Math.max(tool.originalX, tool.mouseX);
        tool.cornerY = Math.max(tool.originalY, tool.mouseY);

        tool.pauseEvent(event);
    };


    /*
     * Prevents elements/text to be selected on mouse drag
     * http://stackoverflow.com/questions/5429827/how-can-i-prevent-text-element-selection-with-cursor-drag
     */
    tool.pauseEvent = function (event) {
        if (event.stopPropagation) {
            event.stopPropagation();
        }
        if (event.preventDefault) {
            event.preventDefault();
        }
        event.cancelBubble = true;
        event.returnValue = false;
        return false;
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
