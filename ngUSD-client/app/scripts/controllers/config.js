'use strict';

angular.module('ngUSDClientApp.controllers').controller('ConfigCtrl', function ($scope, BranchService, Config, CONFIG_LOADED_EVENT) {

    $scope.$on(CONFIG_LOADED_EVENT, function () {
        $scope.configurableBranches = BranchService.findAllBranches();
        $scope.configuration = Config.getRawConfigDataCopy();

        $scope.configurableBranches.then(function (branches) {
            for (var index = 0; index < branches.length; index++) {
                if (branches[index].branch.name === $scope.configuration.defaultBranchName) {
                    $scope.configuredBranch = branches[index];
                }
            }
        });
    });

    $scope.resetConfiguration = function () {
        $scope.configuration = Config.getRawConfigDataCopy();
    };

    $scope.updateConfiguration = function () {
        $scope.successfullyUpdatedConfiguration = false;

        Config.updateConfiguration($scope.configuration, function () {
            $scope.successfullyUpdatedConfiguration = true;
        });
    };
});