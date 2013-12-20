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

describe('Filter', function () {
    var filter;

    // load module
    beforeEach(module('scenarioo.filters'));
    beforeEach(inject(function($filter) {
        filter = $filter;
    }));

    it('Should create a human readable string', function() {
        var result = filter('scHumanReadable')('ThisIsSomeCamelCaseString');

        expect(result).toEqual('This Is Some Camel Case String');
    });

    it('Should start with a capital letter', function() {
        var result = filter('scHumanReadable')('someStringStartingSmall');

        expect(result).toEqual('Some String Starting Small');
    });

    it('Should accept special characters', function() {
        var result = filter('scHumanReadable')('thisIsSomeCamel-Case&/%String');

        expect(result).toContain(' Some Camel-Case');
        expect(result).toContain('&/%');
        expect(result).toContain('String');
    });

    it('Should replace underline with blanks', function() {
        var result = filter('scHumanReadable')('This_may_Also_be_Acceptable');

        expect(result).toEqual('This may Also be Acceptable');
    });
});
