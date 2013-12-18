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

describe('Filter: scenarioFilter,', function () {

    beforeEach(module('scenarioo.filters'));

    var MODEL = [
        {
            test: 'test',
            something: 'else',
            odd: {
                weird: 'things'
            }
        },
        {
            test: 'hi',
            something: 'there',
            odd: {
                weird: 'things'
            }
        }
    ];

    var MODEL_FILTERED = [
        {
            test: 'test',
            something: 'else',
            odd: {
                weird: 'things'
            }
        }
    ];

    var MODEL_EMPTY = [];

    var scFilter;
    beforeEach(inject(function ($filter) {
        scFilter = $filter('scFilterArray');
    }));

    describe('when there is search text is empty,', function () {
        it('should return the original model', function () {
            expect(scFilter(MODEL, '')).toEqual(MODEL);
        });
    });

    describe(' search is not case sensitive,', function () {
        it('should filter the model', function () {
            expect(scFilter(MODEL, 'TEST')).toEqual(MODEL_FILTERED);
        });
    });

    describe('when search text is a simple string,', function () {
        it('should filter the model', function () {
            expect(scFilter(MODEL, 'test')).toEqual(MODEL_FILTERED);
        });
    });

    describe('when search text consists of multiple words,', function () {
        it('keeps all objects in the model, that contain both words', function () {
            expect(scFilter(MODEL, 'test else')).toEqual(MODEL_FILTERED);
        });

        it('filters out all objects that miss one or more words,', function () {
            expect(scFilter(MODEL, 'test weirdthing')).toEqual(MODEL_EMPTY);
        });

        it('keeps the object if the search words were found internally on different levels,', function () {
            expect(scFilter(MODEL, 'test THINGS')).toEqual(MODEL_FILTERED);
        });
    });

});
