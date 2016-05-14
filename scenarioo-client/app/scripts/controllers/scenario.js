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

angular.module('scenarioo.controllers').controller('ScenarioCtrl', function ($scope, $q, $filter, $routeParams,
                                                                             $location, $window, ScenarioResource, HostnameAndPort, SelectedBranchAndBuild, SelectedComparison,
                                                                             Config, PagesAndSteps, DiffInfoService, LabelConfigurationsResource, RelatedIssueResource, SketchIdsResource, ComparisonAliasResource, ScenarioDiffInfoResource, StepDiffInfosResource) {

    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var comparisonBranchName;
    var comparisonBuildName;
    var selectedBranchAndBuild;

    var showAllSteps = [];

    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    SelectedBranchAndBuild.callOnSelectionChange(loadScenario);
    $scope.comparisonInfo = SelectedComparison.info;

    // FIXME this code is duplicated. How can we extract it into a service?
    LabelConfigurationsResource.query({}, function (labelConfigurations) {
        $scope.labelConfigurations = labelConfigurations;
    });

    function loadScenario(selected) {
        selectedBranchAndBuild = selected;
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName,
                scenarioName: scenarioName
            },
            function (result) {
                // Add page to the step to allow search for step- as well as page-properties
                $scope.pagesAndScenarios = PagesAndSteps.populatePagesAndSteps(result);
                $scope.useCaseDescription = result.useCase.description;
                $scope.scenario = $scope.pagesAndScenarios.scenario;
                $scope.useCase = result.useCase;
                $scope.pagesAndSteps = $scope.pagesAndScenarios.pagesAndSteps;
                $scope.metadataTree = transformMetadataToTreeArray($scope.pagesAndScenarios.scenario.details);
                $scope.scenarioInformationTree = createScenarioInformationTree($scope.scenario, result.scenarioStatistics);
                $scope.scenarioStatistics = result.scenarioStatistics;

                if(SelectedComparison.isDefined()) {
                    var selectedBrandAndBuild = SelectedBranchAndBuild.selected();
                    loadDiffInfoData($scope.pagesAndSteps, selectedBrandAndBuild.branch, selectedBrandAndBuild.build, SelectedComparison.selected());
                } else {
                    $scope.scenarios = result.scenarios;
                }

                loadRelatedIssues();

                $scope.hasAnyLabels = function () {
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
        var numberOfPages = $scope.scenarioStatistics.numberOfPages;
        for (var i = 0; i < numberOfPages; i++) {
            showAllSteps[i] = true;
        }
    };

    $scope.collapseAll = function () {
        for (var i = 0; i < showAllSteps.length; i++) {
            showAllSteps[i] = false;
        }
    };

    $scope.getScreenShotUrl = function (imgName, stepIsRemoved) {
        if (angular.isUndefined(selectedBranchAndBuild)) {
            return undefined;
        }

        var branch = selectedBranchAndBuild.branch;
        var build = selectedBranchAndBuild.build;
        if(SelectedComparison.isDefined() && stepIsRemoved) {
            branch = comparisonBranchName;
            build = comparisonBuildName;
        }

        return HostnameAndPort.forLink() + 'rest/branch/' + branch + '/build/' + build +
            '/usecase/' + useCaseName + '/scenario/' + scenarioName + '/image/' + imgName;
    };

    $scope.getLinkToStep = function (pageName, pageOccurrence, stepInPageOccurrence) {
        return '#/step/' + encodeURIComponent(useCaseName) + '/' + encodeURIComponent(scenarioName) + '/' + encodeURIComponent(pageName) +
            '/' + pageOccurrence + '/' + stepInPageOccurrence;
    };

    $scope.resetSearchField = function () {
        $scope.searchFieldText = '';
    };

    function createScenarioInformationTree(scenario, scenarioStatistics) {
        var stepInformation = {};
        stepInformation['Number of Pages'] = scenarioStatistics.numberOfPages;
        stepInformation['Number of Steps'] = scenarioStatistics.numberOfSteps;
        stepInformation.Status = scenario.status;
        return transformMetadataToTree(stepInformation);
    }

    function loadDiffInfoData(pagesAndSteps, baseBranchName, baseBuildName, comparisonName) {
        if (pagesAndSteps && baseBranchName && baseBuildName && useCaseName && scenarioName){
            ComparisonAliasResource.get(
                {'comparisonName': comparisonName},
                function onSuccess(result) {
                    comparisonBranchName = result.comparisonBranchName;
                    comparisonBuildName = result.comparisonBuildName;

                    ScenarioDiffInfoResource.get(
                        {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName, 'useCaseName': useCaseName, 'scenarioName': scenarioName},
                        function onSuccess(scenarioDiffInfo) {
                            StepDiffInfosResource.get(
                                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName, 'useCaseName': useCaseName, 'scenarioName': scenarioName},
                                function onSuccess(stepDiffInfos) {
                                    DiffInfoService.enrichPagesAndStepsWithDiffInfos($scope.pagesAndSteps, scenarioDiffInfo.removedElements, stepDiffInfos);
                                }
                            );
                        }
                    );
                }
            );
        }
    }

    function loadRelatedIssues(){
        RelatedIssueResource.query({
            branchName: SelectedBranchAndBuild.selected().branch,
            buildName: SelectedBranchAndBuild.selected().build,
            useCaseName: $routeParams.useCaseName,
            scenarioName: $routeParams.scenarioName
        }, function(result){
            $scope.relatedIssues = result;
            $scope.hasAnyRelatedIssues = function(){
                return $scope.relatedIssues.length > 0;
            };
            $scope.goToIssue = goToIssue;
        });
    }

    function goToIssue(issue) {
        var selectedBranch = SelectedBranchAndBuild.selected().branch;
        SketchIdsResource.get(
            {'branchName': selectedBranch, 'issueId': issue.id },
            function onSuccess(result) {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    }

    // FIXME this code is duplicated. How can we extract it into a service?
    $scope.getLabelStyle = function (labelName) {
        var labelConfig = $scope.labelConfigurations[labelName];
        if (labelConfig) {
            return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
        }
    };
});
