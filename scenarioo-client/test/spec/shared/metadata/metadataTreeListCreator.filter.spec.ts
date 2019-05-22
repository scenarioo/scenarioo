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

describe('Filter scMetadataTreeListCreator', function () {

    var DATA = {
        myKey: 'myValue',
        keyTwo: {
            theAnswer: 42
        }
    };
    var DATA_TRANSFORMED = {
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

    beforeEach(angular.mock.module('scenarioo.filters'));

    var scMetadataTreeCreator;
    beforeEach(inject(function (_$filter_) {
        scMetadataTreeCreator = _$filter_('scMetadataTreeListCreator', {$filter: _$filter_});
    }));

    it('transforms javascript object into a list of optimized trees', function () {
        expect(scMetadataTreeCreator(DATA)).toEqual(DATA_TRANSFORMED);
    });

});
