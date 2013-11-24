'use strict';

angular.module('scenarioo.controllers').controller('ConfigEditorCtrl', function ($scope, BranchesResource, Config) {

    BranchesResource.query({}, function(branches) {
        $scope.branches = branches;
        calculateConfiguredBranch();
    });

    $scope.$on(Config.CONFIG_LOADED_EVENT, function() {
        $scope.configuration = Config.getRawConfigDataCopy();
        calculateConfiguredBranch();
    });

    Config.load();

    function calculateConfiguredBranch() {
        if(angular.isUndefined($scope.branches) || angular.isUndefined($scope.configuration)) {
            return;
        }

        for (var index = 0; index < $scope.branches.length; index++) {
            if ($scope.branches[index].branch.name === $scope.configuration.defaultBranchName) {
                $scope.configuredBranch = $scope.branches[index];
            }
        }
    }

    $scope.resetConfiguration = function () {
        $scope.configuration = Config.getRawConfigDataCopy();
        calculateConfiguredBranch();
    };

    $scope.updateConfiguration = function () {
        $scope.successfullyUpdatedConfiguration = false;

        Config.updateConfiguration($scope.configuration, function () {
            $scope.successfullyUpdatedConfiguration = true;
        });
    };

});