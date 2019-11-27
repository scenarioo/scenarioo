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

declare var angular: angular.IAngularStatic;

angular.module('scenarioo.services')
    .factory('SelectedBranchAndBuildService', ($location, $rootScope, localStorageService: LocalStorageService, ConfigurationService: ConfigurationService) => {

        const BRANCH_KEY = 'branch';
        const BUILD_KEY = 'build';

        let selectedBranch;
        let selectedBuild;
        let initialValuesFromUrlAndCookieLoaded = false;
        const selectionChangeCallbacks = [];

        function getSelectedBranchAndBuild() {
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

        ConfigurationService.getConfiguration().subscribe(calculateSelectedBranchAndBuild);

        function calculateSelectedBranchAndBuild() {
            selectedBranch = getFromLocalStorageOrUrl(BRANCH_KEY);
            selectedBuild = getFromLocalStorageOrUrl(BUILD_KEY);
        }

        function getFromLocalStorageOrUrl(key: string) {
            let value;

            // check URL first, this has priority over the cookie value
            const params = $location.search();
            if (params !== null && angular.isDefined(params[key])) {
                value = params[key];
                localStorageService.set(key, value);
                return value;
            }

            // check cookie if value was not found in URL
            value = localStorageService.get(key);
            if (angular.isDefined(value) && value !== null) {
                $location.search(key, value);
                return value;
            }

            // If URL and cookie do not specify a value, we use the default from the config
            value = ConfigurationService.defaultBranchAndBuild()[key];
            if (angular.isDefined(value)) {
                localStorageService.set(key, value);
                $location.search(key, value);
            }
            return value;
        }

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
            return angular.isDefined(selectedBranch) && angular.isDefined(selectedBuild);
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

// TODO: this service should be really upgraded to an injectable service and then be downgraded for AngularJS and it should use real types (instead of any!)
export class SelectedBranchAndBuildService {
    callOnSelectionChange(fn: any) {

    }

    selected(): any {
        return '';
    }
}
