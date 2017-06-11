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

angular.module('scenarioo.services').factory('ReferenceTreeNavigationService', function () {

    var objType = {
        feature: 1,
        scenario: 2,
        page: 3,
        step: 4,
        object: 5
    };

    function goToStep(navigationElement) {
        if (angular.isUndefined(navigationElement.stepIdentifier) || !angular.isString(navigationElement.stepIdentifier)) {
            return;
        }

        var stepIdentifierParts = navigationElement.stepIdentifier.split('/');
        if (stepIdentifierParts.length !== 3) {
            return;
        }

        navigationElement.pageName = stepIdentifierParts[0];
        navigationElement.pageOccurrence = stepIdentifierParts[1];
        navigationElement.stepInPageOccurrence = stepIdentifierParts[2];
        return buildLocationPath(navigationElement);
    }

    function buildLocationPath(navElement) {
        var locationPath = '';

        if (navElement.objectType === objType.scenario || navElement.objectType === objType.feature) {
            locationPath = navElement.navigationType + '/' + navElement.featureName +
                '/' + navElement.scenarioName;
        } else if (navElement.objectType === objType.page) {
            locationPath += 'object/page/' + navElement.pageName;
        } else if (navElement.objectType === objType.step) {
            locationPath += 'step/' + navElement.featureName + '/' + navElement.scenarioName +
                '/' + navElement.pageName + '/' +
                navElement.pageOccurrence + '/' + navElement.stepInPageOccurrence;
        } else if (navElement.objectType === objType.object) {
            locationPath += 'object/' + navElement.navigationType + '/' + navElement.navigationName;
        }

        return locationPath;
    }

    function setCommonNavigationElementFields(navigationElement, childNode, navElementObjectType, isClickedNode) {
        if (!isClickedNode) {
            return;
        }
        navigationElement.navigationType = childNode.type;
        navigationElement.navigationName = childNode.name;
        navigationElement.objectType = navElementObjectType;
    }

    function populateNavigationElementRecursively(navigationElement, node, treeModel, isClickedNode) {
        if (angular.isDefined(node)) {
            switch (node.type) {
                case 'feature':
                    setCommonNavigationElementFields(navigationElement, node, objType.feature, isClickedNode);
                    navigationElement.featureName = node.name;
                    break;
                case 'scenario':
                    setCommonNavigationElementFields(navigationElement, node, objType.scenario, isClickedNode);
                    navigationElement.scenarioName = node.name;
                    break;
                case 'page':
                    setCommonNavigationElementFields(navigationElement, node, objType.page, isClickedNode);
                    navigationElement.pageName = node.name;
                    break;
                case 'step':
                    setCommonNavigationElementFields(navigationElement, node, objType.step, isClickedNode);
                    navigationElement.stepIdentifier = node.name;
                    break;
                default:
                    // Any other object (reference tree will not hierarchically traversed up-to root)
                    setCommonNavigationElementFields(navigationElement, node, objType.object, isClickedNode);
                    return;
            }
        }

        var parentNode = treeModel[treeModel.indexOf(node.parent)];

        if (angular.isDefined(parentNode)) {
            populateNavigationElementRecursively(navigationElement, parentNode, treeModel, false);
        }
    }

    // Build navigation path along the reference hierarchy tree (e.g. step / scenario / feature)
    // In case that some other objects should be navigated, the reference-tree will be called again with
    // selected object
    function buildNavigationElement(node, treeModel) {
        var navigationElement = {
            navigationType: '',
            navigationName: '',
            objectType: 0,
            featureName: '',
            scenarioName: '',
            scenarioPageName: '',
            pageName: null,
            stepIndex: null
        };

        populateNavigationElementRecursively(navigationElement, node, treeModel, true);

        return navigationElement;
    }

    return {
        goToRelatedView: function (nodeElement, treeModel) {
            var navigationElement = buildNavigationElement(nodeElement, treeModel);
            if (navigationElement.objectType === objType.step) {
                return goToStep(navigationElement);
            } else {
                return buildLocationPath(navigationElement);
                
            }
        }
    }
});
