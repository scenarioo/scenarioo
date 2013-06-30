'use strict';

NgUsdClientApp.value('Config', {

    buildUrlParameter: "build",
    buildDefaultValue: "current",

    branchUrlParameter: "branch",
    branchDefaultValue: "trunk",

    scenarioPropertiesInOverview: ['userProfile', 'configuration'],

    selectedBuild: function ($location) {
        var params = $location.search();
        if (params != null && params[this.buildUrlParameter] != null ) {
            return params[this.buildUrlParameter];
        } else {
            return this.buildDefaultValue;
        }
    },
    selectedBranch: function ($location) {
        var params = $location.search();
        if (params != null && params[this.branchUrlParameter] != null ) {
            return params[this.branchUrlParameter];
        } else {
            return this.branchDefaultValue;
        }
    }
});