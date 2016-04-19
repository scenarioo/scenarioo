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

angular.module('scenarioo.services')
    .factory('scUrlUtil', function () {

        return {
            getContextPathFromUrl: getContextPathFromUrl
        };

        /**
         * @param url the current value of $location.absUrl()
         */
        function getContextPathFromUrl(url) {
            var contextPath = after(after(before(url, '#'), '//'), '/', '');
            return contextPath.replace(/(^\/)|(\/$)/g, '');
        }

        function before(string, separator, notFoundText) {
            var index = string.indexOf(separator);
            if (index >= 0) {
                return string.substring(0, index);
            }
            else {
                return (notFoundText !== undefined) ? notFoundText : string;
            }
        }

        function after(string, separator, notFoundText) {
            var index = string.indexOf(separator);
            if (index >= 0) {
                return string.substring(index + separator.length, string.length);
            }
            else {
                return (notFoundText !== undefined) ? notFoundText : string;
            }
        }

    });
