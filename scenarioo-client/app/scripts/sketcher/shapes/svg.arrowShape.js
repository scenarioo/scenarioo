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
/* eslint no-extra-semi:0*/

;
(function () {
    SVG.ArrowShape = function (width, height, x, y, options) {
        var i;

        this.settings = {
            fill: '#e74c3c'
            , stroke: '#e74c3c'
            , strokeWidth: 3
            , padding: 20
            , origin: {x: 0, y: 0 }
            , lineStartPoint: {x: 0, y: 0 }
            , lineEndPoint: {x: 0, y: 0 }
        };

        options = options || {};
        for (i in options)
            this.settings[i] = options[i];

        this.constructor.call(this, SVG.create('svg'));

        this.width(width);
        this.height(height);

        this.boundingline = this.line(0, 0, 0, 0);
        this.boundingline.stroke({ opacity: 0, width: this.settings.padding, color: '#f00'});

        this.line = this.line(0, 0, 0, 0);
        this.line.stroke({ color: this.settings.stroke, width: this.settings.strokeWidth });

        this.arrowhead = this.polygon(0, 0, 0, 0);
        this.arrowhead.fill({ color: this.settings.fill })
            .stroke({ color: this.settings.stroke, width: this.settings.strokeWidth });
    };

    SVG.ArrowShape.prototype = new SVG.Nested();


    // Add methods
    SVG.extend(SVG.ArrowShape, {

        updateChildren: function () {
            // quadrants 1 & 4
            if(this.settings.origin.x <= this.x()) {
                this.settings.lineStartPoint.x = 0;
                this.settings.lineEndPoint.x = this.width();
            } else {
                this.settings.lineStartPoint.x = this.width();
                this.settings.lineEndPoint.x = 0;
            }

            // quadrants 3 & 4
            if(this.settings.origin.y <= this.y()) {
                this.settings.lineStartPoint.y = 0;
                this.settings.lineEndPoint.y = this.height();
            } else {
                this.settings.lineStartPoint.y = this.height();
                this.settings.lineEndPoint.y = 0;
            }

            this.boundingline.plot([[this.settings.lineStartPoint.x, this.settings.lineStartPoint.y],
                [this.settings.lineEndPoint.x, this.settings.lineEndPoint.y]]);

            this.line.plot([[this.settings.lineStartPoint.x, this.settings.lineStartPoint.y],
                [this.settings.lineEndPoint.x, this.settings.lineEndPoint.y]]);
        },

        setOrigin: function (x, y) {
           this.settings.origin = { x: x, y: y };
        },

        getLineAngle: function (startPoint, endPoint) {
            var normEndPoint = { x: endPoint.x - startPoint.x, y: endPoint.y - startPoint.y };
            var adjacent = Math.abs(normEndPoint.y);
            var hypotenuse = Math.sqrt(Math.pow(normEndPoint.x, 2) + Math.pow(normEndPoint.y, 2));

            var angle = Math.acos(adjacent / hypotenuse) * 180 / Math.PI;

            if(angle) {
                //quadrant 1
                if (startPoint.x < endPoint.x && startPoint.y > endPoint.y) {
                    angle = 180 + angle;
                }
                //quadrant 2
                else if (startPoint.x > endPoint.x && startPoint.y > endPoint.y) {
                    angle = 180 - angle;
                }
                //quadrant 3
                else if (startPoint.x > endPoint.x && startPoint.y < endPoint.y) {
                    angle = angle;
                }
                //quadrant 4
                else {
                    angle = -angle;
                }
            }

            return angle;
        },

        drawArrowHead: function () {
            this.arrowhead.plot([[0, 0],
                [5, -8.66],
                [-5, -8.66],
                [0, 0]]);

            this.arrowhead.center(this.settings.lineEndPoint.x, this.settings.lineEndPoint.y);

            var angle = this.getLineAngle(this.settings.lineStartPoint, this.settings.lineEndPoint);

            if(angle) {
                this.arrowhead.rotate(angle);
            }
        },

        drawStart: function (originalX, originalY) {
            this.attr({
                x: originalX,
                y: originalY
            });

            this.setOrigin(originalX, originalY);
        },

        drawEnd: function () {
            // enlarge svg container in order to show arrow
            this.width(this.width() + 2 * this.settings.padding);
            this.height(this.height() + 2 * this.settings.padding);
            this.move(this.x() - this.settings.padding, this.y() - this.settings.padding);

            this.line.move(this.settings.padding, this.settings.padding);
            this.boundingline.move(this.settings.padding, this.settings.padding);

            this.settings.lineStartPoint = { x: this.settings.lineStartPoint.x + this.settings.padding,
                y: this.settings.lineStartPoint.y + this.settings.padding };
            this.settings.lineEndPoint = { x: this.settings.lineEndPoint.x + this.settings.padding,
                y: this.settings.lineEndPoint.y + this.settings.padding };

            this.setOrigin(this.settings.lineStartPoint.x, this.settings.lineStartPoint.y);

            this.drawArrowHead();
        },

        drawing: function (width, height, x, y) {
            this.attr({
                width: width,
                height: height,
                x: x,
                y: y
            });

            this.updateChildren();
        }
    });

    // Extend SVG container
    SVG.extend(SVG.Container, {
        arrowShape: function (width, height, x, y) {
            return this.put(new SVG.ArrowShape(width, height, x, y));
        }

    });
}).call(this);

