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

describe('Filter scBranchOrderBy', function () {

    var ConfigService, $httpBackend, HostnameAndPort, TestData;

    var scBranchOrderByFilter;

    beforeEach(module('scenarioo.controllers'));
    beforeEach(angular.mock.module('scenarioo.services'));

    // load module
    beforeEach(module('scenarioo.filters'));
    beforeEach(inject(function ($filter, _ConfigService_, _$httpBackend_, _TestData_, _HostnameAndPort_) {

        ConfigService = _ConfigService_;
        $httpBackend = _$httpBackend_;
        TestData = _TestData_;
        HostnameAndPort = _HostnameAndPort_;

        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/configuration').respond(TestData.CONFIG_BRANCH_SELECTION_1);

        ConfigService.load();
        $httpBackend.flush();

        scBranchOrderByFilter = $filter('scBranchOrderBy');
    }));

    describe('should handle invalid input gracefully:', function () {
        it('string', function () {
            var result = scBranchOrderByFilter('someString');
            expect(result).toEqual('someString');
        });
        it('object', function () {
            var result = scBranchOrderByFilter({some: 'object'});
            expect(result).toEqual({some: 'object'});
        });
        it('number', function () {
            var result = scBranchOrderByFilter(1);
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
                    },
                    builds: [
                        {
                            build: {date: 0}
                        }
                    ]
                },
                {
                    alias: true,
                    branch: {
                        name: 'B'
                    },
                    builds: [
                        {
                            build: {date: 1}
                        }
                    ]
                }
            ];


            var result = scBranchOrderByFilter(inputArray);

            expect(result[0].branch.name).toEqual('B');
            expect(result[1].branch.name).toEqual('A');
        });

        it('then alphabetically (not case sensitive!)', function () {

            var inputArray = [
                {
                    alias: true,
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
                    alias: true,
                    branch: {
                        name: 'aa'
                    }
                }
            ];

            var result = scBranchOrderByFilter(inputArray);

            expect(result[0].branch.name).toEqual('aa');
            expect(result[1].branch.name).toEqual('Ae');
            expect(result[2].branch.name).toEqual('Ba');
            expect(result[3].branch.name).toEqual('be');
        });

        it('then by newest build date', function () {

            var inputArray = [
                {
                    alias: false,
                    branch: {
                        name: 'Ae'
                    },
                    builds: [
                        {
                            build: {date: 1}
                        }
                    ]
                },
                {
                    alias: false,
                    branch: {
                        name: 'Ba'
                    },
                    builds: [
                        {
                            build: {date: 2}
                        }
                    ]
                },
                {
                    alias: false,
                    branch: {
                        name: 'be'
                    },
                    builds: [
                        {
                            build: {date: 3}
                        }
                    ]
                },
                {
                    alias: false,
                    branch: {
                        name: 'aa'
                    },
                    builds: [
                        {
                            build: {date: 4}
                        }
                    ]
                }
            ];

            var result = scBranchOrderByFilter(inputArray);

            expect(result[0].branch.name).toEqual('aa');
            expect(result[1].branch.name).toEqual('be');
            expect(result[2].branch.name).toEqual('Ba');
            expect(result[3].branch.name).toEqual('Ae');
        });

    });


});
