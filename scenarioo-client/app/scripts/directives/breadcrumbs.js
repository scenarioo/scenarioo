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

angular.module('scenarioo.directives').directive('scBreadcrumb', function ($routeParams, $location, $route, $compile, $filter, $sce, BreadcrumbsService, ScShareStepPopup) {

    var limit = 50;

    return {
        restrict: 'E',
        priority: 0,
        replace: true,
        templateUrl: 'template/breadcrumbs.html',
        link: function (scope) {

            scope.breadcrumbs = [];
            var navParameters = [];
            var breadcrumbId = $route.current.$$route.breadcrumbId;

            // Get all relevant scenarioo navigation artifacts (e.g. scenarioName, usecaseName, pageIndex, ...)
            navParameters = getNavigationParameters();

            var navElements = BreadcrumbsService.getNavigationElements(breadcrumbId, navParameters);

            angular.forEach(navElements, function (breadcrumbItem) {

                // Create breadcrumb objects
                var isLabelTextShortened = breadcrumbItem.label.length > limit && !breadcrumbItem.isLastNavigationElement;
                var breadcrumbLabelText = getShortenedLabelText(breadcrumbItem, isLabelTextShortened);
                var breadcrumb = {
                    text: breadcrumbLabelText,
                    tooltip: breadcrumbItem.textForTooltip,
                    showTooltip: isLabelTextShortened,
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

            scope.showStepLinks = function () {
                ScShareStepPopup.showShareStepPopup();
            };
        }
    };

    function getShortenedLabelText(breadcrumbItem, isLabelTextShortened) {
        return isLabelTextShortened ? getShortenedText(breadcrumbItem.label) : breadcrumbItem.label;
    }

    function getNavigationParameters() {
        return {
            usecase: $routeParams.useCaseName,
            scenario: $routeParams.scenarioName,
            pageName: $routeParams.pageName,
            pageOccurrence: parseInt($routeParams.pageOccurrence, 10),
            stepInPageOccurrence: parseInt($routeParams.stepInPageOccurrence, 10),
            objectType: $routeParams.objectType,
            objectName: $routeParams.objectName
        };
    }

    function getShortenedText(text) {
        if (text.length > limit) {
            var shortenedText = text.substr(0, limit);
            return shortenedText + '...';
        }
        return text;
    }
});