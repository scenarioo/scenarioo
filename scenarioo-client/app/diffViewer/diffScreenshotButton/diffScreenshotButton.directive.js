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

angular.module('scenarioo.directives').directive('scDiffScreenshotButton', scDiffScreenshotButton);

function scDiffScreenshotButton() {

    var directive = {
        restrict: 'E',
        scope: {
            linkingVariable: '=',
            localStorageKey: '@'
        },
        templateUrl: 'diffViewer/diffScreenshotButton/diffScreenshotButton.html',
        controller: controller
    };
    return directive;

    function controller($scope, localStorageService){

        var STEP_DIFF_SCREENSHOT_VISIBLE = 'scenarioo-diffScreenshotVisible-';

        initDiffScreenshotVisibleFromLocalStorage($scope, $scope.localStorageKey);

        $scope.toggleShowingDiffScreenshot = function() {
            $scope.linkingVariable = !$scope.linkingVariable;
            localStorageService.set(STEP_DIFF_SCREENSHOT_VISIBLE + $scope.localStorageKey, '' + $scope.linkingVariable);
        };

        function initDiffScreenshotVisibleFromLocalStorage(scope, key) {
            var diffScreenshotVisible = localStorageService.get(STEP_DIFF_SCREENSHOT_VISIBLE + key);
            if (diffScreenshotVisible === 'false') {
                scope.linkingVariable = false;
            } else {
                scope.linkingVariable = true;
            }
        }
    }
}
