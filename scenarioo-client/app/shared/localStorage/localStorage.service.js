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
    .factory('scLocalStorage', function ($location, scUrlUtil, localStorageService) {

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
         * @param key the key to make scenarioo context aware
         * @returns {string} key with scenarioo context path from location url inside.
         */
        function getScenariooContextPathAwareKey(key) {
            return '/' + scUrlUtil.getContextPathFromUrl($location.absUrl()) + '/' + key;
        }

    });
