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

angular.module('scenarioo.directives').directive('scSortableColumn', function ($compile) {
    var sortableDescriptionObject = {
        restrict: 'A',
        scope: true,
        link: function (scope, element, attrs) {
            scope.$watch(attrs.scSortableColumn, function () {
                var scSortableColumn = attrs.scSortableColumn;
                if (!scSortableColumn) {
                    return;
                }
                element.addClass('link');

                var iconString = '<i class="icon-sort" ng-show="table.sort.column != \'' + scSortableColumn + '\'"></i>' +
                    '<i class="icon-sort-up" ng-show="table.sort.column == \'' + scSortableColumn + '\' && !table.sort.reverse"></i>' +
                    '<i class="icon-sort-down" ng-show="table.sort.column == \'' + scSortableColumn + '\' && table.sort.reverse"></i>';

                var iconElement = angular.element(iconString);
                var iconCompiled = $compile(iconElement)(scope);
                element.append('&nbsp;&nbsp;');
                element.append(iconCompiled);
                element.bind('click', function () {
                    if (!scope.table.sort) {
                        scope.table.sort = {};
                    }
                    var changed = scope.table.sort.column !== scSortableColumn;
                    if (changed) {
                        scope.table.sort.column = scSortableColumn;
                    }
                    if (changed) {
                        scope.table.sort.reverse = false;
                    } else {
                        scope.table.sort.reverse = !scope.table.sort.reverse;
                    }
                    scope.$apply();
                });
            });
        }
    };
    return sortableDescriptionObject;
});
