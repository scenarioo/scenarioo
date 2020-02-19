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
import {Location} from '@angular/common';
import {Injectable, OnInit} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

angular.module('scenarioo.services')
    .factory('SelectedBranchAndBuildService', ($location, $rootScope, localStorageService: LocalStorageService, ConfigurationService: ConfigurationService) => {

        const BRANCH_KEY: string = 'branch';
        const BUILD_KEY: string = 'build';

        let selectedBranch;
        let selectedBuild;
        let initialValuesFromUrlAndCookieLoaded: boolean = false;
        const selectionChangeCallbacks = [];

        function getSelectedBranchAndBuild(): SelectedBranchAndBuild {
            if (!initialValuesFromUrlAndCookieLoaded) {
                // Here we calculate the selected branch and build because
                // it may not yet be calculated because there was no CONFIG_LOADED_EVENT yet.
                calculateSelectedBranchAndBuild();
                initialValuesFromUrlAndCookieLoaded = true;
            }

            return {
                branch: selectedBranch,
                build: selectedBuild,
            };
        }

        // TODO: Subscribe in ngOnInit in new Angular service
        ConfigurationService.getConfiguration().subscribe(calculateSelectedBranchAndBuild);

        function calculateSelectedBranchAndBuild() {
            selectedBranch = getFromLocalStorageOrUrl(BRANCH_KEY);
            selectedBuild = getFromLocalStorageOrUrl(BUILD_KEY);
        }

        function getFromLocalStorageOrUrl(key: string) {
            let value;

            // check URL first, this has priority over the cookie value
            const params = $location.search();
            if (params !== null && params[key] !== undefined) {
                value = params[key];
                localStorageService.set(key, value);
                return value;
            }

            // check cookie if value was not found in URL
            value = localStorageService.get(key);
            if (value !== undefined && value !== null) {
                $location.search(key, value);
                return value;
            }

            // If URL and cookie do not specify a value, we use the default from the config
            value = ConfigurationService.defaultBranchAndBuild()[key];
            if (value !== undefined) {
                localStorageService.set(key, value);
                $location.search(key, value);
            }
            return value;
        }

        // LYBO: Listen for any changes to the URL?
        // Maybe this could be replaced with an Observable?
        $rootScope.$watch(() => $location.search(), () => {
            calculateSelectedBranchAndBuild();
        }, true);

        $rootScope.$watch(getSelectedBranchAndBuild,
            (selected) => {
                if (isBranchAndBuildDefined()) {
                    for (const selectionChangeCallback of selectionChangeCallbacks) {
                        selectionChangeCallback(selected);
                    }
                }
            }, true);

        /**
         * @returns true if branch and build are both specified (i.e. not 'undefined').
         */
        function isBranchAndBuildDefined() {
            return selectedBranch !== undefined && selectedBuild !== undefined;
        }

        function registerSelectionChangeCallback(callback) {
            addCallback(selectionChangeCallbacks, callback);
            const selected = getSelectedBranchAndBuild();
            if (isBranchAndBuildDefined()) {
                callback(selected);
            }
        }

        function addCallback(callbackList, newCallback) {
            let callbackListContainsNewCallback = false;
            angular.forEach(callbackList, (callback) => {
                if (callback.toString() === newCallback.toString()) {
                    callbackListContainsNewCallback = true;
                }
            });
            if (!callbackListContainsNewCallback) {
                callbackList.push(newCallback);
            }
        }

        return {
            BRANCH_KEY,
            BUILD_KEY,

            /**
             * Returns the currently selected branch and build as a map with the keys 'branch' and 'build'.
             */
            selected: getSelectedBranchAndBuild,

            /**
             * Returns true only if both values (branch and build) are defined.
             */
            isDefined: isBranchAndBuildDefined,

            /**
             * This method lets you register callbacks that get called, as soon as a new and also valid build and branch
             * selection is available. The callback is called with the new selection as a parameter.
             *
             * Note these special cases:
             * - If there is already a valid selection available (i.e. branch and build are both defined), the callback
             *   is called immediately when it is registered.
             * - If the selection changes to an invalid selection (e.g. branch is defined, but build is undefined),
             *   the callback is not called.
             */
            callOnSelectionChange: registerSelectionChangeCallback,
        };

    });

export class SelectedBranchAndBuildService {
    selected(): string {
        return '';
    }

    callOnSelectionChange(fn: any){
    }

}

@Injectable()
// tslint:disable-next-line:max-classes-per-file
export class SelectedBranchAndBuildService2 {
    readonly BRANCH_KEY: string = 'branch';
    readonly BUILD_KEY: string = 'build';

    private selectedBranchAndBuild: SelectedBranchAndBuild = {
        branch: '',
        build: '',
    };
    private initialValuesFromUrlAndCookieLoaded: boolean = false;
    // TODO: Add type for functions?
    private selectionChangeCallbacks: any[] = [];

    constructor(private localStorageService: LocalStorageService, private configurationService: ConfigurationService,
                private locationService: LocationService, private location: Location) {
        this.configurationService.getConfiguration().subscribe(() => this.calculateSelectedBranchAndBuild);
        this.location.subscribe(() => this.calculateSelectedBranchAndBuild());
    }

    // TODO: Rename appropriately once fully migrated (getSelectedBranchAndBuild)?
    selected(): SelectedBranchAndBuild {
        return this.getSelectedBranchAndBuild();
    }

    callOnSelectionChange(callback: any): void {
        this.registerSelectionChangeCallback(callback);
    }

    private getSelectedBranchAndBuild(): SelectedBranchAndBuild {
        if (!this.initialValuesFromUrlAndCookieLoaded) {
            // Here we calculate the selected branch and build because
            // it may not yet be calculated because there was no CONFIG_LOADED_EVENT yet.
            this.calculateSelectedBranchAndBuild();
            this.initialValuesFromUrlAndCookieLoaded = true;
        }

        // TODO: Verify if this works (above is a subscribe to the getSelectedBranchAndBuild function)
        if (this.isDefined()) {
            for (const selectionChangeCallback of this.selectionChangeCallbacks) {
                selectionChangeCallback(this.selectedBranchAndBuild);
            }
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

    private registerSelectionChangeCallback(callback) {
        this.addCallback(this.selectionChangeCallbacks, callback);
        const selected = this.getSelectedBranchAndBuild();
        if (this.isDefined()) {
            callback(selected);
        }
    }

    private addCallback(callbackList, newCallback) {
        callbackList.forEach((callback) => {
            if (callback.toString() === newCallback.toString()) {
                return;
            }
        });
        callbackList.push(newCallback);
    }

    // TODO: Rename appropriately once migrated (isBranchAndBuildDefined)?
    /**
     * @returns true if branch and build are both specified (i.e. not 'undefined').
     */
    isDefined(): boolean {
        return this.selectedBranchAndBuild.branch !== undefined && this.selectedBranchAndBuild.build !== undefined;
    }

}

angular.module('scenarioo.services')
    .factory('SelectedBranchAndBuildService2', downgradeInjectable(SelectedBranchAndBuildService2));
