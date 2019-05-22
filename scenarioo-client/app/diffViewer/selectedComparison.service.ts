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

angular.module('scenarioo.services').service('SelectedComparison', SelectedComparisonService);

function SelectedComparisonService($location, $rootScope, localStorageService) {
    const COMPARISON_KEY = 'comparison';
    const COMPARISON_DISABLED = 'Disabled';
    let selectedComparison;
    let initialValuesFromUrlAndCookieLoaded = false;
    const info = {isDefined: false};

    function getSelectedComparison() {
        if (!initialValuesFromUrlAndCookieLoaded) {
            // Here we calculate the selected comparison because
            // it may not yet be calculated because there was no CONFIG_LOADED_EVENT yet.
            setSelectedComparison();
            initialValuesFromUrlAndCookieLoaded = true;
        }

        return selectedComparison;
    }

    function setSelectedComparison(value?) {
        if (value) {
            selectedComparison = value;
        } else {
            selectedComparison = getFromLocalStorageOrUrl(COMPARISON_KEY);
        }
        info.isDefined = isComparisonDefined();
    }

    function getFromLocalStorageOrUrl(key) {
        let value;

        // check URL first, this has priority over the cookie value
        const params = $location.search();
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

        // If URL and cookie do not specify a value, we use the disabled as default
        value = COMPARISON_DISABLED;
        localStorageService.set(key, value);
        $location.search(key, value);

        return value;
    }

    $rootScope.$watch(() => $location.search()[COMPARISON_KEY], () => {
        setSelectedComparison();
    }, true);

    /**
     * @returns true if comparison is specified (i.e. not 'undefined').
     */
    function isComparisonDefined() {
        if (angular.isDefined(selectedComparison)) {
            return selectedComparison !== COMPARISON_DISABLED;
        }
        return false;
    }

    return {
        COMPARISON_KEY,
        COMPARISON_DISABLED,

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
         * Returns object including isDefined information
         */
        info,
    };
}
