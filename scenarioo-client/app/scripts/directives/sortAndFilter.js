'use strict';

angular.module('ngUSDClientApp.directives').directive('sortandfilter', function ($compile) {
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