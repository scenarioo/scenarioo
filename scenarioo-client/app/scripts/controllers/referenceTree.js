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
        useCaseName: '',
        scenarioName: '',
        scenarioPageName: '',
        pageIndex: null,
        stepIndex: null
    };
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    var objType = {
        USECASE: 0,
        SCENARIO: 1,
        PAGE: 2,
        STEP:3
    };

    $scope.referenceTree = [];
    $scope.treemodel = [];
    $scope.showingMetaData = true;

    // Determines if the tree has expanded / collapsed rootnodes initially
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

        if (navigationElement.navigationType === objType.PAGE || navigationElement.navigationType === objType.STEP) {

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
        var locationPath = '/' + navElement.navigationName + '/' + encodeURIComponent(navElement.useCaseName) +
            '/' + encodeURIComponent(navElement.scenarioName);

        if (navElement.navigationType === objType.STEP) {
            locationPath += '/' + encodeURIComponent(navElement.pageName) + '/' + navElement.pageIndex + '/' + navElement.stepIndex;
        }
        else if (navElement.navigationType === objType.PAGE) {
            locationPath += '/' + encodeURIComponent(navElement.pageName) + '/' + navElement.pageIndex + '/' + navElement.stepIndex;
        }
        return locationPath;
    }

    // Build navigation path along the reference hierarchy tree
    // In case that some other objects should be navigated, the last
    // navigation hierarchy is always 'step'
    function buildNavigationElement(childNode) {
        if (angular.isDefined(childNode)) {
            switch (childNode.type) {
            case 'usecase':
                setNavigationType(childNode, objType.USECASE);
                navigationElement.useCaseName = childNode.name;
                break;
            case 'scenario':
                setNavigationType(childNode, objType.SCENARIO);
                navigationElement.scenarioName = childNode.name;
                break;
            case 'page':
                setNavigationType(childNode, objType.PAGE);
                navigationElement.pageIndex = childNode.name;
                break;
            case 'step':
                setNavigationType(childNode, objType.STEP);
                navigationElement.stepIndex = childNode.name;
            }
        }

        var parentNode = $scope.treemodel[$scope.treemodel.indexOf(childNode.parent)];

        if (angular.isDefined(parentNode)) {
            buildNavigationElement(parentNode);
        }
    }

    function setNavigationType(childNode, objType) {
        if (navigationElement.navigationType === '') {
            navigationElement.navigationType = objType;
            navigationElement.navigationName = childNode.type;
        }
    }

    $scope.expandAndCollapseTree = function(treemodel) {
        TreeNode.expandAndCollapseTree(treemodel, $scope);
    };

    $scope.resetSearchField = function() {
        $scope.searchField = '';
    };
});