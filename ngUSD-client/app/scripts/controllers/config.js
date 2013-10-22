'use strict';

angular.module('ngUSDClientApp.controllers').controller('ConfigCtrl', function ($scope, $q, ConfigResource, BranchService, Config) {

    var data = ConfigResource.get();
    $scope.configurableBranches = BranchService.findAllBranches();

    data.then(function (result) {
        $scope.configuration = result;

        $scope.configurableBranches.then(function (branches) {
            for (var index = 0; index < branches.length; index++) {
                if (branches[index].branch.name === $scope.configuration.defaultBranchName) {
                    $scope.configuredBranch = branches[index];
                }
            }
        });
    });

    $scope.resetConfiguration = function () {
        $scope.configuration = ConfigResource.get();
    };

    $scope.updateConfiguration = function () {
        $scope.successfullyUpdatedConfiguration = false;
        $scope.configuration.defaultBranchName = $scope.configuredBranch.branch.name;
        var request = ConfigResource.updateConfiguration($scope.configuration);
        request.then(function () {
            $scope.successfullyUpdatedConfiguration = true;
        });
        Config.updateConfiguration($scope.configuration);
    };
});