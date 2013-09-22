'use strict';

NgUsdClientApp.factory('Config', function (ConfigService, $q) {

    var config = {
        configData: {},
        branchUrlParameter: 'branch',
        buildUrlParameter: 'build',

        updateConfiguration: function (newConfig) {
            if (newConfig) {
                var deferred = $q.defer();
                deferred.resolve(newConfig);
                this.configData = deferred.promise;
            } else {
                this.configData = ConfigService.getConfiguration();
            }
        },

        selectedBuild: function ($location) {
            var params = $location.search();
            if (params !== null && params[this.buildUrlParameter]) {
                return params[this.buildUrlParameter];
            } else {
                return this.defaultBuild();
            }
        },

        selectedBranch: function ($location) {
            var params = $location.search();
            if (params !== null && params[this.branchUrlParameter]) {
                return params[this.branchUrlParameter];
            } else {
                return this.defaultBranch();
            }
        },

        defaultBranch: function () {
            return this.getConfiguration('defaultBranchName');
        },

        defaultBuild: function () {
            return this.getConfiguration('defaultBuildName');
        },

        scenarioPropertiesInOverview: function () {
            return this.getConfigurationAsArray('scenarioPropertiesInOverview');
        },

        applicationInformation: function () {
            return this.getConfiguration('applicationInformation');
        },

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
    };
    config.updateConfiguration();
    return config;
});