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
/* global SVG:false, jQuery:false*/
/* eslint no-console:0*/

angular.module('scenarioo.services').factory('DrawingPadService', function ($rootScope) {

    var drawingPadNodeId = 'drawingPad';
    var viewPortGroupId = 'viewPortGroup';
    var backgroundImageId = 'sketcher-original-screenshot';
    var drawingPad = SVG(drawingPadNodeId).size('100%', '100%').spof();
    var viewPortGroup = drawingPad.group().attr({
        class: 'svg-pan-zoom_viewport',
        id: viewPortGroupId
    });

    var DRAWINGPAD_CLICKED_EVENT = 'drawingPadClicked';


    function exportDrawing() {
        return drawingPad.svg();
    }

    viewPortGroup.getOffset = function(event) {
        //console.log(drawingPad.parent());
        var offset = jQuery(drawingPad.parent()).offset();
        var point = { x: 0, y: 0 };

        point.x = Math.max(event.pageX - offset.left, 0);
        point.y = Math.max(event.pageY - offset.top, 0);

        return point;
    };

    drawingPad.on('mouseup', function(event) {
        if(event.target.id === drawingPadNodeId || event.target.id === viewPortGroupId || event.target.id === backgroundImageId) {
            $rootScope.$broadcast(DRAWINGPAD_CLICKED_EVENT);
        }
    });


    return {
        root: drawingPad,
        get: viewPortGroup,
        drawingPadNodeId: drawingPadNodeId,
        backgroundImageId: backgroundImageId,

        DRAWINGPAD_CLICKED_EVENT: DRAWINGPAD_CLICKED_EVENT,

        exportDrawing: function () {
            return exportDrawing();
        },

        unSelectAllShapes: function(container) {
            if(container) {
                container.each(function() {
                    this.unSelect();
                });
            }
        }
    };
});
