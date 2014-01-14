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

describe('Filter :: scTreeDataCreator', function () {

    var DATA_STRING = 'someStringValue';
    var DATA_STRING_TRANSFORMED = { nodeLabel : 'Value', nodeValue : 'someStringValue', childNodes : [  ] };

    var DATA_1 = {};
    var DATA_1_TRANSFORMED = { nodeLabel : '', childNodes : [  ] };

    var DATA_2 = {
        myKey : 'myValue',
        keyTwo : 'valueTwo'
    };
    var DATA_2_TRANSFORMED = {
        nodeLabel : '',
        childNodes : [
            {
                nodeLabel: 'myKey',
                nodeValue: 'myValue'
            },
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            }
        ]
    };

    var DATA_3 = {
        'details': {
            'start': '12312',
            'end': [
                { 'val' : '23123' },
                { 'val2' : '111' }
            ]
        },
        'name' : 'page_load',
        'type' : 'statistics'
    };
    var DATA_3_TRANSFORMED = {
        nodeLabel: '',
        childNodes: [
            {
                nodeLabel: 'details',
                childNodes: [
                    {
                        nodeLabel: 'start',
                        nodeValue: '12312'
                    },
                    {
                        nodeLabel: 'end',
                        childNodes: [
                            {
                                nodeLabel: '',
                                childNodes: [
                                    {
                                        nodeLabel: 'val',
                                        nodeValue: '23123'
                                    }
                                ]
                            },
                            {
                                nodeLabel: '',
                                childNodes: [
                                    {
                                        nodeLabel: 'val2',
                                        nodeValue: '111'
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            {
                nodeLabel: 'name',
                nodeValue: 'page_load'
            },
            {
                nodeLabel: 'type',
                nodeValue: 'statistics'
            }
        ]
    };

    beforeEach(module('scenarioo.filters'));

    var scTreeDataCreator;
    beforeEach(inject(function ($filter) {
        scTreeDataCreator = $filter('scTreeDataCreator');
    }));

    it('creates empty tree from undefined input', function() {
        var tree = scTreeDataCreator(undefined);
        expect(tree).toBeUndefined();
    });


    it('transforms DATA_STRING', function() {
        var tree = scTreeDataCreator(DATA_STRING);
        expect(tree).toEqual(DATA_STRING_TRANSFORMED);
    });

    it('transforms DATA_1', function() {
        var tree = scTreeDataCreator(DATA_1);
        expect(tree).toEqual(DATA_1_TRANSFORMED);
    });

    it('transforms DATA_2', function() {
        var tree = scTreeDataCreator(DATA_2);
        expect(tree).toEqual(DATA_2_TRANSFORMED);
    });

    it('transforms DATA_3', function() {
        var tree = scTreeDataCreator(DATA_3);
        expect(tree).toEqual(DATA_3_TRANSFORMED);
    });

});