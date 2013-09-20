'use strict';

NgUsdClientApp.controller('ConfigCtrl', function ($scope, $q, ConfigService, BranchService, Config) {

    var data = ConfigService.getConfiguration();
    $scope.configurableBranches = BranchService.findAllBranches();

    data.then(function(result) {
        $scope.configuration = result;

        $scope.configurableBranches.then(function(branches) {
            for (var index=0; index<branches.length; index++) {
                if (branches[index].branch.name === $scope.configuration.defaultBranchName) {
                    $scope.configuredBranch = branches[index];
                }
            }
        });
    });

    $scope.resetConfiguration = function() {
        $scope.configuration = ConfigService.getConfiguration();
    };

    $scope.updateConfiguration = function(){
        $scope.successfullyUpdatedConfiguration = false;
        $scope.configuration.defaultBranchName = $scope.configuredBranch.branch.name;
        var request = ConfigService.updateConfiguration($scope.configuration);
        request.then(function() {
            $scope.successfullyUpdatedConfiguration = true;
        });
        Config.updateConfiguration($scope.configuration);
    };
});