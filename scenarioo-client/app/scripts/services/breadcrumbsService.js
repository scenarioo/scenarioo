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

angular.module('scenarioo.services').factory('BreadcrumbsService', function ($filter) {

    /**
     *  Configure breadcrumb elements for different objects that can occur in a concrete breadcrumb path for a page
     */

    var homeElement =
    {
        label: '<i class="icon-home"></i> Home',
        route: 'views/main.html'   // maybe better rename to linkUrl
    };

    var manageElement =
    {
        label: '<i class="icon-cogs"></i> Manage',
        route: 'views/manage/manage.html'
    };

    var useCaseElement =
    {
        label: '<strong>Use Case:</strong> [usecase]',
        route: '/usecase/:usecase/'
    };

    var scenarioElement =
    {
        label: '<strong>Scenario:</strong> [scenario]',
        route: '/scenario/:usecase/:scenario/'
    };

    var stepElement =
    {
        label: '<strong>Step:</strong> [pageName]/[pageOccurrence]/[stepInPageOccurrence]',
        route: '/step/:usecase/:scenario/:pageName/:pageOccurrence/:stepInPageOccurrence/'
    };

    var objectElement =
    {
        label: '<strong>[objectType]:</strong> [objectName]',
        route: '/object/:objectType/:objectName'
    };

    var stepSketchElement =
    {
        label: '<strong>Sketch</strong>'
    };

    /**
     *  Configure breadcrumb paths that can be assigned to routes (see app.js) to display them as breadcrumbs for according pages.
     *  Key of the elements is the 'breadcrumbId', use it to link one of this path to a routing in app.js
     */
    var breadcrumbPaths = {

        'usecase': {
            breadcrumbPath: [homeElement, useCaseElement]
        },

        'scenario': {
            breadcrumbPath: [homeElement, useCaseElement, scenarioElement]
        },

        'step': {
            breadcrumbPath: [homeElement, useCaseElement, scenarioElement, stepElement]
        },

        'main': {
            breadcrumbPath: [homeElement]
        },

        'object': {
            breadcrumbPath: [homeElement, objectElement]
        },

        'manage': {
            breadcrumbPath: [homeElement, manageElement]
        },

        'stepsketch': {
            breadcrumbPath: [homeElement, stepSketchElement]
        }
    };

    function convertToPlainText(text) {
        return text.replace(/<\/?[^>]+(>|$)/g, '');
    }

    function setValuesInRoute(text, navParameter) {
        var placeholders = text.match(/:.*?[^<](?=\/)/g);

        if (placeholders !== null) {
            angular.forEach(placeholders, function (placeholder) {
                placeholder = placeholder.replace(':', '');
                if (placeholder === 'usecase' || placeholder === 'scenario' || placeholder === 'issueId' || placeholder === 'scenarioSketchId') {
                    text = text.replace(':' + placeholder, navParameter[placeholder]);
                }
            });
        }
        return text;
    }

    function getText(navParameter, placeholder) {
        var value = navParameter[placeholder];

        if (placeholder === 'usecase' || placeholder === 'scenario') {
            return $filter('scHumanReadable')(value);
        }

        return value;
    }

    function setValuesInLabel(text, navParameter) {
        var placeholders = text.match(/\[.*?(?=])./g);

        if (placeholders !== null) {
            angular.forEach(placeholders, function (placeholder) {
                placeholder = placeholder.replace(/[\[\]]/g, '');
                text = text.replace(/[\[\]]/g, '');
                text = text.replace(placeholder, getText(navParameter, placeholder));
            });
            text = decodeURIComponent(text);
        }
        return text;
    }

    function buildNavigationElements(breadcrumbId, navParameters) {
        if (angular.isUndefined(breadcrumbId) || angular.isUndefined(navParameters)) {
            return undefined;
        }

        var breadCrumbElements = angular.copy(breadcrumbPaths[breadcrumbId]);

        var navElements = [];

        if (angular.isDefined(breadCrumbElements)) {
            angular.forEach(breadCrumbElements.breadcrumbPath, function (navigationElement, index) {
                if ((breadCrumbElements.breadcrumbPath.length - index) === 1) {
                    navigationElement.isLastNavigationElement = true;
                }
                else {
                    navigationElement.isLastNavigationElement = false;
                }
                if(navigationElement.route) {
                    navigationElement.route = setValuesInRoute(navigationElement.route, navParameters);
                }
                navigationElement.label = setValuesInLabel(navigationElement.label, navParameters);
                navigationElement.textForTooltip = convertToPlainText(navigationElement.label);
                navElements.push(navigationElement);
            });
        }
        return navElements;
    }

    // Service interface
    return {
        getNavigationElements: buildNavigationElements
    };

});
