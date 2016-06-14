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

angular.module('scenarioo.services').factory('BranchesAndBuildsService', function ($rootScope, ConfigService, BranchesResource, $q, SelectedBranchAndBuildService) {

    var getBranchesAndBuildsData = function () {
        var deferred = $q.defer();
        BranchesResource.query({}, function findSelectedBranchAndBuild(branches) {
            if (branches.length === 0) {
                deferred.reject('Branch list empty!');
                return;
            }

            var loadedData = {
                branches: branches
            };

            if (SelectedBranchAndBuildService.isDefined()) {
                var selected = SelectedBranchAndBuildService.selected();

                loadedData.selectedBranch = getSelectedBranch(loadedData, selected.branch);
                loadedData.selectedBuild = getSelectedBuild(loadedData.selectedBranch, selected.build);
            }

            deferred.resolve(loadedData);
        }, function (error) {
            deferred.reject(error);
        });

        return deferred.promise;
    };

    function getSelectedBranch(loadedData, branchName) {
        var index;
        for (index = 0; index < loadedData.branches.length; index++) {
            if (loadedData.branches[index].branch.name === branchName) {
                return loadedData.branches[index];
            }
        }

    }

    function getSelectedBuild(selectedBranch, buildName) {
        var index;
        if (angular.isDefined(selectedBranch)) {
            var allBuildsOnSelectedBranch = selectedBranch.builds;
            for (index = 0; index < selectedBranch.builds.length; index++) {
                if (allBuildsOnSelectedBranch[index].linkName === buildName) {
                    return allBuildsOnSelectedBranch[index];
                }
            }
        }
    }

    function getBranchDisplayName(wrappedBranch) {

        if (wrappedBranch === undefined) {
            return null;
        }

        var displayName = wrappedBranch.branch.name;
        if (wrappedBranch.alias) {
            displayName = displayName + ' (' + wrappedBranch.branch.description + ')';
        }
        return displayName;
    }

    function getDisplayNameForBuildName(branchName, buildName, returnShortText) {
        var deferred = $q.defer();


        getBranchesAndBuildsData().then(function onSuccess(result){
            var selectedBranch = getSelectedBranch(result, branchName);
            var selectedBuild = getSelectedBuild(selectedBranch, buildName);
            var baseBuildName = getDisplayNameForBuild(selectedBuild, returnShortText);

            deferred.resolve(baseBuildName);
        });


        return deferred.promise;
    }

    function getDisplayNameForBuild(build, returnShortText) {
        if (angular.isUndefined(build)) {
            return '';
        }

        // The displayName is required for the special "last successful scenarios" build
        if (angular.isDefined(build.displayName) && build.displayName !== null) {
            return build.displayName;
        }

        if (isBuildAlias(build)) {
            return getDisplayNameForAliasBuild(build, returnShortText);
        } else {
            return build.build.name;
        }
    }

    function getDisplayNameForAliasBuild(build, returnShortText) {
        if (returnShortText) {
            return build.linkName;
        } else {
            return build.linkName + ': ' + build.build.name;
        }
    }

    function isBuildAlias(build) {
        if (angular.isUndefined(build)) {
            return false;
        }

        return build.build.name !== build.linkName;
    }

    function isLastSuccessfulScenariosBuild(build) {
        if (angular.isUndefined(build)) {
            return false;
        }

        return build.getDisplayNameForBuild === 'last successful scenarios';
    }

    return {
        getBranchesAndBuilds: getBranchesAndBuildsData,
        getBranchDisplayName: getBranchDisplayName,
        isLastSuccessfulScenariosBuild: isLastSuccessfulScenariosBuild,
        isBuildAlias: isBuildAlias,
        getDisplayNameForBuild: getDisplayNameForBuild,
        getDisplayNameForBuildName: getDisplayNameForBuildName
    };
});
