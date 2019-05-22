/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('scenarioo.controllers').factory('LineDrawTool', function (DrawTool) {

    var tool = DrawTool();

    tool.id = 'sc-sketcher-tool-line';
    tool.icon = 'line';
    tool.tooltip = 'Draw a line';

    tool.startpoint = null;
    tool.endpoint = null;


    tool.onmousedown = function (event) {
        tool.onmousedownTemplate(event);

        tool.startpoint = tool.endpoint = [tool.originalX, tool.originalY];

        tool.update();
    };

    tool.onmouseup = function (event) {
        tool.onmouseupTemplate(event);
    };

    tool.onmousedrag = function (event) {
        if (!tool.mousedown) {
            return;
        }
        tool.onmousedragTemplate(event);

        tool.endpoint = [tool.mouseX, tool.mouseY];
        tool.update();
    };

    tool.update = function() {
        tool.shape.plot([tool.startpoint, tool.endpoint]);
    };

    tool.getShape = function () {
        return tool.getDrawingPad().lineShape(0, 0, 0, 0);
    };

    return tool;

});
