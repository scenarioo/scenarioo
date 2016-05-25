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

angular.module('scenarioo.directives').directive('scDiffInfoIcon', scDiffInfoIcon);

function scDiffInfoIcon() {

    var directive = {
        restrict: 'E',
        scope: {
            diffInfo: '=',
            elementType: '@',
            childElementType: '@'
        },
        templateUrl: 'diffViewer/diffInfoIcon/diffInfoIcon.html',
        controller: controller
    };
    return directive;

    function controller($scope, $sce, $filter) {
        $scope.$watch('diffInfo', function(){
            initValues($scope);
        });

        function initValues(scope) {
            if(scope.diffInfo) {
                scope.changedPercentage = 0 + '%';
                scope.addedPercentage = 0 + '%';
                scope.removedPercentage = 0 + '%';
                scope.unchangedPercentage = 0 + '%';

                if(scope.diffInfo.isAdded) {
                    scope.addedPercentage = 100 + '%';
                    scope.infoText = $sce.trustAsHtml('This ' + scope.elementType + ' has been added');
                } else if(scope.diffInfo.isRemoved){
                    scope.removedPercentage = 100 + '%';
                    scope.infoText = $sce.trustAsHtml('This ' + scope.elementType + ' has been removed');
                } else if(scope.diffInfo.changeRate === 0) {
                    scope.unchangedPercentage = 100 + '%';
                    scope.infoText = $sce.trustAsHtml('This ' + scope.elementType + ' has no changes');
                } else {
                    var totalChangedChildElements = scope.diffInfo.added + scope.diffInfo.removed + scope.diffInfo.changed;
                    if(totalChangedChildElements && totalChangedChildElements > 0) {
                        var addedPercentage = (scope.diffInfo.added / totalChangedChildElements) * scope.diffInfo.changeRate;
                        var removedPercentage = (scope.diffInfo.removed / totalChangedChildElements) * scope.diffInfo.changeRate;
                        var changedPercentage = scope.diffInfo.changeRate - addedPercentage - removedPercentage;

                        scope.changedPercentage = changedPercentage + '%';
                        scope.addedPercentage = addedPercentage + '%';
                        scope.removedPercentage = removedPercentage + '%';
                        scope.unchangedPercentage = 100 - changedPercentage - addedPercentage - removedPercentage + '%';
                    }

                    var changedInfoText = buildChangedInfoText(scope.diffInfo, scope.elementType, scope.childElementType);
                    scope.infoText = $sce.trustAsHtml(changedInfoText);
                }
            }
        }

        function buildChangedInfoText(diffInfo, elementType, childElementType) {
            var changedInfoText = $filter('number')(diffInfo.changeRate, 0) + '% of this ' + elementType + ' has changed:';
            if(diffInfo.changed > 0) {
                changedInfoText += '<br />';
                changedInfoText += '<span class="square changed"></span>';
                changedInfoText += diffInfo.changed + ' ' + childElementType + (diffInfo.changed === 1 ? '' : 's') + ' changed';
            }
            if(diffInfo.added > 0) {
                changedInfoText += '<br />';
                changedInfoText += '<span class="square added"></span>';
                changedInfoText += diffInfo.added + ' ' + childElementType + (diffInfo.added === 1 ? '' : 's') + ' added';
            }
            if(diffInfo.removed > 0) {
                changedInfoText += '<br />';
                changedInfoText += '<span class="square removed"></span>';
                changedInfoText += diffInfo.removed + ' ' + childElementType + (diffInfo.removed === 1 ? '' : 's') + ' removed';
            }
            return changedInfoText;
        }
    }
}


