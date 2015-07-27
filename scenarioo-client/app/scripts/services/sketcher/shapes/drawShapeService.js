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
/* global SVG:false */


angular.module('scenarioo.services').service('DrawShapeService', function ($rootScope, ZoomPanService) {

    var SHAPE_SELECTED_EVENT = 'shapeSelected';

    return {

        SHAPE_SELECTED_EVENT: SHAPE_SELECTED_EVENT,


        registerShapeEvents: function (shape, isEditable) {

            shape.addClass('shape');

            shape.on('mouseup.shape', function () {
                $rootScope.$broadcast(SHAPE_SELECTED_EVENT, this);
            }, false);

            shape.on('dragend', function () {
                ZoomPanService.updateZoomPan();
            });

            shape.on('selected', function () {
                ZoomPanService.disableZoomPan();
            });
            shape.on('unselected', function () {
                ZoomPanService.enableZoomPan();
                ZoomPanService.updateZoomPan();
            });

            if (isEditable) {
                shape.on('dblclick', function () {
                    this.edit(ZoomPanService.getZoomFactor(), ZoomPanService.getPanPosition());
                });
            }
        },

        createNewShapeByClassName: function (drawingPad, shape) {
            var classes = shape.attr('class');
            var typeClass = classes.split(' ').filter(function (value) {
                return value.indexOf('-shape') > -1;
            });

            var newShape;

            switch (typeClass[0]) {
                case 'rect-shape':
                    newShape = drawingPad.rectShape(shape.width(), shape.height(), shape.x(), shape.y());
                    break;
                case 'border-shape':
                    newShape = drawingPad.borderShape(shape.width(), shape.height(), shape.x(), shape.y());
                    break;
                case 'note-shape':
                    newShape = drawingPad.noteShape(shape.width(), shape.height(), shape.x(), shape.y());
                    break;
                case 'text-shape':
                    newShape = drawingPad.textShape(shape.width(), shape.height(), shape.x(), shape.y());
                    break;
                case 'button-shape':
                    newShape = drawingPad.buttonShape(shape.width(), shape.height(), shape.x(), shape.y());
                    break;
            }

            shape.each(function () {
                if (this instanceof SVG.Text) {
                    newShape.setText(this.node.textContent);
                }
            });

            return newShape;

        }
    };
});
