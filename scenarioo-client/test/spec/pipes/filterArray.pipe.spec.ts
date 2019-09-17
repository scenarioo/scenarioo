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

import {FilterArrayPipe} from '../../../app/pipes/filterArray.pipe';

'use strict';

describe('Pipe: scFilterArray', () => {
    let scFilterArray: FilterArrayPipe;
    let input;

    let MODEL = [
        {
            test: 'test',
            something: 'else',
            odd: {
                weird: 'things'
            }
        },
        {
            test: 'hi',
            something: 'there',
            odd: {
                weird: 'things'
            }
        }
    ];

    let MODEL_FILTERED = [
        {
            test: 'test',
            something: 'else',
            odd: {
                weird: 'things'
            }
        }
    ];

    let MODEL_EMPTY = [];

    // Arrange: set up new instance of the pipe FilterArray
    beforeEach(() => {
        scFilterArray = new FilterArrayPipe();
    });

    it('Should return the original model when search text is empty', () => {
        // Act
        input = scFilterArray.transform(MODEL, '');
        // Assert
        expect(input).toEqual(MODEL);
    });

    it('Should filter the model when search text is a normal string', () => {
        // Act
        input = scFilterArray.transform(MODEL, 'test');
        // Assert
        expect(input).toEqual(MODEL_FILTERED);
    });

    it('Should filter the model, regarding search is not case sensitive', () => {
        // Act
        input = scFilterArray.transform(MODEL, 'TEST');
        // Assert
        expect(input).toEqual(MODEL_FILTERED);
    });

    describe('when search text consists of multiple words, ', () => {
        it('keeps all objects in the model, that contain both words', () => {
            // Act
            input = scFilterArray.transform(MODEL, 'test else');
            // Assert
            expect(input).toEqual(MODEL_FILTERED);
        });

        it('filters out all objects that miss one or more words', () => {
            // Act
            input = scFilterArray.transform(MODEL, 'test weirdthing');
            // Assert
            expect(input).toEqual(MODEL_EMPTY);
        });

        it('keeps the object if the search words were found internally on different levels', () => {
            // Act
            input = scFilterArray.transform(MODEL, 'test THINGS');
            // Assert
            expect(input).toEqual(MODEL_FILTERED);
        });

    });

});
