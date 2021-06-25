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

import {Injectable} from '@angular/core';
import {LocationService} from '../shared/location.service';
import {RootScopeService} from '../shared/rootScope.service';
import {LocalStorageService} from '../services/localStorage.service';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

/**
 * Service to access current selected comparison.
 */
@Injectable()
export class SelectedComparisonService {

    COMPARISON_URL_PARAM_KEY: string = 'comparison';

    COMPARISON_DISABLED: string = 'Disabled';

    private selectedComparison: string;

    private initialValuesFromUrlAndCookieLoaded: boolean = false;

    info: DiffInfo = {isDefined: false};

    constructor(private locationService: LocationService,
                private rootScope: RootScopeService,
                private localStorageService: LocalStorageService) {
        // TODO Angular-Migration: listen to the URL change in pure Angular way ?? (probably only doable once we introduce the router properly)
        rootScope.$watch(() => locationService.search()[this.COMPARISON_URL_PARAM_KEY], () => {
            this.setSelected();
        }, true);
    }

    selected = () => {
        if (!this.initialValuesFromUrlAndCookieLoaded) {
            // Here we calculate the selected comparison because
            // it may not yet be calculated because there was no CONFIG_LOADED_EVENT yet.
            this.setSelected();
            this.initialValuesFromUrlAndCookieLoaded = true;
        }
        return this.selectedComparison;
    }

    setSelected = (value?) => {
        if (value) {
            this.selectedComparison = value;
        } else {
            this.selectedComparison = this.getFromLocalStorageOrUrl(this.COMPARISON_URL_PARAM_KEY);
        }
        this.info.isDefined = this.isDefined();
    }

    private getFromLocalStorageOrUrl = (key) => {
        let value;

        // 1. take param from URL (if available)
        const params = this.locationService.search();
        if (params !== null && angular.isDefined(params[key])) {
            value = params[key];
            this.localStorageService.set(key, value);
            return value;
        }

        // 2. Otherwise read from local storage
        value = this.localStorageService.get(key);
        if (angular.isDefined(value) && value !== null) {
            this.locationService.search(key, value);
            return value;
        }

        // 3. Set default value if both not specified
        value = this.COMPARISON_DISABLED;
        this.localStorageService.set(key, value);
        this.locationService.search(key, value);
        return value;
    }

    isDefined = () => {
        if (angular.isDefined(this.selectedComparison)) {
            return this.selectedComparison !== this.COMPARISON_DISABLED;
        }
        return false;
    }

}

export interface DiffInfo {

    isDefined: boolean;

    // more if it is defined ...
}

angular.module('scenarioo.services')
    .factory('SelectedComparisonService', downgradeInjectable(SelectedComparisonService));
