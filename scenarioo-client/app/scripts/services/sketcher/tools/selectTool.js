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


angular.module('scenarioo.controllers').factory('SelectTool', function(Tool) {
    var tool = Tool.get;

    tool.name = 'Select Tool';
    tool.tooltip = 'This tool is used to select elements of the drawing.';


    tool.onmouseup = function(event) {
        if(event.target.id !== tool.drawingPad.node.id){
            var element = SVG.get(event.target.id);
            console.log(event.target.id);
            element.fill('#f06');
        }


    };

    tool.onmousedown = function(event) {};
    tool.onmousedrag = function(event) {};

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
