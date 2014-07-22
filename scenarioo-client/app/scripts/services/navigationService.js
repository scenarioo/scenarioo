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

angular.module('scenarioo.services').factory('Navigation', function () {

    /**
     *  Configure for each routing a navigation element
     */
    var homeNavigation =
    {
        label: '<i class="icon-home"></i> Home',
        text: '',
        route: 'views/main.html',
        objectType: 'main',
        isLastNavigationElement: false
    };

    var manageNavigation =
    {
        label: '<i class="icon-cogs"></i> Manage',
        text: '',
        route: 'views/manage/manage.html',
        objectType: '',
        isLastNavigationElement: false
    };

    var useCaseNavigation =
    {
        label: '<strong>Use Case: </strong>',
        text: '',
        route: '/usecase/:usecase/',
        objectType: 'usecase',
        isLastNavigationElement: false
    };

    var scenarioNavigation =
    {
        label: '<strong>Scenario: </strong>',
        text: '',
        route: '/scenario/:usecase/:scenario/',
        objectType: 'scenario',
        isLastNavigationElement: false
    };

    var stepNavigation =
    {
        label: '<strong>Step: [pageIndex].[stepIndex] - [pageName]</strong>',
        text: '',
        route: '/step/:usecase/:scenario/:pageName/:pageIndex/:stepIndex/',
        objectType: 'step',
        isLastNavigationElement: false
    };

    var objectNavigation =
    {
        label: '<strong>[objectType]: [objectName]</strong>',
        text: '',
        route: '/object/:objectType/:objectName',
        objectType: 'object',
        isLastNavigationElement: false
    };

    /**
     *  Configure each breadcrumb element with navigationHierarchy
     */
    var breadcrumbElements = {
        'usecase': {
            label: 'Use Case: ',
            navigationHierarchy: [homeNavigation, useCaseNavigation]
        },

        'scenario': {
            label: 'Scenario:',
            navigationHierarchy: [homeNavigation, useCaseNavigation, scenarioNavigation]
        },

        'step': {
            label: 'Step:',
            navigationHierarchy: [homeNavigation, useCaseNavigation, scenarioNavigation, stepNavigation]
        },

        'main': {
            label: 'Main',
            navigationHierarchy: [homeNavigation]
        },

        'object': {
            label: 'Object',
            navigationHierarchy: [homeNavigation, objectNavigation]
        },

        'manage': {
            label: 'Manage',
            navigationHierarchy: [homeNavigation, manageNavigation]
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
                text = text.replace(':' + placeholder, navParameter[placeholder]);
            });
        }
        return text;
    }

    return {
        loadNavigationElements: function (objectType) {
            return angular.copy(breadcrumbElements[objectType]);
        },

        getNavigationElements: function(navElement, navParameter) {
            var navElements = [];

            if (angular.isDefined(navElement)) {
                angular.forEach(navElement.navigationHierarchy, function (navigationElement, index) {
                    if ((navElement.navigationHierarchy.length - index) === 1) {
                        navigationElement.isLastNavigationElement = true;
                    }
                    else {
                        navigationElement.isLastNavigationElement = false;
                    }

                    if (angular.isDefined(navParameter[navigationElement.objectType])) {
                        navigationElement.text = convertToPlainText(navParameter[navigationElement.objectType]);
                        navigationElement.route = setValuesInRoute(navigationElement.route, navParameter);
                    }

                    navElements.push(navigationElement);
                });
            }
            return navElements;
        },

        setValuesInLabel: function(text, navParameter) {
            var placeholders = text.match(/\[.*?(?=])./g);

            if (placeholders !== null) {
                angular.forEach(placeholders, function (placeholder) {
                    placeholder = placeholder.replace(/[\[\]]/g, '');
                    text = text.replace(/[\[\]]/g, '');
                    text = text.replace(placeholder, navParameter[placeholder]);
                });
            }
            return text;
        }
    };
});
