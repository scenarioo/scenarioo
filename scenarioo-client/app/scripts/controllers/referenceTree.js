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

angular.module('scenarioo.controllers').controller('ReferenceTreeCtrl', function ($scope, $routeParams, $location, ObjectIndexListResource, PagesAndSteps, SelectedBranchAndBuild, ScenarioResource, TreeNode, $filter) {

    var objectType = $routeParams.objectType;
    var objectName = $routeParams.objectName;

    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    var objType = {
        usecase: 1,
        scenario: 2,
        page: 3,
        step: 4,
        object: 5
    };

    $scope.referenceTree = [];
    $scope.treemodel = [];
    $scope.showingMetaData = true;

    // Determines if the tree has expanded / collapsed root-nodes initially
    $scope.rootIsCollapsed = false;
    $scope.toggleLabel = 'collapse';

    SelectedBranchAndBuild.callOnSelectionChange(loadReferenceTree);

    function loadReferenceTree(selected) {

        // Get all references for given object
        ObjectIndexListResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                objectType: objectType,
                objectName: objectName
            },
            function (result) {
                $scope.object = result;
                var transformedMetaDataTree = transformMetadataToTree(result.object.details);
                $scope.metadataTree = transformedMetaDataTree.childNodes;
            });
    }

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
        var locationPath = buildLocationPath(navigationElement);
        $location.path(locationPath);
    }

    // Entry point when a tree entry is clicked
    $scope.goToRelatedView = function (nodeElement) {
        var navigationElement = buildNavigationElement(nodeElement);
        if (navigationElement.objectType === objType.step) {
            goToStep(navigationElement);
        } else {
            var locationPath = buildLocationPath(navigationElement);
            $location.path(locationPath);
        }
    };

    function buildLocationPath(navElement) {
        var locationPath = '';

        if (navElement.objectType === objType.scenario || navElement.objectType === objType.usecase) {
            locationPath = navElement.navigationType + '/' + navElement.useCaseName +
            '/' + navElement.scenarioName;
        } else if (navElement.objectType === objType.page) {
            locationPath += 'object/page/' + navElement.pageName;
        } else if (navElement.objectType === objType.step) {
            locationPath += 'step/' + navElement.useCaseName + '/' + navElement.scenarioName +
            '/' + navElement.pageName + '/' +
            navElement.pageOccurrence + '/' + navElement.stepInPageOccurrence;
        } else if (navElement.objectType === objType.object) {
            locationPath += 'object/' + navElement.navigationType + '/' + navElement.navigationName;
        }

        return locationPath;
    }

    // Build navigation path along the reference hierarchy tree (e.g. step / scenario / usecase)
    // In case that some other objects should be navigated, the reference-tree will be called again with
    // selected object
    function buildNavigationElement(node) {
        var navigationElement = {
            navigationType: '',
            navigationName: '',
            objectType: 0,
            useCaseName: '',
            scenarioName: '',
            scenarioPageName: '',
            pageName: null,
            stepIndex: null
        };

        populateNavigationElementRecursively(navigationElement, node, true);

        return navigationElement;
    }

    function populateNavigationElementRecursively(navigationElement, node, isClickedNode) {
        if (angular.isDefined(node)) {
            switch (node.type) {
                case 'usecase':
                    setCommonNavigationElementFields(navigationElement, node, objType.usecase, isClickedNode);
                    navigationElement.useCaseName = node.name;
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

        var parentNode = $scope.treemodel[$scope.treemodel.indexOf(node.parent)];

        if (angular.isDefined(parentNode)) {
            populateNavigationElementRecursively(navigationElement, parentNode, false);
        }
    }

    function setCommonNavigationElementFields(navigationElement, childNode, navElementObjectType, isClickedNode) {
        if (!isClickedNode) {
            return;
        }
        navigationElement.navigationType = childNode.type;
        navigationElement.navigationName = childNode.name;
        navigationElement.objectType = navElementObjectType;
    }

    $scope.expandAndCollapseTree = function (treemodel) {
        TreeNode.expandAndCollapseTree(treemodel, $scope);
    };

    $scope.resetSearchField = function () {
        $scope.searchField = '';
    };

});
