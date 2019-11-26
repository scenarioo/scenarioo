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

import encodeUri from '../../../../app/shared/utils/httpUriEncoder';

describe('http url encoder', () => {

    describe('when no special characters are passed', () => {
        it('returns the concatenated string', () => {
            expect(encodeUri(['rest', 'buildImports'])).toEqual('rest/buildImports');
        });
    });

    describe('when special characters are passed', () => {
        it('returns the concatenated url encoded string', () => {
            expect(encodeUri(['rest', 'branch', 'branch name', 'build', 'build name', 'usecase', 'use%20case'])).toEqual('rest/branch/branch%20name/build/build%20name/usecase/use%2520case');
        });
    });

    describe('when a single element with special characters is passed', () => {
        it('returns the encoded string', () => {
            expect(encodeUri(['use%20case'])).toEqual('use%2520case');
        });
    });

});
