/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('scenarioo.controllers').controller('ConfigEditorCtrl', function ($scope, BranchesResource, Config) {

    BranchesResource.query({}, function (branches) {
        $scope.branches = branches;
        calculateConfiguredBranch();
    });

    $scope.$on(Config.CONFIG_LOADED_EVENT, function () {
        $scope.configuration = Config.getRawConfigDataCopy();
        calculateConfiguredBranch();
    });

    Config.load();

    function calculateConfiguredBranch() {
        if (angular.isUndefined($scope.branches) || angular.isUndefined($scope.configuration)) {
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
