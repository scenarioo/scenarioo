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

import {TreeDataCreatorPipe} from '../../../../app/pipes/metadata/treeDataCreator.pipe';

'use strict';

describe('Pipe: scTreeDataCreator', () => {
    let scTreeDataCreator: TreeDataCreatorPipe;
    let input;

    let TWO_INPUTS = {
        myKey: 'myValue',
        keyTwo: 'valueTwo'
    };

    let TWO_INPUTS_TRANSFORMED = {
        nodeLabel: '',
        childNodes: [
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

    let COMPLEX_INPUTS = {
        'details': {
            'start': '12312',
            'end': [
                {'val': '23123'},
                {'val2': '111'}
            ]
        },
        'name': 'page_load',
        'type': 'statistics'
    };

    let COMPLEX_INPUTS_TRANSFORMED = {
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

    let CHILDNODES_WITH_NODELABEL = {
        'list': [
            'Listentry 0', 'Listentry 1'
        ]
    };

    let CHILDNODES_WITH_NODELABEL_TRANSFORMED = {
        nodeLabel: '',
        childNodes: [
            {
                nodeLabel: 'list',
                childNodes: [
                    {
                        nodeLabel: 'Listentry 0'
                    },
                    {
                        nodeLabel: 'Listentry 1'
                    }
                ]
            }
        ]
    };

    let CHILDNODES_WITHOUT_NODELABEL = ['Listentry 0', 'Listentry 1'];

    let CHILDNODES_WITHOUT_NODELABEL_TRANSFORMED = {
        nodeLabel: '',
        childNodes: [
            {nodeLabel: 'Listentry 0'},
            {nodeLabel: 'Listentry 1'}
        ]
    };

    // Arrange: set up new instance of the pipe TreeDataCreator
    beforeEach(() => {
        scTreeDataCreator = new TreeDataCreatorPipe();
    });

    fit('Should return undefined from an undefined input', () => {
        // Act
        input = scTreeDataCreator.transform(undefined);
        // Assert
        expect(input).toBeUndefined();
    });

    fit('Should create a tree from a string', () => {
        // Act
        input = scTreeDataCreator.transform('someStringValue');
        // Assert
        expect(input).toEqual({nodeLabel: 'someStringValue'});
    });

    fit('Should create a tree from an empty input', () => {
        // Act
        input = scTreeDataCreator.transform({});
        // Assert
        expect(input).toEqual({nodeLabel: '', childNodes: []});
    });

    fit('Should create two trees from two input values', () => {
        // Act
        input = scTreeDataCreator.transform(TWO_INPUTS);
        // Assert
        expect(input).toEqual(TWO_INPUTS_TRANSFORMED);
    });

    fit('Should create correct trees from complex input values', () => {
        // Act
        input = scTreeDataCreator.transform(COMPLEX_INPUTS);
        // Assert
        expect(input).toEqual(COMPLEX_INPUTS_TRANSFORMED);
    });

    fit('Should identify childNodes of a nodeLabel', () => {
        // Act
        input = scTreeDataCreator.transform(CHILDNODES_WITH_NODELABEL);
        // Assert
        expect(input).toEqual(CHILDNODES_WITH_NODELABEL_TRANSFORMED);
    });

    fit('Should identify childNodes without having a nodeLabel', () => {
        // Act
        input = scTreeDataCreator.transform(CHILDNODES_WITHOUT_NODELABEL);
        // Assert
        expect(input).toEqual(CHILDNODES_WITHOUT_NODELABEL_TRANSFORMED);
    });

});
