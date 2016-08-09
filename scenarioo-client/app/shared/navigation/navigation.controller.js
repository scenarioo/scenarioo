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

angular.module('scenarioo.controllers').controller('NavigationController', NavigationController);

function NavigationController($scope, $location, LocalStorageService, BranchesAndBuildsService,
                              SelectedBranchAndBuildService, ApplicationInfoPopupService, ConfigService,
                              GlobalHotkeysService, SearchEngineStatusService) {

    $scope.$on(ConfigService.CONFIG_LOADED_EVENT, function () {
        $scope.applicationName = ConfigService.applicationName();
    });

    $scope.$on('branchesUpdated', function () {
        loadBranchesAndBuildsService();
    });

    SelectedBranchAndBuildService.callOnSelectionChange(loadBranchesAndBuildsService);

    $scope.globalSearch = {
        queryString: ''
    };

    $scope.search = function () {
        $location.path('/search').search('q=' + $scope.globalSearch.queryString);
    };

    function loadSearchEngineRunning () {
        SearchEngineStatusService.isSearchEngineRunning().then(function(result) {
            $scope.isSearchEngineRunning = result.searchEngineRunning;
        });
    }

    $scope.isSearchEngineRunning = loadSearchEngineRunning();

    function loadBranchesAndBuildsService() {
        BranchesAndBuildsService.getBranchesAndBuildsService().then(function onSuccess(branchesAndBuilds) {
            $scope.branchesAndBuilds = branchesAndBuilds;
        });
    }

    $scope.setBranch = function (branch) {
        $scope.branchesAndBuilds.selectedBranch = branch;
        LocalStorageService.remove(SelectedBranchAndBuildService.BUILD_KEY);
        $location.search(SelectedBranchAndBuildService.BRANCH_KEY, branch.branch.name);
    };

    $scope.setBuild = function (selectedBranch, build) {
        $scope.branchesAndBuilds.selectedBuild = build;
        $location.search(SelectedBranchAndBuildService.BUILD_KEY, build.linkName);
    };

    $scope.updating = false;

    $scope.getDisplayNameForBuild = function (build, returnShortText) {
        if (angular.isUndefined(build)) {
            return '';
        }

        // The displayName is required for the special "last successful scenarios" build
        if (angular.isDefined(build.displayName) && build.displayName !== null) {
            return build.displayName;
        }

        if ($scope.isBuildAlias(build)) {
            return getDisplayNameForAliasBuild(build, returnShortText);
        } else {
            return build.build.name;
        }
    };

    function getDisplayNameForAliasBuild(build, returnShortText) {
        if (returnShortText) {
            return build.linkName;
        } else {
            return build.linkName + ': ' + build.build.name;
        }
    }

    $scope.getBranchDisplayName = function (wrappedBranch) {

        if (wrappedBranch === undefined) {
            return null;
        }

        var displayName = wrappedBranch.branch.name;
        if (wrappedBranch.alias) {
            displayName = displayName + ' (' + wrappedBranch.branch.description + ')';
        }
        return displayName;
    };

    $scope.isBuildAlias = function (build) {
        if (angular.isUndefined(build)) {
            return false;
        }

        return build.build.name !== build.linkName;
    };

    $scope.isLastSuccessfulScenariosBuild = function (build) {
        if (angular.isUndefined(build)) {
            return false;
        }

        return build.getDisplayNameForBuild === 'last successful scenarios';
    };

    GlobalHotkeysService.registerGlobalHotkey('i', function () {
        $scope.showApplicationInfoPopup();
    });

    $scope.showApplicationInfoPopup = function () {
        ApplicationInfoPopupService.showApplicationInfoPopup();
    };

}
