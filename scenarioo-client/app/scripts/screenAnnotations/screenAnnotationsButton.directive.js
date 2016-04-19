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

angular
    .module('scenarioo.directives')
    .directive('scScreenAnnotationsButton', screenAnnotationsButton);

function screenAnnotationsButton(scLocalStorage, GlobalHotkeysService) {

    var SCREEN_ANNOTATIONS_VISIBLE_KEY = 'scenarioo-screenAnnotationsVisible';

    return {
        restrict: 'E',
        scope: {
            screenAnnotations: '=',
            visibilityToggle: '='
        },
        templateUrl: 'template/screenAnnotationsButton.html',
        controller: ScreenAnnotationsButtonController,
        controllerAs: 'screenAnnotationsButton'
    };

    function ScreenAnnotationsButtonController($scope) {

        // we should use 'bindToController' here, as soon we upgrade to angular 1.3 or upwards (instead of $scope)

        var vm = this;
        vm.toggleAnnotationsVisible = toggleAnnotationsVisible;

        activate();

        function activate() {
            initAnnotationsVisibleFromLocalStorage();
            GlobalHotkeysService.registerGlobalHotkey('a', vm.toggleAnnotationsVisible);
        }

        function initAnnotationsVisibleFromLocalStorage() {
            var annotationsVisible = scLocalStorage.get(SCREEN_ANNOTATIONS_VISIBLE_KEY);
            if (annotationsVisible === 'true') {
                $scope.visibilityToggle = true;
            }
            else if (annotationsVisible === 'false') {
                $scope.visibilityToggle = false;
            } else {
                // default
                $scope.visibilityToggle = true;
            }
        }

        function toggleAnnotationsVisible() {
            $scope.visibilityToggle = !$scope.visibilityToggle;
            scLocalStorage.set(SCREEN_ANNOTATIONS_VISIBLE_KEY, '' + $scope.visibilityToggle);
        }

    }


}
