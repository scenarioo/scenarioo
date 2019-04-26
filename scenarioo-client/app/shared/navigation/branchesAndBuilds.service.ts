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

angular.module('scenarioo.services')
    .factory('BranchesAndBuildsService', ($rootScope, ConfigService, BranchesResource, $q, SelectedBranchAndBuildService) => {

        const getBranchesAndBuildsData = () => {
            const deferred = $q.defer();
            BranchesResource.query().subscribe((branches) => {
                if (branches.length === 0) {
                    deferred.reject('Branch list empty!');
                    return;
                }

                const loadedData: any = {
                    branches,
                };

                if (SelectedBranchAndBuildService.isDefined()) {
                    const selected = SelectedBranchAndBuildService.selected();

                    loadedData.selectedBranch = getBranch(loadedData, selected.branch);
                    loadedData.selectedBuild = getBuild(loadedData.selectedBranch, selected.build);
                }

                deferred.resolve(loadedData);
            }, (error) => {
                deferred.reject(error);
            });

            return deferred.promise;
        };

        function getBranch(loadedData, branchName) {
            let index;
            for (index = 0; index < loadedData.branches.length; index++) {
                if (loadedData.branches[index].branch.name === branchName) {
                    return loadedData.branches[index];
                }
            }

        }

        function getBuild(branch, buildName) {
            let index;
            if (angular.isDefined(branch)) {
                const allBuildsOnSelectedBranch = branch.builds;
                for (index = 0; index < branch.builds.length; index++) {
                    if (allBuildsOnSelectedBranch[index].linkName === buildName) {
                        return allBuildsOnSelectedBranch[index];
                    }
                }
            }
        }

        function getBranchDisplayName(wrappedBranch) {

            if (!wrappedBranch) {
                return null;
            }

            let displayName = wrappedBranch.branch.name;
            if (wrappedBranch.isAlias) {
                displayName = displayName + ' (' + wrappedBranch.branch.description + ')';
            }
            return displayName;
        }

        function getDisplayNameForBuildName(branchName, buildName) {
            const deferred = $q.defer();

            getBranchesAndBuildsData().then((result) => {
                const selectedBranch = getBranch(result, branchName);
                const selectedBuild = getBuild(selectedBranch, buildName);
                const baseBuildName = getDisplayNameForBuild(selectedBuild, false);

                deferred.resolve(baseBuildName);
            });

            return deferred.promise;
        }

        function getDisplayNameForBuild(build, returnShortText) {
            if (!build) {
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
            if (!build) {
                return false;
            }

            return build.build.name !== build.linkName;
        }

        function isLastSuccessfulScenariosBuild(build) {
            if (!build) {
                return false;
            }

            return build.getDisplayNameForBuild === 'last successful scenarios';
        }

        function getBuildByName(branchName, buildName) {
            const deferred = $q.defer();

            getBranchesAndBuildsData().then((result) => {
                const branch = getBranch(result, branchName);
                const build = getBuild(branch, buildName);
                deferred.resolve(build);
            });

            return deferred.promise;
        }

        return {
            getBranchesAndBuilds: getBranchesAndBuildsData,
            getBranchDisplayName,
            isLastSuccessfulScenariosBuild,
            isBuildAlias,
            getDisplayNameForBuild,
            getDisplayNameForBuildName,
            getBuild: getBuildByName,
        };
    });
