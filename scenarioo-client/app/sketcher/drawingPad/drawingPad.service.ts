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
import * as $ from 'jquery';
declare var angular: angular.IAngularStatic;

declare const SVG: any;

angular.module('scenarioo.services')
    .service('DrawingPadService', ($rootScope, $routeParams, $http, SketcherContextService, DrawShapeService, ZoomPanService, $location, $log) => {

        const drawingPadNodeId = 'drawingPad';
        const drawingContainerId = 'drawingContainer';
        const backgroundImageId = 'sketcher-original-screenshot';
        let drawingPad;
        let selectedShape;
        let isSetup = false;
        const DRAWINGPAD_CLICKED_EVENT = 'drawingPadClicked';
        let svgUrl;

        function setup() {

            if (drawingPad && !isSetup) {
                // tiled background
                const bg = drawingPad.rect().attr({
                    width: drawingPad.width(),
                    height: drawingPad.height(),
                    x: drawingPad.x(),
                    y: drawingPad.y(),
                    fill: drawingPad.pattern(10, 10, (add) => {
                        add.rect(10, 10).fill('#ddd');
                        add.rect(5, 5).fill('#fff');
                        add.rect(5, 5).move(5, 5).fill('#fff');
                    }),
                });

                /*
                 * to get the select border working in a zoomed svg
                 * we need an additional svg element because the
                 * select border attaches to the parent svg element
                 */
                drawingPad.drawingContainer = drawingPad.nested().attr({
                    id: drawingContainerId,
                });

                /*
                 * in order to get svgPanZoom to work we need a group
                 * with all the elements that should be zoomed and panned
                 */
                drawingPad.zoomPanGroup = drawingPad.group().attr({
                    class: 'svg-pan-zoom_viewport',
                });

                drawingPad.zoomPanGroup.add(drawingPad.drawingContainer);

                drawingPad.drawingContainer.image('')
                    .attr({
                        id: backgroundImageId,
                        draggable: false,
                        width: '100%',
                        height: '100%',
                    })
                    .ondragstart = () => false;

                drawingPad.drawingContainer.getOffset = (event) => {
                    const offset = $(drawingPad.drawingContainer.node).offset();
                    const point = {x: 0, y: 0};

                    point.x = Math.max(event.pageX - offset.left, 0);
                    point.y = Math.max(event.pageY - offset.top, 0);

                    return point;
                };

                loadBackgroundImageOrExistingSketch();

                drawingPad.on('mouseup', (event) => {
                    if (event.target.id === drawingPadNodeId || event.target.id === drawingContainerId || event.target.id === backgroundImageId || event.target.id === bg.id()) {
                        $rootScope.$broadcast(DRAWINGPAD_CLICKED_EVENT);
                    }
                });

                ZoomPanService.initZoomPan(drawingPad.id());

                isSetup = true;
            }
        }

        function loadBackgroundImageOrExistingSketch() {
            const backgroundImageHtmlElement = SVG.get(backgroundImageId);

            if (!backgroundImageHtmlElement) {
                $log.error('can\'t get background image node');
                return;
            }

            const mode = $location.search().mode;

            if (mode === 'create') {
                loadBackgroundImageForNewSketch(backgroundImageHtmlElement);
            } else if (mode === 'edit') {
                loadExistingSketch(backgroundImageHtmlElement);
            }
        }

        function loadBackgroundImageForNewSketch(backgroundImageHtmlElement) {
            const screenshotURL = SketcherContextService.screenshotURL;

            if (!screenshotURL) {
                $log.warn('screenshot url not set in context service');
                return;
            }

            convertImgToBase64URL(decodeURIComponent(screenshotURL), (base64Img) => {
                backgroundImageHtmlElement.load(base64Img).loaded((loader) => {
                    backgroundImageHtmlElement.attr({
                        width: loader.width,
                        height: loader.height,
                    });
                    ZoomPanService.resetZoomPan();
                });
            });
        }

        function loadExistingSketch(backgroundImageHtmlElement) {
            if (!svgUrl) {
                $log.log('svgUrl is not set');
                return;
            }

            $http.get(decodeURIComponent(svgUrl), {headers: {accept: 'image/svg+xml'}}).success((data) => {
                importExistingSketch(backgroundImageHtmlElement, data);
                ZoomPanService.resetZoomPan();
            }).error((data, status, headers) => {
                $log.error(data, status, headers);
            });
        }

        function importExistingSketch(backgroundImageHtmlElement, svgData) {
            const dp = backgroundImageHtmlElement.doc(SVG.Doc); // Gets the SVG image root (svg.js)
            const tempContainer = dp.nested(); // Creates a nested SVG document inside the parent SVG document (svg.js)
            tempContainer.svg(svgData); // Import the loaded SVG file into the nested SVG document (svg.js)
            const tempSVG = tempContainer.first(); // Get the first SVG element which is the root node of the imported document (svg.js)

            tempSVG.each(function() {
                let newShape;

                if (this.hasClass('shape')) {
                    newShape = DrawShapeService.createNewShapeByClassName(drawingPad.drawingContainer, this);
                    DrawShapeService.registerShapeEvents(newShape, newShape instanceof SVG.Nested);
                } else if (this.type === 'image') {
                    newShape = this; // This is the background image node
                }
                drawingPad.drawingContainer.add(newShape);
            });
            tempContainer.remove();
            backgroundImageHtmlElement.remove();
            dp.drawingContainer.first().id(backgroundImageId);
        }

        function convertImgToBase64URL(url, callback) {
            const img = new Image();
            img.crossOrigin = 'Anonymous';
            img.onload = function() {
                const canvas: any = document.createElement('CANVAS');
                const ctx = canvas.getContext('2d');
                let dataURL;
                // TODO typescript says these properties do not exist
                canvas.height = (this as any).height;
                canvas.width = (this as any).width;
                ctx.drawImage(this, 0, 0);
                dataURL = canvas.toDataURL();
                callback(dataURL);
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
            if (selectedShape && selectedShape.isSelected) {
                selectedShape.unSelect();
                selectedShape.remove();
                selectedShape = null;
                $rootScope.$broadcast('edit_drawing_event');
            }
        }

        return {
            DRAWINGPAD_CLICKED_EVENT,

            setSvgUrl(newSvgUrl) {
                svgUrl = newSvgUrl;
            },

            setDrawingPad(dp) {
                drawingPad = dp;
                setup();
            },

            getDrawingPad() {
                setup();
                return drawingPad;
            },

            getParentNode() {
                return document.getElementById(drawingPadNodeId);
            },

            getParentNodeId() {
                return drawingPadNodeId;
            },

            // For saving the SVG image
            exportDrawing() {
                const zoomPanGroup = drawingPad.zoomPanGroup.clone().hide();
                const svg = zoomPanGroup.first();
                svg.attr({
                    'encoding': 'utf-8',
                    'xmlns': 'http://www.w3.org/2000/svg',
                    'version': '1.1',
                    'xmlns:xlink': 'http://www.w3.org/1999/xlink',
                    'width': svg.first().width(),
                    'height': svg.first().height(),
                });
                return svg.svg(); // return the exported XML SVG string
            },

            unSelectAllShapes() {
                if (drawingPad.drawingContainer) {
                    drawingPad.drawingContainer.each(function() {
                        if (this.hasClass('shape')) {
                            if (this instanceof SVG.CompositeShape) {
                                this.view();
                            }
                            this.unSelect();
                        }
                    });
                    selectedShape = null;
                }
            },

            setSelectedShape(shape) {
                selectedShape = shape;
            },

            getSelectedShape() {
                return selectedShape;
            },

            sendSelectedShapeToBack() {
                if (selectedShape) {
                    selectedShape.back();
                    selectedShape.forward();
                    $rootScope.$broadcast('edit_drawing_event');
                }
            },

            sendSelectedShapeToFront() {
                if (selectedShape) {
                    selectedShape.front();
                    selectedShape.backward();
                    $rootScope.$broadcast('edit_drawing_event');
                }
            },

            sendSelectedShapeBackward() {
                /*
                 * the first element is the background image. so backward can only be called if the
                 * selected shape is at least at 3rd position in the stack
                 */
                if (selectedShape && drawingPad.drawingContainer.get(1) !== selectedShape) {
                    selectedShape.backward();
                    $rootScope.$broadcast('edit_drawing_event');
                }
            },

            sendSelectedShapeForward() {
                const indexLast = drawingPad.drawingContainer.index(drawingPad.drawingContainer.last());

                /*
                 * the last element is the select border. so forward can only be called if the
                 * selected shape is at least at 3rd last position in the stack
                 */

                if (selectedShape && drawingPad.drawingContainer.get(indexLast - 1) !== selectedShape) {
                    selectedShape.forward();
                    $rootScope.$broadcast('edit_drawing_event');
                }
            },

            destroy() {
                drawingPad.clear();
                drawingPad = null;
                isSetup = false;
            },

        };
    });
