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

import {DateTimePipe} from './dateTime.pipe';

'use strict';

describe('Pipe: scDateTime', () => {
    let scDateTime: DateTimePipe;
    let input;

    // Arrange: set up new instance of the pipe DateTime
    beforeEach(() => {
        scDateTime = new DateTimePipe('en-US');
    });

    it('Undefined returns an empty string', () => {
        // Act
        input = scDateTime.transform(undefined);
        // Assert
        expect(input).toEqual('');
    });

    it('Empty string returns an empty string', () => {
        // Act
        input = scDateTime.transform('');
        // Assert
        expect(input).toEqual('');
    });

    it('Timestamp returns the formatted Date and time string', () => {
        // Act
        input = scDateTime.transform(new Date(2014, 3, 30, 23, 9).getTime());
        // Assert
        expect(input).toEqual('April 30, 2014, 11:09 PM');
    });

});
