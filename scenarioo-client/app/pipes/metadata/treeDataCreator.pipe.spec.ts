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

import {TreeDataCreatorPipe} from './treeDataCreator.pipe';

describe('Pipe: scTreeDataCreator', () => {
    let scTreeDataCreator: TreeDataCreatorPipe;

    beforeEach(() => {
        scTreeDataCreator = new TreeDataCreatorPipe();
    });

    it('Should return undefined from an undefined input', async () => {
        // Act
        const output = scTreeDataCreator.transform(undefined);
        // Assert
        await expect(output).toBeUndefined();
    });

    it('Should create a tree from a string', async () => {
        // Act
        const output = scTreeDataCreator.transform('someStringValue');
        // Assert
        await expect(output).toEqual({nodeLabel: 'someStringValue'});
    });

    it('Should create a tree from an empty input', async () => {
        // Act
        const output = scTreeDataCreator.transform({});
        // Assert
        await expect(output).toEqual({nodeLabel: '', childNodes: []});
    });

    it('Should create two trees from two input values', async () => {
        // Act
        const output = scTreeDataCreator.transform({
            myKey: 'myValue',
            keyTwo: 'valueTwo',
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: '',
            childNodes: [
                {
                    nodeLabel: 'myKey',
                    nodeValue: 'myValue',
                },
                {
                    nodeLabel: 'keyTwo',
                    nodeValue: 'valueTwo',
                },
            ],
        });
    });

    it('Should create correct trees from complex input values', async () => {
        // Act
        const output = scTreeDataCreator.transform({
            details: {
                start: '12312',
                end: [
                    {val: '23123'},
                    {val2: '111'},
                ],
            },
            name: 'page_load',
            type: 'statistics',
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: '',
            childNodes: [
                {
                    nodeLabel: 'details',
                    childNodes: [
                        {
                            nodeLabel: 'start',
                            nodeValue: '12312',
                        },
                        {
                            nodeLabel: 'end',
                            childNodes: [
                                {
                                    nodeLabel: '',
                                    childNodes: [
                                        {
                                            nodeLabel: 'val',
                                            nodeValue: '23123',
                                        },
                                    ],
                                },
                                {
                                    nodeLabel: '',
                                    childNodes: [
                                        {
                                            nodeLabel: 'val2',
                                            nodeValue: '111',
                                        },
                                    ],
                                },
                            ],
                        },
                    ],
                },
                {
                    nodeLabel: 'name',
                    nodeValue: 'page_load',
                },
                {
                    nodeLabel: 'type',
                    nodeValue: 'statistics',
                },
            ],
        });
    });

    it('Should identify childNodes of a nodeLabel', async () => {
        // Act
        const output = scTreeDataCreator.transform({
            list: [
                'Listentry 0', 'Listentry 1',
            ],
        });
        // Assert
        await expect(output).toEqual({
            nodeLabel: '',
            childNodes: [
                {
                    nodeLabel: 'list',
                    childNodes: [
                        {
                            nodeLabel: 'Listentry 0',
                        },
                        {
                            nodeLabel: 'Listentry 1',
                        },
                    ],
                },
            ],
        });
    });

    it('Should identify childNodes without having a nodeLabel', async () => {
        // Act
        const output = scTreeDataCreator.transform(['Listentry 0', 'Listentry 1']);
        // Assert
        await expect(output).toEqual({
            nodeLabel: '',
            childNodes: [
                {nodeLabel: 'Listentry 0'},
                {nodeLabel: 'Listentry 1'},
            ],
        });
    });

});
