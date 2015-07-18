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

angular.module('scenarioo.services').factory('DrawingPadService', function ($rootScope, $routeParams, $http, ContextService) {

    var drawingPadNodeId = 'drawingPad';
    var viewPortGroupId = 'viewPortGroup';
    var backgroundImageId = 'sketcher-original-screenshot';

    var DRAWINGPAD_CLICKED_EVENT = 'drawingPadClicked';

    var drawingPad;


    function init() {

        drawingPad = SVG(drawingPadNodeId).size('100%', '100%').spof();

        drawingPad.viewPortGroup = drawingPad.group().attr({
            class: 'svg-pan-zoom_viewport',
            id: viewPortGroupId
        });

        drawingPad.on('mouseup', function (event) {
            if (event.target.id === drawingPadNodeId || event.target.id === viewPortGroupId || event.target.id === backgroundImageId) {
                $rootScope.$broadcast(DRAWINGPAD_CLICKED_EVENT);
            }
        });

        drawingPad.viewPortGroup.getOffset = function(event) {
            var offset = jQuery(drawingPad.parent()).offset();
            var point = {x: 0, y: 0};

            point.x = Math.max(event.pageX - offset.left, 0);
            point.y = Math.max(event.pageY - offset.top, 0);

            return point;
        };

        drawingPad.viewPortGroup.image('')
            .attr({
                id: backgroundImageId,
                draggable: false,
                width: '100%',
                height: '100%'
            })
            .ondragstart = function () {
            return false;
        };

        loadBackgroundImage(drawingPad);

        return drawingPad;
    }

    function exportDrawing() {
        return drawingPad.svg();
    }


    function loadBackgroundImage (dp) {
        var bgImg = SVG.get(backgroundImageId);

        if (bgImg !== undefined && $routeParams.screenshotURL && ContextService.sketchStepIndex == null) {
            convertImgToBase64URL(decodeURIComponent($routeParams.screenshotURL), function(base64Img){
                bgImg.load(base64Img).loaded(function (loader) {
                    dp.attr({
                        width: loader.width,
                        height: loader.height
                    });
                    bgImg.attr({
                        width: loader.width,
                        height: loader.height
                    });
                });
            });

        }
        else if (bgImg !== undefined && $routeParams.screenshotURL && ContextService.sketchStepIndex !== null){
            $http.get(decodeURIComponent($routeParams.screenshotURL), {headers: {accept: 'image/svg+xml'}}).
                success(function(data) {
                    // This should strip out the redundant parts: <svg> tags, <defs>, the viewport group...
                    // However, it breaks import of one of the elements, and doesn't fix anything.
                    // Preserved in case truncation will be important.
                    /*var truncated = data.substring(data.search('<image '), data.search('</g>'));
                     drawingPad.svg(truncated);*/
                    dp.svg(data);
                }).
                error(function(data, status, headers) {
                    console.log(data);
                    console.log(status);
                    console.log(headers);
                });
        }
    }

    function convertImgToBase64URL(url, callback, outputFormat) {
        var img = new Image();
        img.crossOrigin = 'Anonymous';
        img.onload = function () {
            var canvas = document.createElement('CANVAS'),
                ctx = canvas.getContext('2d'), dataURL;
            canvas.height = this.height;
            canvas.width = this.width;
            ctx.drawImage(this, 0, 0);
            dataURL = canvas.toDataURL(outputFormat);
            callback(dataURL);
            canvas = null; //TODO: Does this destroy the canvas element? Does it matter if not?
        };
        img.src = url;
    }

    function unSelectAllShapes(container) {
        if (container) {
            container.each(function () {
                this.unSelect();
            });
        }
    }


    return {
        DRAWINGPAD_CLICKED_EVENT: DRAWINGPAD_CLICKED_EVENT,

        init: function () {
         return init();
         },

        parentNode: function () {
            return document.getElementById(drawingPadNodeId);
        },

        exportDrawing: function () {
            return exportDrawing();
        },

        unSelectAllShapes: function (container) {
            return unSelectAllShapes(container);
        }
    };
});
