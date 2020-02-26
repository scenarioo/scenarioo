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
import {Injectable} from '@angular/core';
import {BranchesResource} from '../services/branchesResource.service';
import {SelectedBranchAndBuildService} from './selectedBranchAndBuild.service';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BranchesAndBuildsService {
    constructor(private BranchesResource: BranchesResource,
                private SelectedBranchAndBuildService: SelectedBranchAndBuildService) {
    }

    getBranchesAndBuilds(): Observable<any> {
        return new Observable<any>((subscriber) => {
            this.BranchesResource.query().subscribe((branches) => {
                if (branches.length === 0) {
                    subscriber.error('Branch list empty!');
                    return;
                }

                const loadedData: any = {
                    branches,
                };

                if (this.SelectedBranchAndBuildService.isDefined()) {
                    const selected = this.SelectedBranchAndBuildService.selected();

                    loadedData.selectedBranch = this.getBranch(loadedData, selected.branch);
                    loadedData.selectedBuild = this.getBuildFromBranch(loadedData.selectedBranch, selected.build);
                }

                subscriber.next(loadedData);
            }, (error) => {
                subscriber.error(error);
            });
        });
    }

    // TODO: Set type for loadedData
    private getBranch(loadedData: any, branchName: string): IBranchBuilds {
        return loadedData.branches.find((currentBranch) => currentBranch.branch.name === branchName);
    }

    private getBuildFromBranch(branch: IBranchBuilds, buildName: string): IBuildLink {
        if (branch !== undefined) {
            return branch.builds.find((build) => build.linkName === buildName);
        }
    }

    getBranchDisplayName(wrappedBranch): string {
        if (!wrappedBranch) {
            return null;
        }

        let displayName = wrappedBranch.branch.name;
        if (wrappedBranch.isAlias) {
            displayName = displayName + ' (' + wrappedBranch.branch.description + ')';
        }
        return displayName;
    }

    getDisplayNameForBuildName(branchName: string, buildName: string): Observable<string> {
        return new Observable<string>((subscriber) => {
            this.getBranchesAndBuilds().subscribe((result) => {
                const selectedBranch = this.getBranch(result, branchName);
                const selectedBuild = this.getBuildFromBranch(selectedBranch, buildName);
                const baseBuildName = this.getDisplayNameForBuild(selectedBuild, false);

                subscriber.next(baseBuildName);
            });
        });
    }

    getDisplayNameForBuild(build: IBuildLink, returnShortText: boolean): string {
        if (!build) {
            return '';
        }

        // The displayName is required for the special "last successful scenarios" build
        if (build.displayName !== undefined && build.displayName !== null) {
            return build.displayName;
        }

        if (this.isBuildAlias(build)) {
            return this.getDisplayNameForAliasBuild(build, returnShortText);
        } else {
            return build.build.name;
        }
    }

    private getDisplayNameForAliasBuild(build: IBuildLink, returnShortText: boolean): string {
        if (returnShortText) {
            return build.linkName;
        } else {
            return build.linkName + ': ' + build.build.name;
        }
    }

    isBuildAlias(build: IBuildLink): boolean {
        if (!build) {
            return false;
        }

        return build.build.name !== build.linkName;
    }

    isLastSuccessfulScenariosBuild(build: IBuildLink) {
        if (!build) {
            return false;
        }

        return this.getDisplayNameForBuild(build, false) === 'last successful scenarios';
    }

    getBuild(branchName: string, buildName: string): Observable<IBuildLink> {
        return new Observable<any>((subscriber) => {
            this.getBranchesAndBuilds().subscribe((result) => {
                const branch = this.getBranch(result, branchName);
                const build = this.getBuildFromBranch(branch, buildName);
                subscriber.next(build);
            });
        });
    }
}

angular.module('scenarioo.services')
    .factory('BranchesAndBuildsService', downgradeInjectable(BranchesAndBuildsService));
