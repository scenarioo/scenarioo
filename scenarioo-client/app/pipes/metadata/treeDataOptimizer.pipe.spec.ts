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

import {TreeDataOptimizerPipe} from './treeDataOptimizer.pipe';

'use strict';

describe('Pipe: scTreeDataOptimizer', () => {
    let scTreeDataOptimizer: TreeDataOptimizerPipe;
    let input;

    let DATA_EMPTY_NODE = {
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

    let DATA_EMPTY_NODE_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'keyTwo',
                nodeValue: 'valueTwo'
            }
        ]
    };

    let DATA_DETAILS = {
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

    let DATA_DETAILS_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'hero',
                nodeValue: 'Donald Duck'
            }
        ]
    };

    let DATA_HUMAN_READABLE = {
        nodeLabel: 'veryImportantNode',
        childNodes: [
            {
                nodeLabel: 'firstName',
                nodeValue: 'donaldDuck'
            }
        ]
    };

    let DATA_HUMAN_READABLE_OPTIMIZED = {
        nodeLabel: 'veryImportantNode',
        childNodes: [
            {
                nodeLabel: 'firstName',
                nodeValue: 'donaldDuck'
            }
        ]
    };

    let DATA_EMPTY_LABEL = {
        nodeLabel: '',
        childNodes: [
            {
                nodeLabel: '',
                nodeValue: 'Juliet',
                childNodes: [
                    {
                        nodeLabel: 'type',
                        nodeValue: 'Person'
                    },
                    {
                        nodeLabel: 'name',
                        nodeValue: 'Romeo\'s love'
                    }
                ]
            }
        ]
    };

    let DATA_EMPTY_LABEL_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'Person',
                nodeValue: 'Juliet',
                childNodes: [
                    {
                        nodeLabel: 'name',
                        nodeValue: 'Romeo\'s love'
                    }
                ],
                nodeObjectType: 'Person'
            }
        ]
    };

    let DATA_EMPTY_VALUE = {
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

    let DATA_EMPTY_VALUE_OPTIMIZED = {
        childNodes: [
            {
                nodeLabel: 'Juliet',
                nodeValue: 'Romeo\'s love',
                childNodes: [],
                nodeObjectName: 'Romeo\'s love'
            }
        ]
    };

    let DATA_TYPE = {

        childNodes: [
            {
                nodeLabel: 'type',
                nodeValue: 'MyObjectType'
            },
            {
                nodeLabel: 'name',
                nodeValue: 'MyObjectName'
            }

        ]
    };

    let DATA_TYPE_OPTIMIZED = {
        nodeLabel: 'MyObjectType',
        nodeValue: 'MyObjectName',
        childNodes: [],
        nodeObjectType: 'MyObjectType',
        nodeObjectName: 'MyObjectName'
    };

    let CHILDREN = {
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

    let CHILDREN_OPTIMIZED = {
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

    let EMPTY_LABEL = {
        nodeLabel: 'something',
        nodeValue: 'special',
        childNodes: [
            {
                nodeLabel: ''
            }
        ]
    };

    let EMPTY_LABEL_OPTIMIZED = {
        nodeLabel: 'something',
        nodeValue: 'special',
        childNodes: [
            {
                nodeLabel: 'Item'
            }
        ]
    };

    let ITEM_LABEL_ROOT_NODE = {
        nodeLabel: 'Item'
    };

    let ITEM_LABEL_ROOT_NODE_OPTIMIZED = {};

    let ITEM_LABEL_WITH_VALUE_ROOT_NODE = {
        nodeLabel: 'Item',
        nodeValue: 'someValue'
    };

    let ITEM_LABEL_WITH_VALUE_ROOT_NODE_OPTIMIZED = {
        nodeLabel: 'Item',
        nodeValue: 'someValue'
    };

    // Arrange: set up new instance of the pipe TreeDataOptimizer
    beforeEach(() => {
        scTreeDataOptimizer = new TreeDataOptimizerPipe();
    });

    // TODO Check with Rolf whether we need to remove empty child nodes
    xit('removes empty nodes"', () => {
        // Act
        input = scTreeDataOptimizer.transform(DATA_EMPTY_NODE);
        // Assert
        expect(input).toEqual(DATA_EMPTY_NODE_OPTIMIZED);
    });

    it('pulls children of details nodes one level up', () => {
        // Act
        input = scTreeDataOptimizer.transform(DATA_DETAILS);
        // Assert
        expect(input).toEqual(DATA_DETAILS_OPTIMIZED);
    });

    it('makes node labels human readable but does not touch node values', () => {
        // Act
        input = scTreeDataOptimizer.transform(DATA_HUMAN_READABLE);
        // Assert
        expect(input).toEqual(DATA_HUMAN_READABLE_OPTIMIZED);
    });

    it('uses the name child node to replace empty node label', () => {
        // Act
        input = scTreeDataOptimizer.transform(DATA_EMPTY_LABEL);
        // Assert
        expect(input).toEqual(DATA_EMPTY_LABEL_OPTIMIZED);
    });

    it('uses the name child node to replace empty node values', () => {
        // Act
        input = scTreeDataOptimizer.transform(DATA_EMPTY_VALUE);
        // Assert
        expect(input).toEqual(DATA_EMPTY_VALUE_OPTIMIZED);
    });

    it('always uses the type child node to replace the parent node label', () => {
        // Act
        input = scTreeDataOptimizer.transform(DATA_TYPE);
        // Assert
        expect(input).toEqual(DATA_TYPE_OPTIMIZED);
    });

    it('moves nodes with label "children" behind all other childNodes', () => {
        // Act
        input = scTreeDataOptimizer.transform(CHILDREN);
        // Assert
        expect(input).toEqual(CHILDREN_OPTIMIZED);
    });

    it('sets nodeLabel of nodes with empty label to "Item"', () => {
        // Act
        input = scTreeDataOptimizer.transform(EMPTY_LABEL);
        // Assert
        expect(input).toEqual(EMPTY_LABEL_OPTIMIZED);
    });

    it('sets nodeLabel of root node to empty, if it is "Item"', () => {
        // Act
        input = scTreeDataOptimizer.transform(ITEM_LABEL_ROOT_NODE);
        // Assert
        expect(input).toEqual(ITEM_LABEL_ROOT_NODE_OPTIMIZED);
    });

    it('does not change nodeLabel of root node to empty, if it is "Item" and also has a nodeValue', () => {
        // Act
        input = scTreeDataOptimizer.transform(ITEM_LABEL_WITH_VALUE_ROOT_NODE);
        // Assert
        expect(input).toEqual(ITEM_LABEL_WITH_VALUE_ROOT_NODE_OPTIMIZED);
    });

});
