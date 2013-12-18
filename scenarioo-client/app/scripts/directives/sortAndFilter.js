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

'use strict';

angular.module('scenarioo.directives').directive('sortandfilter', function ($compile) {
    var sortableDescriptionObject = {
        restrict: 'A',
        scope: true,
        link: function (scope, element, attrs) {
            scope.$watch(attrs.sortandfilter, function () {
                var sortAndFilter = attrs.sortandfilter;
                if (!sortAndFilter) {
                    return;
                }
                element.addClass('link');

                var iconStr = '<i class="icon-sort pull-right" ng-show="table.sort.column != \'' + sortAndFilter + '\'"></i>' +
                    '<i class="icon-sort-up pull-right" ng-show="table.sort.column == \'' + sortAndFilter + '\' && !table.sort.reverse"></i>' +
                    '<i class="icon-sort-down pull-right" ng-show="table.sort.column == \'' + sortAndFilter + '\' && table.sort.reverse"></i>';
                var filterStr = '';
                if (!element.hasClass('filter-none')) {
                    filterStr = '<div class="tableFilter" ng-show="table.filtering">' +
                        '<input type="text" class="form-control" ng-model="table.search[\'' + sortAndFilter + '\']" stop-event="click" placeholder="Enter search criteria..."></div>';
                }
                var iconsAndFilter = angular.element(iconStr + filterStr);
                var iconsAndFilterCompiled = $compile(iconsAndFilter)(scope);
                element.append(iconsAndFilterCompiled);
                element.bind('click', function () {
                    if (!scope.table.sort) {
                        scope.table.sort = {};
                    }
                    var changed = scope.table.sort.column !== sortAndFilter;
                    if (changed) {
                        scope.table.sort.column = sortAndFilter;
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