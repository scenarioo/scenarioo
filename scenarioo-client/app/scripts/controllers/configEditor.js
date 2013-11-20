'use strict';

angular.module('ngUSDClientApp.controllers').controller('ConfigCtrl', function ($scope, BranchService, Config) {

    $scope.$watch(function () {
        return Config.getRawConfigDataCopy();
    }, function (configData) {
        $scope.configuration = configData;

        $scope.configurableBranches.then(function (branches) {
            for (var index = 0; index < branches.length; index++) {
                if (branches[index].branch.name === $scope.configuration.defaultBranchName) {
                    $scope.configuredBranch = branches[index];
                }
            }
        });
    }, true);

    $scope.configurableBranches = BranchService.findAllBranches();

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