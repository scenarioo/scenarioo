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

/* eslint no-console:0 */


angular.module('scenarioo.services').factory('Tool', function (DrawingPadService) {

    return function () {

        return {

            // Every tool needs to set those fields to an adequate value
            id: 'HTML id required',
            icon: 'default',
            tooltip: 'Tooltip text required',
            cursor: 'default',

            // Button is disabled when tool is already active
            buttonDisabled: false,


            DRAWING_ENDED_EVENT: 'drawingEnded',


            getDrawingPad: function () {
                if (DrawingPadService.getDrawingPad().drawingContainer) {
                    return DrawingPadService.getDrawingPad().drawingContainer;
                }
            },

            activate: function () {
                this.buttonDisabled = true;
                var dp = this.getDrawingPad();
                // Instead of registering the three events, why not just set the tool on the drawing pad?
                if (dp) {
                    dp.on('mousedown.drawingpad', this.onmousedown);
                    dp.on('mouseup.drawingpad', this.onmouseup);
                    dp.on('mousemove.drawingpad', this.onmousedrag);
                }

                $('#drawingPad').css('cursor', this.cursor);
            },

            deactivate: function () {
                this.buttonDisabled = false;

                var dp = this.getDrawingPad();
                if (dp) {
                    dp.off('mousedown.drawingpad', this.onmousedown);
                    dp.off('mouseup.drawingpad', this.onmouseup);
                    dp.off('mousemove.drawingpad', this.onmousedrag);
                }

                $('#drawingPad').css('cursor', 'default');
            },

            onmousedown: function () {
            },
            onmouseup: function () {
            },
            onmousedrag: function () {
            }
        };
    };
});
