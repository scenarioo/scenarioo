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

angular.module('scenarioo.directives').directive('scMetaDataButton', ($window, LocalStorageService, GlobalHotkeysService) => {

    const METADATA_VISIBLE_KEY = 'scenarioo-metadataVisible-';
    function initMetadataVisibleFromLocalStorage(scope, key) {
        const metadataVisible = LocalStorageService.get(getLocalStorageKey(key));
        if (metadataVisible === 'true') {
            scope.linkingVariable = true;
        } else if (metadataVisible === 'false') {
            scope.linkingVariable = false;
        } else {
            // default
            scope.linkingVariable = $window.innerWidth > 800;
        }
    }
    function getLocalStorageKey(key) {
        return METADATA_VISIBLE_KEY + key;
    }

    return {
        restrict: 'E',
        scope: {
            linkingVariable: '=',
            localStorageKey: '@',
        },
        template: require('./metaDataButton.html'),
        link(scope: any) {
            GlobalHotkeysService.registerGlobalHotkey('d', () => {
                scope.toggleShowingMetadata();
            });
        },
        controller($scope, $route) {
            initMetadataVisibleFromLocalStorage($scope, $scope.localStorageKey);
            $scope.toggleShowingMetadata = () => {
                $scope.linkingVariable = !$scope.linkingVariable;
                LocalStorageService.set(getLocalStorageKey($scope.localStorageKey), '' + $scope.linkingVariable);
                // if toggleShowingMetadata is triggered on the step page we have to potentially reload the page.
                // Delegate this decision to the StepController
                if (angular.isFunction($route.current.scope.refreshIfComparisonActive)) {
                    $route.current.scope.refreshIfComparisonActive();
                }
            };
        },
    };

});
