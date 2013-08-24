'use strict';

NgUsdClientApp.factory('Config', function (ConfigService, $q) {

    var buildDefaultValue= "current";

    var branchDefaultValue= "trunk";

    var scenarioPropertiesInOverview= ['userProfile', 'configuration'];

    var Config = {
        configData: {},
        branchUrlParameter: "branch",
        buildUrlParameter: "build",

        updateConfiguration: function (data) {
            if (!data) {
                this.configData = ConfigService.getConfiguration();
            } else {
                this.configData = data;
            }
        },

        selectedBuild: function ($location) {
            var params = $location.search();
            if (params != null && params[this.buildUrlParameter] != null ) {
                var deferred = $q.defer();
                deferred.resolve(params[this.buildUrlParameter]);
                return deferred.promise;
            } else {
                return this.defaultBuild();
            }
        },
        selectedBranch: function ($location) {
            var params = $location.search();
            if (params != null && params[this.branchUrlParameter] != null ) {
                var deferred = $q.defer();
                deferred.resolve(params[this.branchUrlParameter]);
                return deferred;
            } else {
                return this.defaultBranch();
            }
        },

        defaultBranch: function() {
            return this.getConfiguration("defaultBranchName");
        },

        defaultBuild: function() {
            return this.getConfiguration("defaultBuildName");
        },


        getConfiguration: function(property) {
            return this.configData.then(function (result) {
                return eval("result."+property);
            });
        }
    };
    Config.updateConfiguration();
    return Config;
});