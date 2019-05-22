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

/* global SVG:false */

import * as angular from 'angular';
declare const SVG: any;

angular.module('scenarioo.services').service('DrawShapeService', ($rootScope, ZoomPanService) => {

    const SHAPE_SELECTED_EVENT = 'shapeSelected';

    return {
        SHAPE_SELECTED_EVENT,

        registerShapeEvents(shape, isEditable) {

            shape.addClass('shape');

            shape.on('mouseup.shape', function() {
                $rootScope.$broadcast(SHAPE_SELECTED_EVENT, this);
            }, false);

            shape.on('dragend', () => {
                ZoomPanService.updateZoomPan();
                $rootScope.$broadcast('edit_drawing_event');
            });

            shape.on('selected', () => {
                ZoomPanService.disableZoomPan();
            });
            shape.on('unselected', () => {
                ZoomPanService.enableZoomPan();
                ZoomPanService.updateZoomPan();
            });

            if (isEditable) {
                shape.on('dblclick', function() {
                    this.edit(ZoomPanService.getZoomFactor(), ZoomPanService.getPanPosition());
                    $rootScope.$broadcast('edit_drawing_event');
                });
            }
        },

        createNewShapeByClassName(drawingPad, shape) {
            const self = this;
            const classes = shape.attr('class');
            const typeClass = classes.split(' ').filter((value) => value.indexOf('-shape') > -1);

            let newShape;

            switch (typeClass[0]) {
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
                case 'highlight-shape':
                    newShape = drawingPad.highlightShape(shape.width(), shape.height(), shape.x(), shape.y());
                    break;
                case 'line-shape':
                    newShape = drawingPad.lineShape(shape.attr('x1'), shape.attr('y1'), shape.attr('x2'), shape.attr('y2'));
                    break;
                default:
                    newShape = drawingPad.rectShape(shape.width(), shape.height(), shape.x(), shape.y());
            }

            if (shape.type !== 'line') {
                shape.each(function() {
                    if (this instanceof SVG.Text) {
                        newShape.setText(self.getSVGText(this));
                    }
                });
            }

            return newShape;
        },

        getSVGText(SVGText) {
            let text = '';

            let i = 0;
            const total = SVGText.lines().length();

            SVGText.lines().each(function() {
                i++;
                if (this.node.textContent.length > 0) {
                    text += this.node.textContent;

                    if (i < total) {
                        text += '\n';
                    }
                }
            });

            return text;
        },
    };
});
