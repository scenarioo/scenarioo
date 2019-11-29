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

declare var angular: angular.IAngularStatic;

angular
    .module('scenarioo.directives')
    .component('scScreenAnnotationsButton', {
        bindings: {
            screenAnnotations: '<',
        },
        template: require('./screenAnnotationsButton.html'),
        controller: screenAnnotationsButton,
    });

function screenAnnotationsButton(LocalStorageService, GlobalHotkeysService) {

    const SCREEN_ANNOTATIONS_VISIBLE_KEY = 'scenarioo-screenAnnotationsVisible';

    function ScreenAnnotationsButtonController($scope) {

        // we should use 'bindToController' here, as soon we upgrade to angular 1.3 or upwards (instead of $scope)

        const vm = this;
        vm.toggleAnnotationsVisible = toggleAnnotationsVisible;
        vm.isAnnotationsButtonVisible = isAnnotationsButtonVisible;

        activate();

        function activate() {
            initAnnotationsVisibleFromLocalStorage();
            GlobalHotkeysService.registerGlobalHotkey('a', vm.toggleAnnotationsVisible);
        }

        function isAnnotationsButtonVisible() {
            return $scope.screenAnnotations != null && $scope.screenAnnotations.length > 0;
        }

        function initAnnotationsVisibleFromLocalStorage() {
            const annotationsVisible = LocalStorageService.get(SCREEN_ANNOTATIONS_VISIBLE_KEY);
            if (annotationsVisible === 'true') {
                $scope.visibilityToggle = true;
            } else if (annotationsVisible === 'false') {
                $scope.visibilityToggle = false;
            } else {
                // default
                $scope.visibilityToggle = true;
            }
        }

        function toggleAnnotationsVisible() {
            $scope.visibilityToggle = !$scope.visibilityToggle;
            LocalStorageService.set(SCREEN_ANNOTATIONS_VISIBLE_KEY, '' + $scope.visibilityToggle);
        }

    }

}
