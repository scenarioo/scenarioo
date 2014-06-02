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

angular.module('scenarioo.filters').filter('scDateTime', function ($filter) {

    var DATE_FILTER = $filter('date');

    return function (date) {
        if(typeof date === 'undefined') {
            return '';
        }
        if(typeof date === 'string' && date === '') {
            return '';
        }

        return DATE_FILTER(date, 'longDate') + ', ' + DATE_FILTER(date, 'shortTime');
    };

});
