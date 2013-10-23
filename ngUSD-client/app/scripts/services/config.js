
'use strict';

angular.module('ngUSDClientApp.services').constant('CONFIG_LOADED_EVENT', 'configLoaded');

angular.module('ngUSDClientApp.services').service('Config', function (CONFIG_LOADED_EVENT, ConfigResource, $rootScope, $location, $cookieStore) {

    var configData = {},
        BRANCH_URL_PARAMETER = 'branch',
        BUILD_URL_PARAMETER = 'build',
        CONFIG_KEY_SELECTED_BRANCH = 'selectedBranch',
        CONFIG_KEY_SELECTED_BUILD = 'selectedBuild';

    function getValue(key) {
        if (angular.isUndefined(configData[key])) {
            throw 'ngUSDerror :: Key ' + key + ' not present in configData';
        }
        return configData[key];
    }

    function doLoad() {
        ConfigResource.get({}, function (response) {
            configData = postProcessConfigData(response);
            watchLocationChanges();
            $rootScope.$broadcast(CONFIG_LOADED_EVENT);
        });
    }

    function postProcessConfigData(configDataFromServer) {
        var postProcessedData = configDataFromServer;
        postProcessedData[CONFIG_KEY_SELECTED_BUILD] = getConfiguredBuild(configDataFromServer);
        postProcessedData[CONFIG_KEY_SELECTED_BRANCH] = getConfiguredBranch(configDataFromServer);
        return postProcessedData;
    }

    function getConfiguredBuild(configData) {

        // check URL
        var params = $location.search();
        if (params !== null && params[BUILD_URL_PARAMETER]) {
            return params[BUILD_URL_PARAMETER];
        }

        // check cookie
        var buildCookieValue = $cookieStore.get(BUILD_URL_PARAMETER);
        if (angular.isDefined(buildCookieValue)) {
            return buildCookieValue;
        }

        // else, take default
        return configData.defaultBuildName;
    }

    function getConfiguredBranch(configData) {

        // check URL
        var params = $location.search();
        if (params !== null && params[BRANCH_URL_PARAMETER]) {
            return params[BRANCH_URL_PARAMETER];
        }

        // check cookie
        var branchCookieValue = $cookieStore.get(BRANCH_URL_PARAMETER);
        if (angular.isDefined(branchCookieValue)) {
            return branchCookieValue;
        }

        // else, take default
        return configData.defaultBranchName;
    }

    function watchLocationChanges() {
        $rootScope.$watch(function () {
            return $location.search();
        }, function (newValue) {
            configData = postProcessConfigData(configData);
        });
    }

    var serviceInstance = {
        BRANCH_URL_PARAMETER: BRANCH_URL_PARAMETER,
        BUILD_URL_PARAMETER: BUILD_URL_PARAMETER,


        getRawConfigDataCopy: function () {
            return angular.copy(configData);
        },

        /**
         * Will fire event 'configLoaded'
         */
        load: function () {
            doLoad();
        },

        isLoaded: function () {
            return angular.isDefined(configData.defaultBuildName);
        },

        updateConfiguration: function (newConfig, successCallback) {
            // TODO

            if (successCallback) {
                successCallback();
            }
        },

        selectedBuild: function () {
            return  getValue(CONFIG_KEY_SELECTED_BUILD);
        },

        selectedBranch: function () {
            return getValue(CONFIG_KEY_SELECTED_BRANCH);
        },

        scenarioPropertiesInOverview: function () {
            return getValue('scenarioPropertiesInOverview');
        },

        applicationInformation: function () {
            return getValue('applicationInformation');
        }

    };

    return serviceInstance;
});