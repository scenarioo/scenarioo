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


angular.module('scenarioo.controllers').factory('Tool', function (DrawingPadService) {

    var tool = {};

    tool.name = 'Tool Name';
    tool.icon = null;
    tool.tooltip = '';
    tool.cursor = 'default';
    tool.buttonDisabled = false;
    tool.drawingPad = DrawingPadService.get;


    tool.activate = function (newTool) {
        console.log('Activated tool: ' + newTool.name);
        newTool.buttonDisabled = true;

        tool.drawingPad.on('mousedown', newTool.onmousedown);
        tool.drawingPad.on('mouseup', newTool.onmouseup);
        tool.drawingPad.on('mousemove', newTool.onmousedrag);
    };

    tool.deactivate = function (currentTool) {
        console.log('Deactivated tool: ' + currentTool.name);
        currentTool.buttonDisabled = false;

        tool.drawingPad.off('mousedown', currentTool.onmousedown);
        tool.drawingPad.off('mouseup', currentTool.onmouseup);
        tool.drawingPad.off('mousemove', currentTool.onmousedrag);
    };

    tool.isButtonDisabled = function (someTool) {
        return someTool.buttonDisabled;
    };


    tool.onmousedown = function (event) {
        console.log('onmousedown: not implemented in generic tool');
    };
    tool.onmouseup = function (event) {
        console.log('onmouseup: not implemented in generic tool');
    };
    tool.onmousedrag = function (event) {
        console.log('onmousedrag: not implemented in generic tool');
    };


    return {
        get: tool,
        name: tool.name,
        icon: tool.icon,
        tooltip: tool.tooltip,
        cursor: tool.cursor,
        isButtonDisabled: tool.isButtonDisabled,

        activate: tool.activate,
        deactivate: tool.deactivate,

        onmouseup: tool.onmouseup,
        onmousedown: tool.onmousedown,
        onmousedrag: tool.onmousedrag
    };

});
