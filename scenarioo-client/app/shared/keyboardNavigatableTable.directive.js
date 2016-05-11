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


/**
 * this directive provides keyboard navigation features for a table.
 * Up/Down arrows will navigate through the rows. on hitting the enter key, a callback is invoked, and
 * the currently 'selected' row-item is passed to the callback function.
 * (note: use the css class 'selected' to visually mark the currently selected table row element)
 *
 * use:
 *

 <table sc-keyboard-navigatable-table="filtered" sc-keyboard-navigatable-table-hit="myVM.myCallback" >
 <thead>
 <tr>
 <th>Col1</th>
 <th>Col2</th>
 </tr>
 </thead>
 <tbody>
 <tr ng-class="{'selected':$index==selectedRowIndex}"  ng-repeat="item in filtered=(data)">
 <td>{{item.prop1}}</td>
 <td>{{item.prop2}}</td>
 </tr>
 </tbody>
 </table>

 *
 * define the hit-callback in your controller:
 *

 vm.myCallback = function (item) {
     // do something with item
 };

 *
 */
angular.module('scenarioo.directives').directive('scKeyboardNavigatableTable', function ($parse, GlobalHotkeysService) {
    return {
        restrict: 'A',
        scope: {
            scKeyboardNavigatableTable: '@',
            scKeyboardNavigatableTableHit: '='
        },
        link: function (scope) {
            var
                parentScope = scope.$parent,
                currentCollection,
                currentCollectionLength;

            parentScope.selectedRowIndex = 0;


            scope.$watchCollection(function () {
                return parentScope[scope.scKeyboardNavigatableTable];
            }, function (collection) {
                parentScope.selectedRowIndex = 0;
                currentCollection = collection;
                if (angular.isDefined(collection)) {
                    currentCollectionLength = collection.length;
                } else {
                    currentCollectionLength = 0;
                }
            });

            function selectPreviousRow() {
                if (parentScope.selectedRowIndex > 0) {
                    parentScope.selectedRowIndex--;
                }
            }

            function selectNextRow() {
                if (parentScope.selectedRowIndex < (currentCollectionLength - 1)) {
                    parentScope.selectedRowIndex++;
                }
            }


            function invokeCallback() {
                if (angular.isUndefined(currentCollection) || currentCollectionLength < 1) {
                    return;
                }

                if (typeof scope.scKeyboardNavigatableTableHit === 'function') {
                    scope.scKeyboardNavigatableTableHit.call(parentScope, currentCollection[parentScope.selectedRowIndex]);
                }
            }

            GlobalHotkeysService.registerPageHotkeyCode(38, function () {
                // up arrow
                selectPreviousRow();
            });
            GlobalHotkeysService.registerPageHotkeyCode(40, function () {
                // down arrow
                selectNextRow();
            });
            GlobalHotkeysService.registerPageHotkeyCode(13, function () {
                // enter
                invokeCallback();
            });

        }
    };
});
