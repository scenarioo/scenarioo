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

describe('Pipe: scTreeDataOptimizer', () => {

    let scTreeDataOptimizer: TreeDataOptimizerPipe;

    beforeEach(() => {
        scTreeDataOptimizer = new TreeDataOptimizerPipe();
    });

    it('does not optimize empty nodes"', async () => {

        // Act
        const output = scTreeDataOptimizer.transform({
            childNodes: [
                {
                    nodeLabel: 'keyTwo',
                    nodeValue: 'valueTwo',
                },
                {
                    // empty node with label only
                    nodeLabel: 'empty',
                    childNodes: [],
                },
            ],
        });

        // Assert
        await expect(output).toEqual({
            childNodes: [
                {
                    nodeLabel: 'keyTwo',
                    nodeValue: 'valueTwo',
                },
                {
                    // empty node is kept - not optimized (who inputs crap, receives crap ;-) )
                    nodeLabel: 'empty',
                    childNodes: [],
                },
            ],
        });
    });

    it('pulls children of details nodes one level up', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            childNodes: [
                {
                    nodeLabel: 'details',
                    childNodes: [
                        {
                            nodeLabel: 'hero',
                            nodeValue: 'Donald Duck',
                        },
                    ],
                },
            ],
        });
        // Assert
        await expect(output).toEqual({
            childNodes: [
                {
                    nodeLabel: 'hero',
                    nodeValue: 'Donald Duck',
                },
            ],
        });
    });

    it('makes node labels human readable but does not touch node values', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            nodeLabel: 'veryImportantNode',
            childNodes: [
                {
                    nodeLabel: 'firstName',
                    nodeValue: 'donaldDuck',
                },
            ],
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: 'veryImportantNode',
            childNodes: [
                {
                    nodeLabel: 'firstName',
                    nodeValue: 'donaldDuck',
                },
            ],
        });
    });

    it('uses the name child node to replace empty node label', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            nodeLabel: '',
            childNodes: [
                {
                    nodeLabel: '',
                    nodeValue: 'Juliet',
                    childNodes: [
                        {
                            nodeLabel: 'type',
                            nodeValue: 'Person',
                        },
                        {
                            nodeLabel: 'name',
                            nodeValue: 'Romeo\'s love',
                        },
                    ],
                },
            ],
        });
        // Assert
        await expect(output).toEqual({
            childNodes: [
                {
                    nodeLabel: 'Person',
                    nodeValue: 'Juliet',
                    childNodes: [
                        {
                            nodeLabel: 'name',
                            nodeValue: 'Romeo\'s love',
                        },
                    ],
                    nodeObjectType: 'Person',
                },
            ],
        });
    });

    it('uses the name child node to replace empty node values', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            nodeLabel: '',
            childNodes: [
                {
                    nodeLabel: 'Juliet',
                    nodeValue: '',
                    childNodes: [
                        {
                            nodeLabel: 'name',
                            nodeValue: 'Romeo\'s love',
                        },
                    ],
                },
            ],
        });
        // Assert
        await expect(output).toEqual({
            childNodes: [
                {
                    nodeLabel: 'Juliet',
                    nodeValue: 'Romeo\'s love',
                    childNodes: [],
                    nodeObjectName: 'Romeo\'s love',
                },
            ],
        });
    });

    it('always uses the type child node to replace the parent node label', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            childNodes: [
                {
                    nodeLabel: 'type',
                    nodeValue: 'MyObjectType',
                },
                {
                    nodeLabel: 'name',
                    nodeValue: 'MyObjectName',
                },
            ],
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: 'MyObjectType',
            nodeValue: 'MyObjectName',
            childNodes: [],
            nodeObjectType: 'MyObjectType',
            nodeObjectName: 'MyObjectName',
        });
    });

    it('moves nodes with label "children" behind all other childNodes', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            nodeLabel: 'something',
            nodeValue: 'special',
            childNodes: [
                {
                    nodeLabel: 'children',
                    childNodes: [
                        {
                            nodeLabel: 'someLabel',
                        },
                    ],
                },
                {
                    nodeLabel: 'otherNode',
                },
            ],
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: 'something',
            nodeValue: 'special',
            childNodes: [
                {
                    nodeLabel: 'otherNode',
                },
                {
                    nodeLabel: 'children',
                    childNodes: [
                        {
                            nodeLabel: 'someLabel',
                        },
                    ],
                },
            ],
        });
    });

    it('sets nodeLabel of nodes with empty label to "Item"', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            nodeLabel: 'something',
            nodeValue: 'special',
            childNodes: [
                {
                    nodeLabel: '',
                },
            ],
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: 'something',
            nodeValue: 'special',
            childNodes: [
                {
                    nodeLabel: 'Item',
                },
            ],
        });
    });

    it('sets nodeLabel of root node to empty, if it is "Item"', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            nodeLabel: 'Item',
        });
        // Assert
        await expect(output).toEqual({});
    });

    it('does not change nodeLabel of root node to empty, if it is "Item" and also has a nodeValue', async () => {
        // Act
        const output = scTreeDataOptimizer.transform({
            nodeLabel: 'Item',
            nodeValue: 'someValue',
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: 'Item',
            nodeValue: 'someValue',
        });
    });

});
