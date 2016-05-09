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
         * Extract the context path where the app is running form any URL (without domain name, protocol, port or any hash or parameters behind)
         * @param url the current value of $location.absUrl()
         */
        function getContextPathFromUrl(url) {
            var urlWithoutParamsOrHash = before(before(url, '?'), '#');
            var contextPath = after(after(urlWithoutParamsOrHash, '//'), '/', '');
            return contextPath.replace(/(^\/)|(\/$)/g, '');  // trim leading or trailing slashes
        }

        function before(string, separator, optionalNotFoundResult) {
            var index = string.indexOf(separator);
            if (index >= 0) {
                return string.substring(0, index);
            }
            else {
                return (optionalNotFoundResult !== undefined) ? optionalNotFoundResult : string;
            }
        }

        function after(string, separator, optionalNotFoundResult) {
            var index = string.indexOf(separator);
            if (index >= 0) {
                return string.substring(index + separator.length, string.length);
            }
            else {
                return (optionalNotFoundResult !== undefined) ? optionalNotFoundResult : string;
            }
        }

    });
