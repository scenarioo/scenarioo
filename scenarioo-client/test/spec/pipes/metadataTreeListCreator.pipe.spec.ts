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

import {MetadataTreeListCreatorPipe} from '../../../app/pipes/metadataTreeListCreator.pipe';
import {MetadataTreeCreatorPipe} from '../../../app/pipes/metadataTreeCreator.pipe';
import {TreeDataOptimizerPipe} from '../../../app/pipes/treeDataOptimizer.pipe';
import {TreeDataCreatorPipe} from '../../../app/pipes/treeDataCreator.pipe';

describe('MetadataTreeListCreatorPipe', () => {

    let pipe: MetadataTreeListCreatorPipe;

    const data = {
        myKey: 'myValue',
        keyTwo: {
            theAnswer: 42
        }
    };

    const dataTransformed = {
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

    beforeEach(() => {
        pipe = new MetadataTreeListCreatorPipe(new MetadataTreeCreatorPipe(new TreeDataOptimizerPipe(), new TreeDataCreatorPipe()));
    });

    it('transforms javascript object into a list of optimized trees', () => {
        expect(pipe.transform(data)).toEqual(dataTransformed);
    });
});
