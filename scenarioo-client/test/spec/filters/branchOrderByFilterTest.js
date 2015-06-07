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

describe('BranchOrderByFilter', function () {
    var branchOrderByFilter;

    // load module
    beforeEach(module('scenarioo.filters'));
    beforeEach(inject(function ($filter) {
        branchOrderByFilter = $filter('branchOrderBy');
    }));

    describe('should handle invalid input gracefully:', function () {
        it('string', function () {
            var result = branchOrderByFilter('someString');
            expect(result).toEqual('someString');
        });
        it('object', function () {
            var result = branchOrderByFilter({some: 'object'});
            expect(result).toEqual({some: 'object'});
        });
        it('number', function () {
            var result = branchOrderByFilter(1);
            expect(result).toEqual(1);
        });
    });

    describe('should order given branch resource objects:', function () {

        it('alias branches first', function () {

            var inputArray = [
                {
                    alias: false,
                    branch: {
                        name: 'A'
                    }
                },
                {
                    alias: true,
                    branch: {
                        name: 'B'
                    }
                }
            ];


            var result = branchOrderByFilter(inputArray);

            expect(result[0].branch.name).toEqual('B');
            expect(result[1].branch.name).toEqual('A');
        });

        it('then alphabetically (not case sensitive!)', function () {

            var inputArray = [
                {
                    alias: false,
                    branch: {
                        name: 'Ae'
                    }
                },
                {
                    alias: true,
                    branch: {
                        name: 'Ba'
                    }
                },
                {
                    alias: true,
                    branch: {
                        name: 'be'
                    }
                },
                {
                    alias: false,
                    branch: {
                        name: 'aa'
                    }
                }
            ];

            var result = branchOrderByFilter(inputArray);

            expect(result[0].branch.name).toEqual('Ba');
            expect(result[1].branch.name).toEqual('be');
            expect(result[2].branch.name).toEqual('aa');
            expect(result[3].branch.name).toEqual('Ae');
        });

    });


});
