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

describe('Filter scDateTime', function () {

    beforeEach(angular.mock.module('scenarioo.filters'));

    var DATE_TIMESTAMP = new Date(2014, 3, 30, 23, 9).getTime();
    var DATE_UNDEFINED;
    var DATE_EMPTY_STRING = '';

    var scDateTime;
    beforeEach(inject(function ($filter) {
        scDateTime = $filter('scDateTime');
    }));

    describe('when a timestamp is filtered', function () {
        it('returns the formatted date and time string', function () {
            expect(scDateTime(DATE_TIMESTAMP)).toEqual('April 30, 2014, 11:09 PM');
        });
    });

    describe('when undefined is filtered', function () {
        it('returns an empty string', function () {
            expect(scDateTime(DATE_UNDEFINED)).toEqual('');
        });
    });

    describe('when an empty string is filtered', function () {
        it('returns an empty string', function () {
            expect(scDateTime(DATE_EMPTY_STRING)).toEqual('');
        });
    });

});
