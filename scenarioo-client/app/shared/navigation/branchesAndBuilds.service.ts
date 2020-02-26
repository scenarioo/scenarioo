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

import {Observable} from 'rxjs';
import {IBranchBuilds, IBuildLink} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

angular.module('scenarioo.services')
    .factory('BranchesAndBuildsService', (
        $rootScope, BranchesResource,
        $q, SelectedBranchAndBuildService) => {

        // TODO: Add type for Observable - Needs to include selected branch and build
        const getBranchesAndBuildsData = (): Observable<any> => {
            return new Observable<any>((subscriber) => {
                BranchesResource.query().subscribe((branches) => {
                    if (branches.length === 0) {
                        subscriber.error('Branch list empty!');
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

                    subscriber.next(loadedData);
                }, (error) => {
                    subscriber.error(error);
                });
            });
        };

        // TODO: Set type for loadedData
        function getBranch(loadedData: any, branchName: string): IBranchBuilds {
            return loadedData.branches.find((currentBranch) => currentBranch.branch.name === branchName);
        }

        function getBuild(branch: IBranchBuilds, buildName: string): IBuildLink {
            if (angular.isDefined(branch)) {
                return branch.builds.find((build) => build.linkName === buildName);
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

        function getDisplayNameForBuildName(branchName: string, buildName: string): Observable<string> {
            return new Observable<string>((subscriber) => {
                getBranchesAndBuildsData().subscribe((result) => {
                    const selectedBranch = getBranch(result, branchName);
                    const selectedBuild = getBuild(selectedBranch, buildName);
                    const baseBuildName = getDisplayNameForBuild(selectedBuild, false);

                    subscriber.next(baseBuildName);
                });
            });
        }

        function getDisplayNameForBuild(build: IBuildLink, returnShortText: boolean): string {
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

        function getDisplayNameForAliasBuild(build: IBuildLink, returnShortText: boolean): string {
            if (returnShortText) {
                return build.linkName;
            } else {
                return build.linkName + ': ' + build.build.name;
            }
        }

        function isBuildAlias(build: IBuildLink): boolean {
            if (!build) {
                return false;
            }

            return build.build.name !== build.linkName;
        }

        function isLastSuccessfulScenariosBuild(build: IBuildLink) {
            if (!build) {
                return false;
            }

            return getDisplayNameForBuild(build, false) === 'last successful scenarios';
        }

        function getBuildByName(branchName, buildName): Observable<IBuildLink> {
            return new Observable<any>((subscriber) => {
                getBranchesAndBuildsData().subscribe((result) => {
                    const branch = getBranch(result, branchName);
                    const build = getBuild(branch, buildName);
                    subscriber.next(build);
                });
            });
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

export class BranchesAndBuildsService {
    getBranchesAndBuilds(): Observable<any> {
        return undefined;
    }
}
