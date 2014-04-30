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

angular.module('scenarioo.controllers').controller('ScenarioCtrl', function ($scope, $q, $filter, $routeParams, $location, ScenarioResource, HostnameAndPort, SelectedBranchAndBuild) {

    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var selectedBranchAndBuild;

    var showAllSteps = [];

    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');

    SelectedBranchAndBuild.callOnSelectionChange(loadScenario);

    function loadScenario(selected) {
        selectedBranchAndBuild = selected;
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName,
                scenarioName: scenarioName
            },
            function(result) {
                // Add page to the step to allow search for step- as well as page-properties
                populatePageAndSteps(result);
            });
    }

    function populatePageAndSteps(pagesAndScenarios) {
        for (var indexPage = 0; indexPage < pagesAndScenarios.pagesAndSteps.length; indexPage++) {
            var page = pagesAndScenarios.pagesAndSteps[indexPage];
            page.page.index = indexPage + 1;
            for (var indexStep = 0; indexStep < page.steps.length; indexStep++) {
                var step = page.steps[indexStep];
                step.page = page.page;
                step.index = indexStep;
                step.number = (indexStep === 0) ? page.page.index : page.page.index + '.' + indexStep;
                if (!step.title) {
                    step.title = 'undefined';
                }
            }
        }

        $scope.scenario = pagesAndScenarios.scenario;
        $scope.pagesAndSteps = pagesAndScenarios.pagesAndSteps;
        $scope.metadataTree = transformMetadataToTreeArray(pagesAndScenarios.scenario.details);
    }

    $scope.showAllStepsForPage = function(pageIndex) {
        return showAllSteps[pageIndex] || false;
    };

    $scope.toggleShowAllStepsForPage = function(pageIndex) {
        showAllSteps[pageIndex] = !showAllSteps[pageIndex];
    };

    $scope.isExpandAllPossible = function() {
        if(!angular.isDefined($scope.pagesAndSteps)) {
            return false;
        }

        for(var i = 0; i < $scope.pagesAndSteps.length; i++) {
            if(isExpandPossibleForPage($scope.pagesAndSteps[i], i)) {
                return true;
            }
        }

        return false;
    };

    function isExpandPossibleForPage(page, pageIndex) {
        return page.steps.length > 1 && $scope.showAllStepsForPage(pageIndex) === false;
    }

    $scope.isCollapseAllPossible = function() {
        if(!angular.isDefined($scope.pagesAndSteps)) {
            return false;
        }

        for(var i = 0; i < $scope.pagesAndSteps.length; i++) {
            if(isCollapsePossibleForPage($scope.pagesAndSteps[i], i)) {
                return true;
            }
        }

        return false;
    };

    function isCollapsePossibleForPage(page, pageIndex) {
        return page.steps.length > 1 && $scope.showAllStepsForPage(pageIndex) === true;
    }

    $scope.expandAll = function() {
        // numberOfPages is 0-indexed, therefore we have to add 1.
        // TODO: This should be fixed in the future, so that the server returns the right number.
        var numberOfPages = $scope.scenario.calculatedData.numberOfPages + 1;
        for(var i = 0; i < numberOfPages; i++) {
            showAllSteps[i] = true;
        }
    };

    $scope.collapseAll = function() {
        for(var i = 0; i < showAllSteps.length; i++) {
            showAllSteps[i] = false;
        }
    };

    $scope.getScreenShotUrl = function (imgName) {
        if(angular.isUndefined(selectedBranchAndBuild)) {
            return;
        }
        return HostnameAndPort.forLink() + 'rest/branches/' + selectedBranchAndBuild.branch + '/builds/' + selectedBranchAndBuild.build +
            '/usecases/' + useCaseName + '/scenarios/' + scenarioName + '/image/' + imgName;
    };

    $scope.getLinkToStep = function (pageName, pageIndex, stepIndex) {
        return '#/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) +
            '/' + pageIndex + '/' + stepIndex;
    };

    $scope.resetSearchField = function () {
        $scope.searchFieldText = '';
    };

    // TODO make this generic and share it with step.js
    $scope.showingMetaData = false;
    var metadataExpanded = [];
    metadataExpanded['sc-step-properties'] = true;

    $scope.isMetadataCollapsed = function (type) {
        var collapsed = angular.isUndefined(metadataExpanded[type]) || metadataExpanded[type] === false;
        return collapsed;
    };

    $scope.toggleMetadataCollapsed = function (type) {
        var currentValue = metadataExpanded[type];
        if (angular.isUndefined(currentValue)) {
            currentValue = false;
        }
        var newValue = !currentValue;
        metadataExpanded[type] = newValue;
    };

});
