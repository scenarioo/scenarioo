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

import {ConfigurationService} from '../../services/configuration.service';
import {LocalStorageService} from '../../services/localStorage.service';
import {SelectedBranchAndBuild} from './SelectedBranchAndBuild';
import {LocationService} from '../location.service';
import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';
import {RootScopeService} from '../rootScope.service';
import {ReplaySubject, Subject} from 'rxjs';

declare var angular: angular.IAngularStatic;

@Injectable()
export class SelectedBranchAndBuildService {
    readonly BRANCH_KEY: string = 'branch';
    readonly BUILD_KEY: string = 'build';

    private selectedBranchAndBuild: SelectedBranchAndBuild = {
        branch: undefined,
        build: undefined,
    };
    private selectionChange$ = new ReplaySubject<SelectedBranchAndBuild>(1);

    constructor(private localStorageService: LocalStorageService, private configurationService: ConfigurationService,
                private locationService: LocationService, private rootScopeService: RootScopeService) {
        this.configurationService.getConfiguration().subscribe(() => this.calculateSelectedBranchAndBuild());
        this.rootScopeService.$watch(() => this.locationService.search(), () => this.calculateSelectedBranchAndBuild(), true);
        this.rootScopeService.$watch(() => this.getSelectedBranchAndBuild(),
            () => {
                if (this.isDefined()) {
                    this.selectionChange$.next(this.selectedBranchAndBuild);
                }
            }, true);
    }

    // TODO: Rename appropriately once fully migrated (getSelectedBranchAndBuild)?
    selected(): SelectedBranchAndBuild {
        return this.getSelectedBranchAndBuild();
    }

    callOnSelectionChange(callback: any): void {
        this.selectionChange$.subscribe(callback);
    }

    private getSelectedBranchAndBuild(): SelectedBranchAndBuild {
        // Still need to find an optimal solution...
        // Option: Initialize the branch and build e.g. in the constructor, so it definitely has a value, then use the get function for returning an Observable
        if (!this.isDefined()) {
            this.calculateSelectedBranchAndBuild();
        }
        return this.selectedBranchAndBuild;
    }

    private calculateSelectedBranchAndBuild() {
        this.selectedBranchAndBuild.branch = this.getFromLocalStorageOrUrl(this.BRANCH_KEY);
        this.selectedBranchAndBuild.build = this.getFromLocalStorageOrUrl(this.BUILD_KEY);
    }

    private getFromLocalStorageOrUrl(key: string) {
        let value: string;

        // check URL first, this has priority over the cookie value
        const params = this.locationService.search();
        if (params !== null && params[key] !== undefined) {
            value = params[key];
            this.localStorageService.set(key, value);
            return value;
        }

        // check cookie if value was not found in URL
        value = this.localStorageService.get(key);
        if (value !== undefined && value !== null) {
            this.locationService.search(key, value);
            return value;
        }

        // If URL and cookie do not specify a value, we use the default from the config
        value = this.configurationService.defaultBranchAndBuild()[key];
        if (value !== undefined) {
            this.localStorageService.set(key, value);
            this.locationService.search(key, value);
        }
        return value;
    }

    /**
     * @returns true if branch and build are both specified (i.e. not 'undefined').
     */
    isDefined(): boolean {
        return this.selectedBranchAndBuild.branch !== undefined && this.selectedBranchAndBuild.build !== undefined;
    }

}

angular.module('scenarioo.services')
    .factory('SelectedBranchAndBuildService', downgradeInjectable(SelectedBranchAndBuildService));
