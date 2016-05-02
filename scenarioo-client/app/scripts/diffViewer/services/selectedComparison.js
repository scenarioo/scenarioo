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

angular.module('scenarioo.services').factory('SelectedComparison', function ($location, $rootScope, localStorageService, Config) {
    var COMPARISON_KEY = 'comparison';
    var DEFAULT_COMPARISON = 'none';
    var selectedComparison;
    var initialValuesFromUrlAndCookieLoaded = false;
    var selectionChangeCallbacks = [];

    function getSelectedComparison() {
        if (!initialValuesFromUrlAndCookieLoaded) {
            // Here we calculate the selected comparison because
            // it may not yet be calculated because there was no CONFIG_LOADED_EVENT yet.
            setSelectedComparison();
            initialValuesFromUrlAndCookieLoaded = true;
        }

        return selectedComparison;
    }

    function setSelectedComparison(value) {
        if(value) {
            selectedComparison = value;
        }else {
            selectedComparison = getFromLocalStorageOrUrl(COMPARISON_KEY);
        }
    }

    function getFromLocalStorageOrUrl(key) {
        var value;

        // check URL first, this has priority over the cookie value
        var params = $location.search();
        if (params !== null && angular.isDefined(params[key])) {
            value = params[key];
            localStorageService.set(key, value);
            return value;
        }

        // check cookie if value was not found in URL
        value = localStorageService.get(key);
        if (angular.isDefined(value) && value !== null) {
            $location.search(key, value);
            return value;
        }

        // If URL and cookie do not specify a value, we use the default from the config
        value = DEFAULT_COMPARISON;
        localStorageService.set(key, value);
        $location.search(key, value);

        return value;
    }

    $rootScope.$watch(function () {
        return $location.search()[COMPARISON_KEY];
    }, function () {
        setSelectedComparison();
    }, true);

    $rootScope.$watch(getSelectedComparison,
       function (selected) {
           if (angular.isDefined(selectedComparison)) {
               for (var i = 0; i < selectionChangeCallbacks.length; i++) {
                   selectionChangeCallbacks[i](selected);
               }
           }
       }, true);

    /**
     * @returns true if comparison is specified (i.e. not 'undefined').
     */
    function isComparisonDefined() {
        if (angular.isDefined(selectedComparison)){
                return selectedComparison !== DEFAULT_COMPARISON;
        }
        return false;
    }

    function registerSelectionChangeCallback(callback) {
       selectionChangeCallbacks.push(callback);
       var selected = getSelectedComparison();
       if (angular.isDefined(selectedComparison)) {
           callback(selected);
       }
    }


    return {
        COMPARISON_KEY: COMPARISON_KEY,
        DEFAULT_COMPARISON: DEFAULT_COMPARISON,

        /**
         * Returns the currently selected comparison.
         */
        selected: getSelectedComparison,

        /**
         * Setter for selected comparison.
         */
        setSelected: setSelectedComparison,

        /**
         * Returns true only if comparison value is defined.
         */
        isDefined: isComparisonDefined,

        /**
         * This method lets you register callbacks that get called, as soon as a new and also valid comparison
         * selection is available. The callback is called with the new selection as a parameter.
         *
         * Note these special cases:
         * - If there is already a valid selection available (i.e. branch and build are both defined), the callback
         *   is called immediately when it is registered.
         * - If the selection changes to an invalid selection (e.g. branch is defined, but build is undefined),
         *   the callback is not called.
         */
        callOnSelectionChange: registerSelectionChangeCallback
    };

});
