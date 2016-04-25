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

angular.module('scenarioo.directives').directive('scDiffInfoIcon', function() {

    function initCssClass(scope, diffInfo) {
        if(diffInfo) {
            if(diffInfo.isAdded) {
                scope.cssClass = 'icon-plus';
            } else if(diffInfo.isRemoved){
                scope.cssClass = 'icon-minus';
            } else if(diffInfo.changeRate === 0) {
                scope.cssClass = 'icon-ok';
            } else {
                scope.cssClass = 'icon-exclamation';
            }
        }
    }

    return {
        restrict: 'E',
        scope: {
            diffInfo: '='
        },
        templateUrl: 'views/diffInfoIcon.html',
        controller: function($scope) {
            $scope.$watch('diffInfo', function(value){
                initCssClass($scope, value);
            });
        }
    };
});
