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

angular.module('scenarioo.services').factory('DrawingPadService', function () {
    var drawingPad = SVG('drawingPad').size('100%', '100%').fixSubPixelOffset();

    function exportDrawing() {
        return drawingPad.exportSvg();
    }

    drawingPad.getOffset = function(event) {
        var offset = jQuery(drawingPad.parent).offset();
        var point = { x:0, y:0 };

        point.x = Math.max(event.pageX - offset.left, 0);
        point.y = Math.max(event.pageY - offset.top, 0);

        return point;
    };


    return {
        get: drawingPad,

        exportDrawing: function () {
            return exportDrawing();
        }
    };
});
