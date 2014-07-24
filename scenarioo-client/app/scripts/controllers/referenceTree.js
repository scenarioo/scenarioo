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

angular.module('scenarioo.controllers').controller('ReferenceTreeCtrl', function ($scope,
    $routeParams, $location, ObjectIndexListResource, PagesAndSteps, SelectedBranchAndBuild, ScenarioResource,
    TreeNode, StepService, $filter) {

    var objectType = $routeParams.objectType;
    var objectName = $routeParams.objectName;
    var selectedBranchAndBuild;
    var navigationElement = {
        navigationType: '',
        navigationName: '',
        objectType: 0,
        useCaseName: '',
        scenarioName: '',
        scenarioPageName: '',
        pageIndex: null,
        stepIndex: null
    };

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
        selectedBranchAndBuild = selected;

        // Get all references for given object
        ObjectIndexListResource.get(
        {
            branchName: selected.branch,
            buildName: selected.build,
            objectType: objectType,
            objectName: objectName
        },
        function(result) {
            $scope.object = result;
            var transformedMetaDataTree = transformMetadataToTree(result.object.details);
            $scope.metadataTree = transformedMetaDataTree.childNodes;
        });
	}

    $scope.goToRelatedView = function(nodeElement) {
        buildNavigationElement(nodeElement);
        if (navigationElement.objectType === objType.page || navigationElement.objectType === objType.step) {

            var stepPromise = StepService.getStep({
                'branchName': selectedBranchAndBuild.branch,
                'buildName': selectedBranchAndBuild.build,
                'usecaseName': navigationElement.useCaseName,
                'scenarioName': navigationElement.scenarioName,
                'stepIndex': navigationElement.stepIndex
            });

            stepPromise.then(function (result) {
                $scope.step = result.step;
                $scope.stepNavigation = result.stepNavigation;
                if (angular.isDefined($scope.step) && angular.isDefined($scope.stepNavigation)) {
                    navigationElement.pageName = $scope.step.page.name;
                    navigationElement.pageIndex = $scope.stepNavigation.pageIndex;
                    navigationElement.stepIndex = $scope.stepNavigation.pageStepIndex;
                    var locationPath = buildLocationPath(navigationElement);
                    $location.path(locationPath);
                }
            });
        }
        else {
            var locationPath = buildLocationPath(navigationElement);
            $location.path(locationPath);
        }
    };

    function buildLocationPath(navElement){
        var locationPath = '';

        if (navElement.objectType === objType.scenario || navElement.objectType === objType.usecase) {
            locationPath = navElement.navigationType + '/' + encodeURIComponent(navElement.useCaseName) +
                '/' + encodeURIComponent(navElement.scenarioName);
        }

        if (navElement.objectType === objType.step || navElement.objectType === objType.page) {
            locationPath += 'step/' + encodeURIComponent(navElement.useCaseName) + '/' + encodeURIComponent(navElement.scenarioName) +
                '/' + encodeURIComponent(navElement.pageName) + '/' +
                navElement.pageIndex + '/' + navElement.stepIndex;
        }

        if (navElement.objectType === objType.object) {
            locationPath +=  'object/' + encodeURIComponent(navElement.navigationType) + '/' + encodeURIComponent(navElement.navigationName);
        }

        return locationPath;
    }

    // Build navigation path along the reference hierarchy tree (e.g. step / scenario / usecase)
    // In case that some other objects should be navigated, the reference-tree will be called again with
    // selected object
    function buildNavigationElement(childNode) {
        if (angular.isDefined(childNode)) {
            switch (childNode.type) {
            case 'usecase':
                setNavigationElement(childNode, objType.usecase);
                navigationElement.useCaseName = childNode.name;
                break;
            case 'scenario':
                setNavigationElement(childNode, objType.scenario);
                navigationElement.scenarioName = childNode.name;
                break;
            case 'page':
                setNavigationElement(childNode, objType.page);
                navigationElement.pageIndex = childNode.name;
                break;
            case 'step':
                setNavigationElement(childNode, objType.step);
                navigationElement.stepIndex = childNode.name;
                break;
            default:
                // Any other object (reference tree will not hierarchically traversed up-to root)
                setNavigationElement(childNode, objType.object);
                return;
            }
        }

        var parentNode = $scope.treemodel[$scope.treemodel.indexOf(childNode.parent)];

        if (angular.isDefined(parentNode)) {
            buildNavigationElement(parentNode);
        }
    }

    function setNavigationElement(childNode, objectType) {

        if (navigationElement.objectType === 0) {
            navigationElement.navigationType = childNode.type;
            navigationElement.navigationName = childNode.name;
            navigationElement.objectType = objectType;
        }
    }

    $scope.expandAndCollapseTree = function(treemodel) {
        TreeNode.expandAndCollapseTree(treemodel, $scope);
    };

    $scope.resetSearchField = function() {
        $scope.searchField = '';
    };
});