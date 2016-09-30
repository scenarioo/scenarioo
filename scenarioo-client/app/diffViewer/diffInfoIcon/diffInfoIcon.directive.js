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
    .directive('scDiffInfoIcon', scDiffInfoIcon);

function scDiffInfoIcon() {

    return {
        restrict: 'E',
        scope: {
            diffInfo: '=',
            elementType: '@',
            childElementType: '@'
        },
        templateUrl: 'diffViewer/diffInfoIcon/diffInfoIcon.html',
        controller: DiffInfoIconController,
        controllerAs: 'vm',
        bindToController: true
    };
}
DiffInfoIconController.$inject = ['$scope', '$sce', '$filter'];
function DiffInfoIconController($scope, $sce, $filter) {
    var vm = this;
    vm.changedPercentage = '';
    vm.addedPercentage = '';
    vm.removedPercentage = '';
    vm.unchangedPercentage = '';


    $scope.$watch('vm.diffInfo', function(){
        initValues(vm);
    });

    function initValues(vm) {
        if(vm.diffInfo) {
            vm.changedPercentage = 0 + '%';
            vm.addedPercentage = 0 + '%';
            vm.removedPercentage = 0 + '%';
            vm.unchangedPercentage = 0 + '%';

            if(vm.diffInfo.isAdded) {
                vm.addedPercentage = 100 + '%';
                vm.infoText = $sce.trustAsHtml('This ' + vm.elementType + ' has been added');
            } else if(vm.diffInfo.isRemoved){
                vm.removedPercentage = 100 + '%';
                vm.infoText = $sce.trustAsHtml('This ' + vm.elementType + ' has been removed');
            } else if(vm.diffInfo.changeRate === 0) {
                vm.unchangedPercentage = 100 + '%';
                vm.infoText = $sce.trustAsHtml('This ' + vm.elementType + ' has no changes');
            } else {
                var totalChangedChildElements = vm.diffInfo.added + vm.diffInfo.removed + vm.diffInfo.changed;
                if(totalChangedChildElements && totalChangedChildElements > 0) {
                    var addedPercentage = (vm.diffInfo.added / totalChangedChildElements) * vm.diffInfo.changeRate;
                    var removedPercentage = (vm.diffInfo.removed / totalChangedChildElements) * vm.diffInfo.changeRate;
                    var changedPercentage = vm.diffInfo.changeRate - addedPercentage - removedPercentage;

                    vm.changedPercentage = changedPercentage + '%';
                    vm.addedPercentage = addedPercentage + '%';
                    vm.removedPercentage = removedPercentage + '%';
                    vm.unchangedPercentage = 100 - changedPercentage - addedPercentage - removedPercentage + '%';
                }

                var changedInfoText = buildChangedInfoText(vm.diffInfo, vm.elementType, vm.childElementType);
                vm.infoText = $sce.trustAsHtml(changedInfoText);
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



