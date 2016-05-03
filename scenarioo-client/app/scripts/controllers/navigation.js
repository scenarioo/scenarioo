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

angular.module('scenarioo.controllers').controller('NavigationCtrl', function ($scope, $location, localStorageService, BranchesAndBuilds, SelectedBranchAndBuild, $uibModal, ScApplicationInfoPopup, Config, GlobalHotkeysService) {

    $scope.$on(Config.CONFIG_LOADED_EVENT, function () {
        $scope.applicationName = Config.applicationName();
    });

    $scope.$on('branchesUpdated', function () {
        loadBranchesAndBuilds();
    });

    SelectedBranchAndBuild.callOnSelectionChange(loadBranchesAndBuilds);

    function loadBranchesAndBuilds() {
        BranchesAndBuilds.getBranchesAndBuilds().then(function onSuccess(branchesAndBuilds) {
            $scope.branchesAndBuilds = branchesAndBuilds;
        });
    }

    $scope.setBranch = function (branch) {
        $scope.branchesAndBuilds.selectedBranch = branch;
        localStorageService.remove(SelectedBranchAndBuild.BUILD_KEY);
        $location.search(SelectedBranchAndBuild.BRANCH_KEY, branch.branch.name);
    };

    $scope.setBuild = function (selectedBranch, build) {
        $scope.branchesAndBuilds.selectedBuild = build;
        $location.search(SelectedBranchAndBuild.BUILD_KEY, build.linkName);
    };

    $scope.updating = false;

    $scope.getDisplayNameForBuild = function (build, returnShortText) {
        return BranchesAndBuilds.getDisplayNameForBuild(build, returnShortText);
    };

    $scope.getBranchDisplayName = function (wrappedBranch) {
        return BranchesAndBuilds.getBranchDisplayName(wrappedBranch);
    };

    $scope.isBuildAlias = function (build) {
        return BranchesAndBuilds.isBuildAlias(build);
    };

    $scope.isLastSuccessfulScenariosBuild = function (build) {
        return BranchesAndBuilds.isLastSuccessfulScenariosBuild(build);
    };

    GlobalHotkeysService.registerGlobalHotkey('i', function () {
        $scope.showApplicationInfoPopup();
    });

    $scope.showApplicationInfoPopup = function () {
        ScApplicationInfoPopup.showApplicationInfoPopup();
    };

    ScApplicationInfoPopup.showApplicationInfoPopupIfRequired();

});
