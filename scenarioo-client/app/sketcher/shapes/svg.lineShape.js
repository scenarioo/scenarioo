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

SVG.LineShape = function (x1, y1, x2, y2) {
    this.constructor.call(this, SVG.create('line'));

    this.plot(x1, y1, x2, y2)
        .stroke({ color: '#e74c3c', width: 3 })
        .addClass('line-shape');
};

SVG.LineShape.prototype = new SVG.Line();

SVG.extend(SVG.Container, {
    lineShape: function (x1, y1, x2, y2) {
        return this.put(new SVG.LineShape(x1, y1, x2, y2));
    }
});
