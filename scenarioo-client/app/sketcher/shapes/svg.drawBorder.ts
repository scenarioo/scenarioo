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
declare const SVG: any;

SVG.DrawBorder = function(width, height, x, y) {

    this.constructor.call(this, SVG.create('rect'));

    this.size(width, height)
        .move(x, y)
        .fill({color: '#fff', opacity: 0})
        .stroke({color: '#000', width: 1, dasharray: '10,10', opacity: 0.8});
};

SVG.DrawBorder.prototype = new SVG.Shape();

SVG.extend(SVG.Container, {
    drawBorder(width, height, x, y) {
        return this.put(new SVG.DrawBorder(width, height, x, y));
    },
});
