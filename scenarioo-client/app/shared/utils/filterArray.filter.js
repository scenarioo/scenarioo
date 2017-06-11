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

angular.module('scenarioo.filters').filter('scFilterArray', function () {

    function contains(haystack, needle) {
        return haystack.toLowerCase().indexOf(needle.toLowerCase()) > -1;
    }

    function objectContainsAllSearchElements(object, filterString, excludedPropertiesString) {
        var searchElements = filterString.split(' ');
        var excludedProperties = excludedPropertiesString.split(' ');
        excludedProperties.push('$$hashKey');
        for (var i in searchElements) {
            if (typeof searchElements[i] === 'string') {
                if (!objectContainsString(object, searchElements[i], excludedProperties)) {
                    return false;
                }
            }
        }
        return true;
    }

    function objectContainsString(object, string, excluded) {
        var returnTrue = false;
        var searchedKeys = [];
        angular.forEach(object, function (property, key) {
            if (excluded.indexOf(key) !== -1){
                return;
            }
            searchedKeys.push(key);
            if (!returnTrue) {
                if (typeof property === 'string') {
                    if (contains(property, string)) {
                        returnTrue = true;
                    }
                } else {
                    returnTrue = objectContainsString(property, string, excluded);
                }
            }
        });
        return returnTrue;
    }

    return function (array, filterString, excludedProperties) {

        if (!angular.isArray(array)) {
            return array;
        }
        if (angular.isUndefined(filterString) || typeof filterString !== 'string') {
            return array;
        }

        if (angular.isUndefined(excludedProperties) || typeof excludedProperties !== 'string') {
            excludedProperties = '';
        }

        var filteredModel = [];

        angular.forEach(array, function (arrayElement) {
            if (typeof arrayElement === 'object') {
                if (objectContainsAllSearchElements(arrayElement, filterString, excludedProperties)) {
                    filteredModel.push(arrayElement);
                }
            }
        });

        return filteredModel;
    };

});
