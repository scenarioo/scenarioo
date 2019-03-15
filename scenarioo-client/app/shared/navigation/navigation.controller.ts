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

angular.module('scenarioo.controllers')
    .controller('NavigationController', NavigationController);

function NavigationController($scope, $location, LocalStorageService, BranchesAndBuildsService,
                              SelectedBranchAndBuildService, SelectedComparison, ApplicationInfoPopupService,
                              NewConfigService,
                              GlobalHotkeysService,
                              BuildDiffInfosResource,
                              SearchEngineStatusService) {

    NewConfigService.configLoaded$.subscribe(() => {
        $scope.applicationName = NewConfigService.applicationName();
    });

    $scope.$on('branchesUpdated', () => {
        loadBranchesAndBuilds();
    });

    $scope.COMPARISON_DISABLED = SelectedComparison.COMPARISON_DISABLED;
    $scope.comparisonInfo = SelectedComparison.info;

    SelectedBranchAndBuildService.callOnSelectionChange(loadBranchesAndBuilds);

    $scope.globalSearch = {
        queryString: '',
    };

    $scope.search = () => {
        const searchTerm = $scope.globalSearch.queryString;

        // If the search term is blank nothing happens
        if (!angular.isString(searchTerm) || searchTerm.trim() === '') {
            return;
        }

        // Since angular odes not support encoded slashes in routes, we have to encode it twice.
        // See https://github.com/angular/angular.js/issues/10479
        const searchUrl = '/search/' + encodeURIComponent(encodeURIComponent(searchTerm));
        $location.url(searchUrl);
    };

    function loadSearchEngineRunning() {
        SearchEngineStatusService.isSearchEngineRunning().subscribe((result) => {
            $scope.isSearchEngineRunning = result.running;
        });
    }

    $scope.isSearchEngineRunning = loadSearchEngineRunning();

    function loadBranchesAndBuilds() {
        BranchesAndBuildsService.getBranchesAndBuilds().then((branchesAndBuilds) => {
            $scope.branchesAndBuilds = branchesAndBuilds;
            loadComparisonBuilds();
        });
    }

    function loadComparisonBuilds() {
        if ($scope.branchesAndBuilds && $scope.branchesAndBuilds.selectedBranch && $scope.branchesAndBuilds.selectedBuild) {
            const baseBranchName = $scope.branchesAndBuilds.selectedBranch.branch.name;
            const baseBuildName = $scope.branchesAndBuilds.selectedBuild.linkName;
            BuildDiffInfosResource.query(
                {baseBranchName, baseBuildName},
                (buildDiffInfos) => {
                    $scope.comparisonBuilds = buildDiffInfos;
                    const preSelectedComparison = SelectedComparison.selected();
                    let selectedComparison = {name: SelectedComparison.COMPARISON_DISABLED, changeRate: 0};
                    angular.forEach($scope.comparisonBuilds, (comparisonBuild) => {
                        if (comparisonBuild.name === preSelectedComparison) {
                            selectedComparison = comparisonBuild;
                        }
                    });
                    SelectedComparison.setSelected(selectedComparison.name);
                    if (selectedComparison.name === SelectedComparison.COMPARISON_DISABLED) {
                        $location.search(SelectedComparison.COMPARISON_KEY, selectedComparison.name);
                    }
                    $scope.selectedComparison = selectedComparison;
                }, () => {
                    resetComparisonSelection();
                },
            );
        } else {
            resetComparisonSelection();
        }
    }

    function resetComparisonSelection() {
        $scope.comparisonBuilds = [];
        SelectedComparison.setSelected(SelectedComparison.COMPARISON_DISABLED);
        $location.search(SelectedComparison.COMPARISON_KEY, SelectedComparison.COMPARISON_DISABLED);
        $scope.selectedComparison = undefined;
    }

    $scope.setBranch = (branch) => {
        $scope.branchesAndBuilds.selectedBranch = branch;
        LocalStorageService.remove(SelectedBranchAndBuildService.BUILD_KEY);
        LocalStorageService.remove(SelectedComparison.COMPARISON_KEY);
        $location.search(SelectedBranchAndBuildService.BRANCH_KEY, branch.branch.name);
    };

    $scope.setBuild = (selectedBranch, build) => {
        $scope.branchesAndBuilds.selectedBuild = build;
        LocalStorageService.remove(SelectedComparison.COMPARISON_KEY);
        $location.search(SelectedBranchAndBuildService.BUILD_KEY, build.linkName);
        loadComparisonBuilds();
    };

    $scope.setComparisonBuild = (comparisonBuild) => {
        $location.search(SelectedComparison.COMPARISON_KEY, comparisonBuild.name);
        $scope.selectedComparison = comparisonBuild;
    };

    $scope.disableComparison = () => {
        const disabledComparison = {name: SelectedComparison.COMPARISON_DISABLED, changeRate: 0};
        $scope.setComparisonBuild(disabledComparison);
    };

    $scope.updating = false;

    GlobalHotkeysService.registerGlobalHotkey('i', () => {
        $scope.showApplicationInfoPopup();
    });

    $scope.showApplicationInfoPopup = () => {
        ApplicationInfoPopupService.showApplicationInfoPopup();
    };

}
