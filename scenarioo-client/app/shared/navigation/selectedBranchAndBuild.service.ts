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
import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';
import {ReplaySubject} from 'rxjs';
import {RoutingWrapperService} from '../routing-wrapper.service';

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
                private routingWrapperService: RoutingWrapperService) {
        this.configurationService.getConfiguration().subscribe(() => this.updateSelectedBranchAndBuild());
        // Using wrapper service for Routing, which will eventually implement the Angular Router once migrated
        this.routingWrapperService.onQueryParamsChanged(() => this.updateSelectedBranchAndBuild());
    }

    // TODO: Replace with callOnSelectionChange?
    selected(): SelectedBranchAndBuild {
        if (!this.isDefined()) {
            this.calculateSelectedBranchAndBuild();
        }
        return this.selectedBranchAndBuild;
    }

    // TODO: Return Observable instead
    callOnSelectionChange(callback: any): void {
        this.selectionChange$.subscribe(callback);
    }

    /**
     * @returns true if branch and build are both specified (i.e. not 'undefined').
     */
    isDefined(): boolean {
        return this.selectedBranchAndBuild.branch !== undefined && this.selectedBranchAndBuild.build !== undefined;
    }

    private updateSelectedBranchAndBuild() {
        this.calculateSelectedBranchAndBuild();
        if (this.isDefined()) {
            this.selectionChange$.next(this.selectedBranchAndBuild);
        }
    }

    private calculateSelectedBranchAndBuild() {
        this.selectedBranchAndBuild.branch = this.getFromLocalStorageOrUrl(this.BRANCH_KEY);
        this.selectedBranchAndBuild.build = this.getFromLocalStorageOrUrl(this.BUILD_KEY);
    }

    private getFromLocalStorageOrUrl(key: string) {
        let value: string;

        // check URL first, this has priority over the cookie value
        const params = this.routingWrapperService.getAllQueryParams();
        if (params !== null && params[key] !== undefined) {
            value = params[key];
            this.localStorageService.set(key, value);
            return value;
        }

        // check cookie if value was not found in URL
        value = this.localStorageService.get(key);
        if (value !== undefined && value !== null) {
            this.routingWrapperService.setQueryParam(key, value);
            return value;
        }

        // If URL and cookie do not specify a value, we use the default from the config
        value = this.configurationService.defaultBranchAndBuild()[key];
        if (value !== undefined) {
            this.localStorageService.set(key, value);
            this.routingWrapperService.setQueryParam(key, value);
        }
        return value;
    }

}

angular.module('scenarioo.services')
    .factory('SelectedBranchAndBuildService', downgradeInjectable(SelectedBranchAndBuildService));
