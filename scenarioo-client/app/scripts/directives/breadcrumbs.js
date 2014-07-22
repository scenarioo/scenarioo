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

angular.module('scenarioo.directives').directive('scBreadcrumb', function ($routeParams, $location,
    $route, $compile, $filter, $sce, Navigation) {

    var limit = 50;

    return {
        restrict: 'E',
        priority: 0,
        replace: true,
        templateUrl: 'template/breadcrumbs.html',
        link: function (scope) {
            scope.breadcrumbs = [];

            var navParameter = [];
            var breadCrumbElements = [];
            var objectType = $location.$$path.split('/')[1];

            // Get all relevant scenarioo navigation artifacts (e.g. scenarioName, usecaseName, pageIndex, ...)
            navParameter = getNavigationParameter();

            breadCrumbElements = Navigation.loadNavigationElements(objectType);
            var navElements = Navigation.getNavigationElements(breadCrumbElements, navParameter);

            angular.forEach(navElements, function(breadcrumbItem){
                breadcrumbItem.text = $filter('scHumanReadable')(decodeURIComponent(breadcrumbItem.text));
                breadcrumbItem.label = Navigation.setValuesInLabel(breadcrumbItem.label, navParameter);

                // Create breadcrumb object
                var hasTooltip = (breadcrumbItem.text.length + breadcrumbItem.label.length) > limit && !breadcrumbItem.isLastNavigationElement;
                var breadcrumbText = breadcrumbItem.label + getToolTip(breadcrumbItem, hasTooltip);
                var breadcrumb = {
                    text: breadcrumbText,
                    tooltip: breadcrumbItem.text,
                    showTooltip: hasTooltip,
                    href: '#' + breadcrumbItem.route,
                    isLast: breadcrumbItem.isLastNavigationElement
                };

                // make sure we can bind html to view
                breadcrumb.text = $sce.trustAsHtml(breadcrumb.text);
                scope.breadcrumbs.push(breadcrumb);
            });

            scope.email = {
                title: encodeURIComponent('Link to the User Scenario Documentation'),
                link: encodeURIComponent($location.absUrl())
            };
        }
    };

    function getToolTip(breadcrumbItem, hasTooltip) {
        var toolTip = hasTooltip ? getShortenedText(breadcrumbItem.text) : breadcrumbItem.text;
        return toolTip;
    }

    function getNavigationParameter() {
        var navParameter = {
            step: '',
            usecase: $routeParams.useCaseName,
            scenario: $routeParams.scenarioName,
            pageName: $routeParams.pageName,
            pageIndex: parseInt($routeParams.pageIndex, 10) + 1,
            stepIndex: parseInt($routeParams.stepIndex, 10),
            objectType: $routeParams.objectType,
            objectName: $routeParams.objectName
        };

        return navParameter;
    }

    function getShortenedText(text) {
        if (text.length > limit) {
            var shortenedText = text.substr(0, limit);
            return shortenedText + '...';
        }
        return text;
    }
});