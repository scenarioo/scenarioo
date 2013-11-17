'use strict';

angular.module('ngUSDClientApp.services').factory('SelectedBranchAndBuild', function ($location, $cookieStore, $rootScope, Config) {

    var BRANCH_KEY = 'branch',
        BUILD_KEY = 'build',
        CONFIG_KEY_SELECTED_BRANCH = 'selectedBranch',
        CONFIG_KEY_SELECTED_BUILD = 'selectedBuild';

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
        // check URL first, this has priority over the cookie value
        var params = $location.search();
        if (params !== null && angular.isDefined(params[key])) {
            var value = params[key];
            $cookieStore.put(key, value)
            return value;
        }

        // check cookie if value was not found in URL
        var value = $cookieStore.get(key);
        if (angular.isDefined(value)) {
            $location.search(key, value);
            return value;
        }

        // If URL and cookie do not specify a value, we use the default from the config
        var value = Config.defaultBranchAndBuild()[key];
        if(angular.isDefined(value)) {
            $cookieStore.put(key, value);
            $location.search(key, value);
        }
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

    return {
        BRANCH_KEY: BRANCH_KEY,
        BUILD_KEY: BUILD_KEY,

        selected: function () {
            return getSelectedBranchAndBuild();
        },

        isDefined: function() {
            return isBranchAndBuildDefined();
        },

        /**
         * This method should be used by all code that is interested in changes of the selected
         * branch and build.
         *
         * The callback is called with the new selection as a parameter.
         */
        callOnSelectionChange: function (callback) {
            selectionChangeCallbacks.push(callback);
            var selected = getSelectedBranchAndBuild();
            if (isBranchAndBuildDefined()) {
                callback(selected);
            }
        }
    };

})
;