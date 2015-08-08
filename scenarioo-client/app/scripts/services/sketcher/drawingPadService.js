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
/* eslint no-console:0*/

angular.module('scenarioo.services').service('DrawingPadService', function ($rootScope, $routeParams, $http, ContextService, DrawShapeService, ZoomPanService, $location) {

    var drawingPadNodeId = 'drawingPad',
        viewPortGroupId = 'viewPortGroup',
        backgroundImageId = 'sketcher-original-screenshot',
        drawingPad,
        selectedShape,
        isSetup = false,
        DRAWINGPAD_CLICKED_EVENT = 'drawingPadClicked';


    function setup() {

        if (drawingPad && !isSetup) {
            // tiled background
            var bg = drawingPad.rect().attr({
                width: drawingPad.width(),
                height: drawingPad.height(),
                x: drawingPad.x(),
                y: drawingPad.y(),
                fill: drawingPad.pattern(10, 10, function (add) {
                    add.rect(10, 10).fill('#ddd');
                    add.rect(5, 5).fill('#fff');
                    add.rect(5, 5).move(5, 5).fill('#fff');
                })
            });

            /*
             * to get the select border working in a zoomed svg
             * we need an additional svg element because the
             * select border attaches to the parent svg element
             */
            drawingPad.viewPortGroup = drawingPad.nested().attr({
                id: viewPortGroupId
            });

            /*
             * in order to get svgPanZoom to work we need a group
             * with all the elements that should be zoomed and panned
             */
            drawingPad.group = drawingPad.group().attr({
                class: 'svg-pan-zoom_viewport'
            });

            drawingPad.group.add(drawingPad.viewPortGroup);

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

            drawingPad.viewPortGroup.getOffset = function (event) {
                var offset = $(drawingPad.viewPortGroup.node).offset();
                var point = {x: 0, y: 0};

                point.x = Math.max(event.pageX - offset.left, 0);
                point.y = Math.max(event.pageY - offset.top, 0);

                return point;
            };

            loadBackgroundImage();

            drawingPad.on('mouseup', function (event) {
                if (event.target.id === drawingPadNodeId || event.target.id === viewPortGroupId || event.target.id === backgroundImageId || event.target.id === bg.id()) {
                    $rootScope.$broadcast(DRAWINGPAD_CLICKED_EVENT);
                }
            });

            ZoomPanService.initZoomPan(drawingPad.id());

            isSetup = true;
        }
    }

    function loadBackgroundImage() {
        var bgImg = SVG.get(backgroundImageId);

        var screenshotURL = $location.search().url;
        var mode = $location.search().mode;

        if (bgImg && screenshotURL && mode === 'create') {

                convertImgToBase64URL(decodeURIComponent(screenshotURL), function (base64Img) {
                    bgImg.load(base64Img).loaded(function (loader) {
                        bgImg.attr({
                            width: loader.width,
                            height: loader.height
                        });

                        ZoomPanService.resetZoomPan();
                    });
                });

        }
        else if (bgImg && screenshotURL && mode === 'edit') {
            $http.get(decodeURIComponent(screenshotURL), {headers: {accept: 'image/svg+xml'}}).
                success(function (data) {
                    var dp = bgImg.doc(SVG.Doc);
                    var tempContainer = dp.nested();
                    tempContainer.svg(data);
                    var tempSVG = tempContainer.first();
                    tempSVG.each(function() {
                        var newShape;

                        if(this.hasClass('shape')) {
                            newShape = DrawShapeService.createNewShapeByClassName(drawingPad.viewPortGroup, this);
                            DrawShapeService.registerShapeEvents(newShape, newShape instanceof SVG.Nested);
                        } else {
                            newShape = this;
                        }
                        drawingPad.viewPortGroup.add(newShape);
                    });
                    tempContainer.remove();
                    bgImg.remove();
                    dp.viewPortGroup.first().id(backgroundImageId);

                    ZoomPanService.resetZoomPan();
                }).
                error(function (data, status, headers) {
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

    window.onkeydown = onKeyDownHandler;
    function onKeyDownHandler(event) {
        switch (event.keyCode) {
            case 46: // delete
                removeSelectedShape();
                return;
        }
    }

    function removeSelectedShape() {
        if (selectedShape) {
            selectedShape.unSelect();
            selectedShape.remove();
            selectedShape = null;
        }
    }


    return {
        DRAWINGPAD_CLICKED_EVENT: DRAWINGPAD_CLICKED_EVENT,


        setDrawingPad: function (dp) {
            if (dp) {
                drawingPad = dp;
                setup();
            }
        },

        getDrawingPad: function () {
            setup();
            return drawingPad;
        },

        getParentNode: function () {
            return document.getElementById(drawingPadNodeId);
        },

        getParentNodeId: function () {
            return drawingPadNodeId;
        },

        exportDrawing: function () {
            var group = drawingPad.group.clone().hide();
            var svg = group.first();
            svg.attr({
                xmlns: 'http://www.w3.org/2000/svg',
                version: '1.1',
                'xmlns:xlink': 'http://www.w3.org/1999/xlink',
                width: svg.first().width(),
                height: svg.first().height()
            });
            svg.first().attr({
                'xmlns:xlink': 'http://www.w3.org/1999/xlink'
            });
            var exportedSVG = svg.svg();
            exportedSVG = exportedSVG.replace('NS1:href', 'xlink:href');
            return exportedSVG;
        },

        unSelectAllShapes: function () {
            if (drawingPad.viewPortGroup) {
                drawingPad.viewPortGroup.each(function () {
                    if (this.hasClass('shape')) {
                        if (this instanceof SVG.Nested) {
                            this.view();
                        }
                        this.unSelect();
                    }
                });
                selectedShape = null;
            }
        },

        setSelectedShape: function (shape) {
            selectedShape = shape;
        },

        getSelectedShape: function () {
            return selectedShape;
        },

        sendSelectedShapeToBack: function () {
            if (selectedShape) {
                selectedShape.back();
                selectedShape.forward();
            }
        },

        sendSelectedShapeToFront: function () {
            if (selectedShape) {
                selectedShape.front();
                selectedShape.backward();
            }
        },

        sendSelectedShapeBackward: function () {
            /*
             * the first element is the background image. so backward can only be called if the
             * selected shape is at least at 3rd position in the stack
             */
            if (selectedShape && drawingPad.viewPortGroup.get(1) !== selectedShape) {
                selectedShape.backward();
            }
        },

        sendSelectedShapeForward: function () {
            var indexLast = drawingPad.viewPortGroup.index(drawingPad.viewPortGroup.last());

            /*
             * the last element is the select border. so forward can only be called if the
             * selected shape is at least at 3rd last position in the stack
             */

            if (selectedShape && drawingPad.viewPortGroup.get(indexLast - 1) !== selectedShape) {
                selectedShape.forward();
            }
        },

        destroy: function () {
            drawingPad.clear();
            drawingPad = null;
            isSetup = false;
        }

    };
});
