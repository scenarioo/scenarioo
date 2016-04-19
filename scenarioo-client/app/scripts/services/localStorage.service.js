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
    .factory('scLocalStorage', function ($location, localStorageService) {

        return {
            get: get,
            set: set,
            clearAll: localStorageService.clearAll,
            remove: remove
        };

        function get(key) {
            return localStorageService.get(getScenariooContextPathAwareKey(key));
        }

        function set(key, value) {
            return localStorageService.set(getScenariooContextPathAwareKey(key), value);
        }

        function remove(key) {
            return localStorageService.remove(getScenariooContextPathAwareKey(key));
        }

        /**
         * This code is currently duplicated with test/protractorE2E/util/util.js and needs to be kept in synch for stable e2e tests!
         * TODO: refactor to reusable code
         * @param key the key to make scenarioo context aware
         * @returns {string} key with scenarioo context form location url inside.
         */
        function getScenariooContextPathAwareKey(key) {
            var contextPath = after(after(before($location.absUrl(), '#'), '//'), '/', '');
            contextPath = contextPath.replace(/(^\/)|(\/$)/g, '');
            return '/' + contextPath + '/' + key;
        }

        function before(string, separator, notFoundText) {
            var index = string.indexOf(separator);
            if (index >= 0) {
                return string.substring(0, index);
            }
            else {
                return notFoundText ? notFoundText : string;
            }
        }

        function after(string, separator, notFoundText) {
            var index = string.indexOf(separator);
            if (index >= 0) {
                return string.substring(index + separator.length, string.length);
            }
            else {
                return notFoundText ? notFoundText : string;
            }
        }

    });
