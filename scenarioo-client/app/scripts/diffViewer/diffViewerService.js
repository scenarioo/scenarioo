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


/**
 *  TODO: mscheube: comment
 */
angular.module('scenarioo.services').factory('DiffViewerService', function ($location, $rootScope, localStorageService, Config, HostnameAndPort) {


    function getDiffScreenShotUrl(step, selected, comparisonName, usecaseName, scenarioName) {

        if (angular.isUndefined(step)) {
            return undefined;
        }

        //TODO: mscheube: diffScreenshotFileName
        //var imageName = $scope.step.stepDescription.screenshotFileName;
        var imageName = "temp.png";

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        return HostnameAndPort.forLink() + 'rest/diffViewer/' + selected.branch + '/'+ selected.build +'/'+ comparisonName +'/'+ usecaseName  +'/'+ scenarioName + '/' + imageName;

    }

    //TODO: mscheube: why does a service need a return value?
    //return{
    //
    // };

    //TODO: copied from selectedBranchAndBuild. Why not integrate in selectedBranchAndBuild service?
    var COMPARISON_KEY = 'comparison';
    var selectedComparison;
    var initialValuesFromUrlAndCookieLoaded = false;
    var selectionChangeCallbacks = [];

    function getSelectedComparison() {
        if (!initialValuesFromUrlAndCookieLoaded) {
            // Here we calculate the selected comparison because
            // it may not yet be calculated because there was no CONFIG_LOADED_EVENT yet.
            calculateSelectedComparison();
            initialValuesFromUrlAndCookieLoaded = true;
        }

        return selectedComparison;
    }

    $rootScope.$on(Config.CONFIG_LOADED_EVENT, function () {
        calculateSelectedComparison();
    });

    function calculateSelectedComparison() {
        selectedComparison = getFromLocalStorageOrUrl(COMPARISON_KEY);
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
        value = Config.defaultBranchAndBuild()[key];
        if (angular.isDefined(value)) {
            localStorageService.set(key, value);
            $location.search(key, value);
        }
        return value;
    }

    $rootScope.$watch(function () {
        return $location.search();
    }, function () {
        calculateSelectedComparison();
    }, true);

    $rootScope.$watch(getSelectedComparison,
        function (selected) {
            if (isComparisonDefined()) {
                for (var i = 0; i < selectionChangeCallbacks.length; i++) {
                    selectionChangeCallbacks[i](selected);
                }
            }
        }, true);

    /**
     * @returns true if comparison is specified (i.e. not 'undefined').
     */
    function isComparisonDefined() {
        return angular.isDefined(selectedComparison);
    }

    function registerSelectionChangeCallback(callback) {
        selectionChangeCallbacks.push(callback);
        var selected = getSelectedComparison();
        if (isComparisonDefined()) {
            callback(selected);
        }
    }

    return {
        COMPARISON_KEY: COMPARISON_KEY,


        /**
         * Returns the Diff Screenshot URL
         */
        getDiffScreenShotUrl: getDiffScreenShotUrl,

        /**
         * Returns the currently selected comparison.
         */
        selected: getSelectedComparison,

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
