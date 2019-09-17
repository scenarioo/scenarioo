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

import {MetadataTreeListCreatorPipe} from './metadataTreeListCreator.pipe';
import {MetadataTreeCreatorPipe} from './metadataTreeCreator.pipe';
import {TreeDataOptimizerPipe} from './treeDataOptimizer.pipe';
import {TreeDataCreatorPipe} from './treeDataCreator.pipe';

'use strict';

describe('Pipe: scMetadataTreeListCreator', () => {
    let scMetadataTreeListCreator: MetadataTreeListCreatorPipe;
    let input;

    let DATA = {
        myKey: 'myValue',
        keyTwo: {
            theAnswer: 42
        }
    };

    let DATA_TRANSFORMED = {
        myKey: {
            nodeLabel: 'myValue'
        },
        keyTwo: {
            childNodes: [
                {
                    nodeLabel: 'theAnswer',
                    nodeValue: 42
                }
            ]
        }
    };

    // Arrange: set up new instance of the pipe MetadataTreeListCreator
    beforeEach(() => {
        scMetadataTreeListCreator = new MetadataTreeListCreatorPipe(new MetadataTreeCreatorPipe(new TreeDataOptimizerPipe(), new TreeDataCreatorPipe()));
    });

    it('transforms javascript object into a list of optimized trees', () => {
        // Act
        input = scMetadataTreeListCreator.transform(DATA);
        // Assert
        expect(input).toEqual(DATA_TRANSFORMED);
    });

});
