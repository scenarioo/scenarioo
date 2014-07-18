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
    $location, $window, localStorageService, ScenarioResource, HostnameAndPort, SelectedBranchAndBuild, PagesAndSteps) {

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
                $scope.scenario = $scope.pagesAndScenarios.scenario;
                $scope.pagesAndSteps = $scope.pagesAndScenarios.pagesAndSteps;
                $scope.metadataTree = transformMetadataToTreeArray($scope.pagesAndScenarios.scenario.details);
                $scope.scenarioInformationTree = createScenarioInformationTree($scope.scenario);
            });
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
        // '/' will kill our application
        pageName = pageName.replace(/\//g, ' ');

        return '#/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) +
            '/' + pageIndex + '/' + stepIndex;
    };

    $scope.resetSearchField = function () {
        $scope.searchFieldText = '';
    };

    function createScenarioInformationTree(scenario) {
        var stepInformation = {};
        stepInformation.Description = scenario.description;
        stepInformation['Number of Pages'] = scenario.calculatedData.numberOfPages;
        stepInformation['Number of Steps'] = scenario.calculatedData.numberOfSteps;
        stepInformation.Status = scenario.status;
        return transformMetadataToTree(stepInformation);
    }

    // TODO make the following code generic and share it with step.js

    var SCENARIO_METADATA_SECTION_EXPANDED = 'scenarioo-scenarioMetadataSectionExpanded-';
    var SCENARIO_METADATA_VISIBLE = 'scenarioo-scenarioMetadataVisible';

    $scope.isMetadataExpanded = function (type) {
        var metadataExpanded = localStorageService.get(SCENARIO_METADATA_SECTION_EXPANDED + type);
        if (metadataExpanded === 'true') {
            return true;
        } else {
            return false;
        }
    };

    $scope.toggleMetadataExpanded = function (type) {
        var metadataExpanded = !$scope.isMetadataExpanded(type);
        localStorageService.set(SCENARIO_METADATA_SECTION_EXPANDED + type, '' + metadataExpanded);
    };

    $scope.isMetadataCollapsed = function (type) {
        return !$scope.isMetadataExpanded(type);
    };

    $scope.toggleShowingMetadata = function() {
        $scope.showingMetaData=!$scope.showingMetaData;
        localStorageService.set(SCENARIO_METADATA_VISIBLE, '' + $scope.showingMetaData);
    };

    /**
     * Init metadata visibility and expanded sections from local storage on startup.
     */
    function initMetadataVisibilityAndExpandedSections() {

        // Init metadata visibility from local storage
        var metadataVisible = localStorageService.get(SCENARIO_METADATA_VISIBLE);
        if (metadataVisible === 'true') {
            $scope.showingMetaData = true;
        }
        else if (metadataVisible === 'false') {
            $scope.showingMetaData = false;
        } else {
            // default
            $scope.showingMetaData = $window.innerWidth > 800;
        }

        // Set special scenario metadata to expanded by default.
        var majorStepPropertiesExpanded = localStorageService.get(SCENARIO_METADATA_SECTION_EXPANDED + 'sc-scenario-properties');
        var isMajorStepPropertiesExpandedSetToFalse = majorStepPropertiesExpanded === 'false';
        if (!isMajorStepPropertiesExpandedSetToFalse) {
            localStorageService.set(SCENARIO_METADATA_SECTION_EXPANDED + 'sc-scenario-properties', 'true');
        }

    }
    initMetadataVisibilityAndExpandedSections();

});
