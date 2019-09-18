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

import {HumanReadablePipe} from './humanReadable.pipe';

describe('Pipe: scHumanReadable', () => {
    let scHumanReadable: HumanReadablePipe;
    let input;

    // Arrange: set up new instance of the pipe HumanReadable
    beforeEach(() => {
        scHumanReadable = new HumanReadablePipe();
    });

    it('Let an empty string be an empty string', () => {
        // Act
        input = scHumanReadable.transform('');
        // Assert
        void expect(input).toEqual('');
    });

    it('Should create a human readable string from a CamelCase string', () => {
        // Act
        input = scHumanReadable.transform('ThisIsSomeCamelCaseString');
        // Assert
        void expect(input).toEqual('This Is Some Camel Case String');
    });

    it('Should start with a capital letter', () => {
        // Act
        input = scHumanReadable.transform('someStringStartingSmall');
        // Assert
        void expect(input).toEqual('Some String Starting Small');
    });

    it('Should place a blank between two capital letters', () => {
        // Act
        input = scHumanReadable.transform('ABadExample');
        // Assert
        void expect(input).toEqual('A Bad Example');
    });

    it('Should accept special characters', () => {
        // Act
        input = scHumanReadable.transform('thisIsSomeCamel-Case&/%String');
        // Assert
        void expect(input).toContain(' Some Camel-Case');
        void expect(input).toContain('&/%');
        void expect(input).toContain('String');
    });

    it('Should replace underline with blanks', () => {
        // Act
        input = scHumanReadable.transform('This_may_also_be_acceptable');
        // Assert
        void expect(input).toEqual('This may also be acceptable');
    });

});
