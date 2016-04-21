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

'use strict';

describe('Service :: scUrlUtil', function () {

    var scUrlUtil;

    beforeEach(angular.mock.module('scenarioo.services'));

    beforeEach(inject(function (_scUrlUtil_) {
        scUrlUtil = _scUrlUtil_;
    }));

    it('returns empty string for usual URLs without context', function () {
        expect(scUrlUtil.getContextPathFromUrl('demo.scenarioo.org')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/#')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/#/')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/#/manage')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/#/manage?branch=xyz&build=1234')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('https://demo.scenarioo.org/#/manage')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/#')).toBe('');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/#/manage')).toBe('');
    });

    it('returns context path for usual URLs with simple context', function () {
        expect(scUrlUtil.getContextPathFromUrl('demo.scenarioo.org/scenarioo-demo-master')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo-demo-master')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo-demo-master/')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo-demo-master/#')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo-demo-master/#/manage')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('https://demo.scenarioo.org/scenarioo-demo-master/#/manage')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo-demo-master')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo-demo-master/')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo-demo-master/#')).toBe('scenarioo-demo-master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo-demo-master/#/manage')).toBe('scenarioo-demo-master');
    });

    it('returns context path for usual URLs with more complex context', function () {
        expect(scUrlUtil.getContextPathFromUrl('demo.scenarioo.org/scenarioo/master')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo/master')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo/master/')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo/master/#')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('http://demo.scenarioo.org/scenarioo/master/#/manage')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('https://demo.scenarioo.org/scenarioo/master/#/manage')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo/master')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo/master/')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo/master/#')).toBe('scenarioo/master');
        expect(scUrlUtil.getContextPathFromUrl('localhost:9000/scenarioo/master/#/manage')).toBe('scenarioo/master');
    });

});
