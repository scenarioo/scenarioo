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

/* global svgPanZoom:false */

angular.module('scenarioo.services').service('ZoomPanService', function ($rootScope) {

    var panZoom,
        zoomFactor = 1,
        ZOOM_FACTOR_CHANGED = 'zoom_factor_changed';

    return {
        ZOOM_FACTOR_CHANGED: ZOOM_FACTOR_CHANGED,

        initZoomPan: function (drawingPadId) {
            panZoom = svgPanZoom('#' + drawingPadId, {
                controlIconsEnabled: true
            });
            panZoom.disableDblClickZoom();
            panZoom.setOnZoom(function () {
                zoomFactor = this.getZoom();
                $rootScope.$emit(ZOOM_FACTOR_CHANGED);
            });
            this.resetZoomPan();
        },

        resetZoomPan: function () {
            panZoom.updateBBox();
            panZoom.resize();
            panZoom.fit();
            panZoom.center();
            panZoom.pan({x: panZoom.getPan().x, y: 0});
            panZoom.enableControlIcons();
        },

        getZoomFactor: function () {
            return zoomFactor;
        },

        enableZoomPan: function () {
            panZoom.enablePan();
            panZoom.enableZoom();
        },

        disableZoomPan: function () {
            panZoom.disablePan();
            panZoom.disableZoom();
        },

        // Hack??
        updateZoomPan: function () {
            var z = panZoom.getZoom();
            var p = panZoom.getPan();

            p.x = p.x - 0.000001;
            p.y = p.y - 0.000001;

            panZoom.zoom(z - 0.000001);
            panZoom.pan(p);
        },

        getPanPosition: function () {
            return panZoom.getPan();
        },

        convertToZoomedPoint: function (n) {
            return n / zoomFactor;
        }
    };
});
