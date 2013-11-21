'use strict';

angular.module('scenarioo.services').service('ConfigMock', function () {

    return {
        branch: undefined,
        build: undefined,
        appInfo: undefined,

        isLoaded: function () {
            return angular.isDefined(this.build) && angular.isDefined(this.build);
        },

        selectedBranch: function () {
            return this.branch;
        },

        selectedBuild: function () {
            return this.build;
        },

        selectedBuildAndBranch: function () {
            return {
                branch: this.selectedBranch(),
                build: this.selectedBuild()
            }
        },

        applicationInformation: function() {
            return this.appInfo;
        },

        setSelectedBranch: function (branch) {
            this.branch = branch;
        },

        setSelectedBuild: function (build) {
            this.build = build;
        },

        setApplicationInformation: function(applicationInformation) {
            this.appInfo = applicationInformation;
        },

        scenarioPropertiesInOverview: function () {
            return [
                {
                    text: 'User Profile',
                    property: 'details.properties.userProfile',
                    attr: 'userProfile'
                }
            ];
        }
    };

});