'use strict';

angular.module('scenarioo.services').factory('SelectedBranchAndBuild', function ($location, $cookieStore, $rootScope, Config) {

    var BRANCH_KEY = 'branch';
    var BUILD_KEY = 'build';

    var selectedBranch;
    var selectedBuild;
    var initialValuesFromUrlAndCookieLoaded = false;
    var selectionChangeCallbacks = [];

    function getSelectedBranchAndBuild() {
        if (!initialValuesFromUrlAndCookieLoaded) {
            // Here we calculate the selected branch and build because
            // it may not yet be calculated because there was no CONFIG_LOADED_EVENT yet.
            calculateSelectedBranchAndBuild();
            initialValuesFromUrlAndCookieLoaded = true;
        }

        return {
            branch: selectedBranch,
            build: selectedBuild
        };
    }

    $rootScope.$on(Config.CONFIG_LOADED_EVENT, function () {
        calculateSelectedBranchAndBuild();
    });

    function calculateSelectedBranchAndBuild() {
        selectedBranch = getFromCookieOrUrl(BRANCH_KEY);
        selectedBuild = getFromCookieOrUrl(BUILD_KEY);
    }

    function getFromCookieOrUrl(key) {
        var value;

        // check URL first, this has priority over the cookie value
        var params = $location.search();
        if (params !== null && angular.isDefined(params[key])) {
            value = params[key];
            $cookieStore.put(key, value);
            return value;
        }

        // check cookie if value was not found in URL
        value = $cookieStore.get(key);
        if (angular.isDefined(value)) {
            $location.search(key, value);
            return value;
        }

        // If URL and cookie do not specify a value, we use the default from the config
        value = Config.defaultBranchAndBuild()[key];
        if (angular.isDefined(value)) {
            $cookieStore.put(key, value);
            $location.search(key, value);
        }
        return value;
    }

    $rootScope.$watch(function () {
        return $location.search();
    }, function () {
        calculateSelectedBranchAndBuild();
    }, true);

    $rootScope.$watch(getSelectedBranchAndBuild,
        function (selected) {
            if (isBranchAndBuildDefined()) {
                for (var i = 0; i < selectionChangeCallbacks.length; i++) {
                    selectionChangeCallbacks[i](selected);
                }
            }
        }, true);

    /**
     * @returns true if branch and build are both specified (i.e. not 'undefined').
     */
    function isBranchAndBuildDefined() {
        return angular.isDefined(selectedBranch) && angular.isDefined(selectedBuild);
    }

    function registerSelectionChangeCallback(callback) {
        selectionChangeCallbacks.push(callback);
        var selected = getSelectedBranchAndBuild();
        if (isBranchAndBuildDefined()) {
            callback(selected);
        }
    }

    return {
        BRANCH_KEY: BRANCH_KEY,
        BUILD_KEY: BUILD_KEY,

        /**
         * Returns the currently selected branch and build as a map with the keys 'branch' and 'build'.
         */
        selected: getSelectedBranchAndBuild,

        /**
         * Returns true only if both values (branch are build) are defined.
         */
        isDefined: isBranchAndBuildDefined,

        /**
         * This method lets you register callbacks that get called, as soon as a new and also valid build and branch
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