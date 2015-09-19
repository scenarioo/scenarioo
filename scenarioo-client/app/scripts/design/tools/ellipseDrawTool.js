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


angular.module('scenarioo.controllers').factory('EllipseDrawTool', function (DrawTool) {

    var tool = DrawTool();

    tool.name = 'Ellipse Tool';
    tool.icon = null;
    tool.tooltip = 'This tool is used to draw ellipsis.';


    tool.onmousedown = function (event) {
        tool.onmousedownTemplate(event);

        tool.shape.attr({
            cx: tool.originalX,
            cy: tool.originalY,
            fill: '#e74c3c'
        });
    };

    tool.onmouseup = function (event) {
        tool.onmouseupTemplate(event);
    };

    tool.onmousedrag = function (event) {
        if (!tool.mousedown) {
            return;
        }
        tool.onmousedragTemplate(event);

        tool.shape.attr({
            rx: (tool.cornerX - tool.anchorX) / 2,
            ry: (tool.cornerY - tool.anchorY) / 2,
            cx: tool.anchorX + tool.shape.attr('rx'),
            cy: tool.anchorY + tool.shape.attr('ry')
        });
    };

    tool.getShape = function () {
        return tool.getDrawingPad().ellipse(0, 0, 0, 0);
    };

    return tool;

});
