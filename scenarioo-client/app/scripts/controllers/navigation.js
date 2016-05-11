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

angular.module('scenarioo.controllers').controller('NavigationCtrl', function ($scope, $location, localStorageService, BranchesAndBuilds, SelectedBranchAndBuild, SelectedComparison, $uibModal, ScApplicationInfoPopup, Config, GlobalHotkeysService, BuildDiffInfosResource) {

    $scope.$on(Config.CONFIG_LOADED_EVENT, function () {
        $scope.applicationName = Config.applicationName();
    });

    $scope.$on('branchesUpdated', function () {
        loadBranchesAndBuilds();
    });

    $scope.COMPARISON_DISABLED = SelectedComparison.COMPARISON_DISABLED;
    $scope.comparisonInfo = SelectedComparison.info;

    SelectedBranchAndBuild.callOnSelectionChange(loadBranchesAndBuilds);

    function loadBranchesAndBuilds() {
        BranchesAndBuilds.getBranchesAndBuilds().then(function onSuccess(branchesAndBuilds) {
            $scope.branchesAndBuilds = branchesAndBuilds;
            loadComparisonBuilds();
        });
    }

    function loadComparisonBuilds() {
        if($scope.branchesAndBuilds && $scope.branchesAndBuilds.selectedBranch && $scope.branchesAndBuilds.selectedBuild) {
            var baseBranchName = $scope.branchesAndBuilds.selectedBranch.branch.name;
            var baseBuildName = $scope.branchesAndBuilds.selectedBuild.linkName;
            BuildDiffInfosResource.query(
                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName},
                function onSuccess(buildDiffInfos) {
                    $scope.comparisonBuilds = buildDiffInfos;
                    var preSelectedComparison = SelectedComparison.selected();
                    var selectedComparison = { name: SelectedComparison.COMPARISON_DISABLED, changeRate: 0 };
                    angular.forEach($scope.comparisonBuilds, function(comparisonBuild) {
                        if(comparisonBuild.name === preSelectedComparison) {
                            selectedComparison = comparisonBuild;
                        }
                    });
                    SelectedComparison.setSelected(selectedComparison.name);
                    if(selectedComparison.name === SelectedComparison.COMPARISON_DISABLED) {
                        $location.search(SelectedComparison.COMPARISON_KEY, selectedComparison.name);
                    }
                    $scope.selectedComparison = selectedComparison.name;
                    $scope.selectedChangeRate = selectedComparison.changeRate;
                }
            );
        }
    }

    $scope.setBranch = function (branch) {
        $scope.branchesAndBuilds.selectedBranch = branch;
        localStorageService.remove(SelectedBranchAndBuild.BUILD_KEY);
        localStorageService.remove(SelectedComparison.COMPARISON_KEY);
        $location.search(SelectedBranchAndBuild.BRANCH_KEY, branch.branch.name);
    };

    $scope.setBuild = function (selectedBranch, build) {
        $scope.branchesAndBuilds.selectedBuild = build;
        localStorageService.remove(SelectedComparison.COMPARISON_KEY);
        $location.search(SelectedBranchAndBuild.BUILD_KEY, build.linkName);
        loadComparisonBuilds($scope.branchesAndBuilds.selectedBranch.branch.name, $scope.branchesAndBuilds.selectedBuild.linkName);
    };

    $scope.setComparisonBuild = function(comparisonBuild) {
        $location.search(SelectedComparison.COMPARISON_KEY, comparisonBuild.name);
        $scope.selectedComparison = comparisonBuild.name;
        $scope.selectedChangeRate = comparisonBuild.changeRate;
    };

    $scope.disableComparison = function() {
        var disabledComparison = { name: SelectedComparison.COMPARISON_DISABLED, changeRate: 0 };
        $scope.setComparisonBuild(disabledComparison);
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
