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

import {Url} from './url';

describe('Url', () => {

    describe('encodeComponents', () => {

        it('returns the non-special characters as they are', () => {
            const branch = 'abc';
            const build = 'def';
            const comparison = 'foo';

            const result = Url.encodeComponents`rest/builds/${branch}/${build}/comparisons/${comparison}/calculate`;

            expect(result).toBe('rest/builds/abc/def/comparisons/foo/calculate');
        });

        it('returns the url ending with a parameter correctly', () => {
            const branch = 'abc';
            const build = 'def';

            const result = Url.encodeComponents`rest/builds/${branch}/${build}`;

            expect(result).toBe('rest/builds/abc/def');
        });

        it('returns the url starting with a parameter correctly', () => {
            const branch = 'abc';
            const build = 'def';

            const result = Url.encodeComponents`${branch}/${build}/`;

            expect(result).toBe('abc/def/');
        });

        it('returns the url with encoded special characters', () => {
            const branch = 'Üb€rBr@nche$ & Bu!lds (+special chars)';
            const build = '#1';

            const result = Url.encodeComponents`rest/builds/${branch}/${build}`;

            expect(result).toBe('rest/builds/%C3%9Cb%E2%82%ACrBr%40nche%24%20%26%20Bu!lds%20(%2Bspecial%20chars)/%231');
        });
    });
});
