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

angular.module('scenarioo.controllers').controller('ScenarioCtrl', function ($scope, $q, $filter, $routeParams,
    $location, $window, ScenarioResource, HostnameAndPort, SelectedBranchAndBuild, Config, PagesAndSteps) {

    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var selectedBranchAndBuild;

    var showAllSteps = [];

    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

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
                $scope.pagesAndScenarios = PagesAndSteps.populatePagesAndSteps(result);
                $scope.useCaseDescription = result.useCase.description;
                $scope.scenario = $scope.pagesAndScenarios.scenario;
                $scope.useCase = result.useCase;
                $scope.pagesAndSteps = $scope.pagesAndScenarios.pagesAndSteps;
                $scope.metadataTree = transformMetadataToTreeArray($scope.pagesAndScenarios.scenario.details);
                $scope.scenarioInformationTree = createScenarioInformationTree($scope.scenario);

                $scope.hasAnyLabels = function() {
                    var hasAnyUseCaseLabels = $scope.useCase.labels.labels.length > 0;
                    var hasAnyScenarioLabels = $scope.scenario.labels.labels.length > 0;

                    return hasAnyUseCaseLabels || hasAnyScenarioLabels;
                };

                if (Config.expandPagesInScenarioOverview()) {
                    $scope.expandAll();
                }
            });
    }

    $scope.showAllStepsForPage = function (pageIndex) {
        return showAllSteps[pageIndex] || false;
    };

    $scope.toggleShowAllStepsForPage = function (pageIndex) {
        showAllSteps[pageIndex] = !showAllSteps[pageIndex];
    };

    $scope.isExpandAllPossible = function () {
        if (!angular.isDefined($scope.pagesAndSteps)) {
            return false;
        }

        for (var i = 0; i < $scope.pagesAndSteps.length; i++) {
            if (isExpandPossibleForPage($scope.pagesAndSteps[i], i)) {
                return true;
            }
        }

        return false;
    };

    function isExpandPossibleForPage(page, pageIndex) {
        return page.steps.length > 1 && $scope.showAllStepsForPage(pageIndex) === false;
    }

    $scope.isCollapseAllPossible = function () {
        if (!angular.isDefined($scope.pagesAndSteps)) {
            return false;
        }

        for (var i = 0; i < $scope.pagesAndSteps.length; i++) {
            if (isCollapsePossibleForPage($scope.pagesAndSteps[i], i)) {
                return true;
            }
        }

        return false;
    };

    function isCollapsePossibleForPage(page, pageIndex) {
        return page.steps.length > 1 && $scope.showAllStepsForPage(pageIndex) === true;
    }

    $scope.expandAll = function () {
        // numberOfPages is 0-indexed, therefore we have to add 1.
        // TODO: This should be fixed in the future, so that the server returns the right number.
        var numberOfPages = $scope.scenario.calculatedData.numberOfPages + 1;
        for (var i = 0; i < numberOfPages; i++) {
            showAllSteps[i] = true;
        }
    };

    $scope.collapseAll = function () {
        for (var i = 0; i < showAllSteps.length; i++) {
            showAllSteps[i] = false;
        }
    };

    $scope.getScreenShotUrl = function (imgName) {
        if (angular.isUndefined(selectedBranchAndBuild)) {
            return;
        }
        return HostnameAndPort.forLink() + 'rest/branch/' + selectedBranchAndBuild.branch + '/build/' + selectedBranchAndBuild.build +
            '/usecase/' + useCaseName + '/scenario/' + scenarioName + '/image/' + imgName;
    };

    $scope.getLinkToStep = function (pageName, pageOccurrence, stepInPageOccurrence) {
        // '/' will kill our application
        pageName = pageName.replace(/\//g, ' ');

        return '#/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) +
            '/' + pageOccurrence + '/' + stepInPageOccurrence;
    };

    $scope.resetSearchField = function () {
        $scope.searchFieldText = '';
    };

    function createScenarioInformationTree(scenario) {
        var stepInformation = {};
        stepInformation['Number of Pages'] = scenario.calculatedData.numberOfPages;
        stepInformation['Number of Steps'] = scenario.calculatedData.numberOfSteps;
        stepInformation.Status = scenario.status;
        return transformMetadataToTree(stepInformation);
    }

});
