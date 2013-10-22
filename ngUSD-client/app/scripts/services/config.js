'use strict';


angular.module('ngUSDClientApp.services').service('Config', function ($location,ConfigResource, $rootScope) {

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
            $rootScope.$broadcast('configLoaded');
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
        // TODO


        // else, take default
        return configData.defaultBuildName;
    }

    function getConfiguredBranch(configData) {

        // check URL
        var params =  $location.search();
        if (params !== null && params[BRANCH_URL_PARAMETER]) {
            return params[BRANCH_URL_PARAMETER];
        }

        // check cookie
        // TODO

        // else, take default
        return configData.defaultBranchName;
    }

    /*var config : {
     getConfiguration: function (property) {
     return this.configData.then(function (result) {
     return result[property];
     });
     },

     getConfigurationAsArray: function (property) {
     return this.getConfiguration(property).then(function (result) {
     var array = result.split(',');
     for (var index = 0; index < array.length; index++) {
     array[index] = array[index].replace(/^\s+|\s+$/g, '');
     }
     return array;
     });
     }
     };*/

    var serviceInstance = {

        /**
         * Will fire event 'configLoaded'
         */
        load: function () {
            doLoad();
        },

        updateConfiguration: function (newConfig) {
            // TODO
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