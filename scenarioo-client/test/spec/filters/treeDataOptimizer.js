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

describe('Filter :: scTreeDataOptimizer', function () {

    var DATA_EMPTY_NODE = {
        childNodes: [
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            },
            {
                nodeLabel: 'empty',
                childNodes: []
            }
        ]
    };
    var DATA_EMPTY_NODE_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            }
        ]
    };

    var DATA_DETAILS = {
        childNodes: [
            {
                nodeLabel: 'details',
                childNodes: [
                    {
                        nodeLabel: 'hero',
                        nodeValue: 'Donald Duck'
                    }
                ]
            }
        ]
    };
    var DATA_DETAILS_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'hero',
                nodeValue: 'Donald Duck'
            }
        ]
    };

    var DATA_HUMAN_READABLE = {
        nodeLabel: 'veryImportantNode',
        childNodes: [
            {
                nodeLabel: 'firstName',
                nodeValue: 'donaldDuck'
            }
        ]
    };
    var DATA_HUMAN_READABLE_OPTIMIZED = {
        nodeLabel: 'veryImportantNode',
        childNodes: [
            {
                nodeLabel: 'firstName',
                nodeValue: 'donaldDuck'
            }
        ]
    };

    var DATA_EMPTY_LABEL = {
        nodeLabel: '',
        childNodes: [
            {
                nodeLabel: '',
                nodeValue: 'Juliet',
                childNodes: [
                    {
                        nodeLabel: 'name',
                        nodeValue: 'Romeo\'s love'
                    }
                ]
            }
        ]
    };
    var DATA_EMPTY_LABEL_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'Romeo\'s love',
                nodeValue: 'Juliet',
                childNodes: []
            }
        ]
    };

    var DATA_EMPTY_VALUE = {
        nodeLabel: '',
        childNodes: [
            {
                nodeLabel: 'Juliet',
                nodeValue: '',
                childNodes: [
                    {
                        nodeLabel: 'name',
                        nodeValue: 'Romeo\'s love'
                    }
                ]
            }
        ]
    };
    var DATA_EMPTY_VALUE_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'Juliet',
                nodeValue: 'Romeo\'s love',
                childNodes: []
            }
        ]
    };

    var DATA_TYPE = {
        nodeLabel: 'something',
        nodeValue: 'special',
        childNodes: [
            {
                nodeLabel: 'type',
                nodeValue: 'configModule'
            }
        ]
    };
    var DATA_TYPE_OPTIMIZED = {
        nodeLabel: 'configModule',
        nodeValue: 'special',
        childNodes: []
    };

    var CHILDREN = {
        nodeLabel: 'something',
        nodeValue: 'special',
        childNodes: [
            {
                nodeLabel: 'children',
                childNodes: [
                    {
                        nodeLabel: 'someLabel'
                    }
                ]
            },
            {
                nodeLabel: 'otherNode'
            }
        ]
    };
    var CHILDREN_OPTIMIZED = {
        nodeLabel: 'something',
        nodeValue: 'special',
        childNodes: [
            {
                nodeLabel: 'otherNode'
            },
            {
                nodeLabel: 'children',
                childNodes: [
                    {
                        nodeLabel: 'someLabel'
                    }
                ]
            }
        ]
    };

    var EMPTY_LABEL = {
        nodeLabel: 'something',
        nodeValue: 'special',
        childNodes: [
            {
                nodeLabel: ''
            }
        ]
    };
    var EMPTY_LABEL_OPTIMIZED = {
        nodeLabel: 'something',
        nodeValue: 'special',
        childNodes: [
            {
                nodeLabel: 'Item'
            }
        ]
    };

    var ITEM_LABEL_ROOT_NODE = {
        nodeLabel: 'Item'
    };
    var ITEM_LABEL_ROOT_NODE_OPTIMIZED = {};

    var ITEM_LABEL_WITH_VALUE_ROOT_NODE = {
        nodeLabel: 'Item',
        nodeValue: 'someValue'
    };
    var ITEM_LABEL_WITH_VALUE_ROOT_NODE_OPTIMIZED = {
        nodeLabel: 'Item',
        nodeValue: 'someValue'
    };


    beforeEach(module('scenarioo.filters'));

    var scTreeDataOptimizer;
    beforeEach(inject(function ($filter) {
        scTreeDataOptimizer = $filter('scTreeDataOptimizer');
    }));

    // TODO Check with Rolf whether we need to remove empty child nodes
    xit('removes empty nodes"', function () {
        expect(scTreeDataOptimizer(DATA_EMPTY_NODE)).toEqual(DATA_EMPTY_NODE_OPTIMIZED);
    });

    it('pulls children of details nodes one level up', function () {
        expect(scTreeDataOptimizer(DATA_DETAILS)).toEqual(DATA_DETAILS_OPTIMIZED);
    });

    it('makes node labels human readable but does not touch node values', function () {
        expect(scTreeDataOptimizer(DATA_HUMAN_READABLE)).toEqual(DATA_HUMAN_READABLE_OPTIMIZED);
    });

    it('uses the name child node to replace empty node label', function () {
        expect(scTreeDataOptimizer(DATA_EMPTY_LABEL)).toEqual(DATA_EMPTY_LABEL_OPTIMIZED);
    });

    it('uses the name child node to replace empty node values', function () {
        expect(scTreeDataOptimizer(DATA_EMPTY_VALUE)).toEqual(DATA_EMPTY_VALUE_OPTIMIZED);
    });

    it('always uses the type child node to replace the parent node label', function () {
        expect(scTreeDataOptimizer(DATA_TYPE)).toEqual(DATA_TYPE_OPTIMIZED);
    });

    it('moves nodes with label "children" behind all other childNodes', function () {
        expect(scTreeDataOptimizer(CHILDREN)).toEqual(CHILDREN_OPTIMIZED);
    });

    it('sets nodeLabel of nodes with empty label to "Item"', function () {
        expect(scTreeDataOptimizer(EMPTY_LABEL)).toEqual(EMPTY_LABEL_OPTIMIZED);
    });

    it('sets nodeLabel of root node to empty, if it is "Item"', function () {
        expect(scTreeDataOptimizer(ITEM_LABEL_ROOT_NODE)).toEqual(ITEM_LABEL_ROOT_NODE_OPTIMIZED);
    });

    it('does not change nodeLabel of root node to empty, if it is "Item" and also has a nodeValue', function () {
        expect(scTreeDataOptimizer(ITEM_LABEL_WITH_VALUE_ROOT_NODE)).toEqual(ITEM_LABEL_WITH_VALUE_ROOT_NODE_OPTIMIZED);
    });

});
