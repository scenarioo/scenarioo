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

angular.module('scenarioo.controllers').controller('ScenarioController', ScenarioController);

function ScenarioController($filter, $routeParams,
          $location, ScenarioResource, SelectedBranchAndBuildService, SelectedComparison,
          ConfigService, PagesAndStepsService, DiffInfoService, LabelConfigurationsResource, RelatedIssueResource, SketchIdsResource, BuildDiffInfoResource, ScenarioDiffInfoResource, StepDiffInfosResource) {

    var vm = this;
    vm.useCaseDescription = '';
    vm.scenario = {};
    vm.useCase = {};
    vm.pagesAndSteps = {};
    vm.metadataTree = {};
    vm.scenarioInformationTree = {};
    vm.hasAnyLabels = false;
    vm.searchFieldText = '';
    vm.relatedIssues = [];
    vm.hasAnyRelatedIssues = false;
    vm.expandAll = expandAll;
    vm.showAllStepsForPage = showAllStepsForPage;
    vm.toggleShowAllStepsForPage = toggleShowAllStepsForPage;
    vm.isExpandAllPossible = isExpandAllPossible;
    vm.isCollapseAllPossible = isCollapseAllPossible;
    vm.collapseAll = collapseAll;
    vm.getScreenShotUrl = getScreenShotUrl;
    vm.getLinkToStep = getLinkToStep;
    vm.resetSearchField = resetSearchField;
    vm.goToIssue = goToIssue;
    vm.getLabelStyle = getLabelStyle;
    vm.comparisonInfo = SelectedComparison.info;

    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var comparisonBranchName;
    var comparisonBuildName;
    var selectedBranchAndBuild;
    var labelConfigurations = [];
    var pagesAndScenarios: any = [];
    var scenarioStatistics: any = {};
    var showAllSteps = [];
    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    SelectedBranchAndBuildService.callOnSelectionChange(loadScenario);

    LabelConfigurationsResource.query({}, function (queriedlabelConfigurations) {
        labelConfigurations = queriedlabelConfigurations;
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
                pagesAndScenarios = PagesAndStepsService.populatePagesAndStepsService(result);
                vm.useCaseDescription = result.useCase.description;
                vm.scenario = pagesAndScenarios.scenario;
                vm.pagesAndSteps = pagesAndScenarios.pagesAndSteps;
                vm.useCase = result.useCase;
                vm.metadataTree = transformMetadataToTreeArray(pagesAndScenarios.scenario.details);
                vm.scenarioInformationTree = createScenarioInformationTree(vm.scenario, result.scenarioStatistics, vm.useCase);
                scenarioStatistics = result.scenarioStatistics;

                if(SelectedComparison.isDefined()) {
                    var selectedBrandAndBuild = SelectedBranchAndBuildService.selected();
                    loadDiffInfoData(vm.pagesAndSteps, selectedBrandAndBuild.branch, selectedBrandAndBuild.build, SelectedComparison.selected());
                }

                loadRelatedIssues();

                var hasAnyUseCaseLabels = vm.useCase.labels.labels.length > 0;
                var hasAnyScenarioLabels = vm.scenario.labels.labels.length > 0;
                vm.hasAnyLabels = hasAnyUseCaseLabels || hasAnyScenarioLabels;

                if (ConfigService.expandPagesInScenarioOverview()) {
                    vm.expandAll();
                }
            }
        );
    }

    function showAllStepsForPage(pageIndex) {
        return showAllSteps[pageIndex] || false;
    }

    function toggleShowAllStepsForPage(pageIndex) {
        showAllSteps[pageIndex] = !showAllSteps[pageIndex];
    }

    function isExpandAllPossible() {
        if (!angular.isDefined(vm.pagesAndSteps)) {
            return false;
        }

        for (var i = 0; i < vm.pagesAndSteps.length; i++) {
            if (isExpandPossibleForPage(vm.pagesAndSteps[i], i)) {
                return true;
            }
        }

        return false;
    }

    function isExpandPossibleForPage(page, pageIndex) {
        return page.steps.length > 1 && vm.showAllStepsForPage(pageIndex) === false;
    }

    function isCollapseAllPossible() {
        if (!angular.isDefined(vm.pagesAndSteps)) {
            return false;
        }

        for (var i = 0; i < vm.pagesAndSteps.length; i++) {
            if (isCollapsePossibleForPage(vm.pagesAndSteps[i], i)) {
                return true;
            }
        }

        return false;
    }

    function isCollapsePossibleForPage(page, pageIndex) {
        return page.steps.length > 1 && vm.showAllStepsForPage(pageIndex) === true;
    }

    function expandAll() {
        var numberOfPages = scenarioStatistics.numberOfPages;
        for (var i = 0; i < numberOfPages; i++) {
            showAllSteps[i] = true;
        }
    }

    function collapseAll() {
        for (var i = 0; i < showAllSteps.length; i++) {
            showAllSteps[i] = false;
        }
    }

    function getScreenShotUrl(imgName, stepIsRemoved) {
        if (angular.isUndefined(selectedBranchAndBuild)) {
            return undefined;
        }
        var branch = selectedBranchAndBuild.branch;
        var build = selectedBranchAndBuild.build;
        if(SelectedComparison.isDefined() && stepIsRemoved) {
            branch = comparisonBranchName;
            build = comparisonBuildName;
        }

        return 'rest/branch/' + branch + '/build/' + build +
            '/usecase/' + useCaseName + '/scenario/' + scenarioName + '/image/' + imgName;
    }

    function getLinkToStep(pageName, pageOccurrence, stepInPageOccurrence) {
        return '#/step/' + encodeURIComponent(useCaseName) + '/' + encodeURIComponent(scenarioName) + '/' + encodeURIComponent(pageName) +
            '/' + pageOccurrence + '/' + stepInPageOccurrence;
    }

    function resetSearchField() {
        vm.searchFieldText = '';
    }

    function createScenarioInformationTree(scenario, statistics, useCase) {
        var stepInformation: any = {};
        stepInformation['Use Case'] = useCase.name;
        if(useCase.description) {
            stepInformation['Use Case Description'] = useCase.description;
        }
        stepInformation.Scenario = $filter('scHumanReadable')(scenario.name);
        if(scenario.description) {
            stepInformation['Scenario Description'] = scenario.description;
        }

        stepInformation['Number of Pages'] = statistics.numberOfPages;
        stepInformation['Number of Steps'] = statistics.numberOfSteps;
        stepInformation.Status = scenario.status;
        return transformMetadataToTree(stepInformation);
    }

    function loadStepDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps) {
        ScenarioDiffInfoResource.get(
            {
                'baseBranchName': baseBranchName,
                'baseBuildName': baseBuildName,
                'comparisonName': comparisonName,
                'useCaseName': useCaseName,
                'scenarioName': scenarioName
            },
            function onSuccess(scenarioDiffInfo) {
                StepDiffInfosResource.get(
                    {
                        'baseBranchName': baseBranchName,
                        'baseBuildName': baseBuildName,
                        'comparisonName': comparisonName,
                        'useCaseName': useCaseName,
                        'scenarioName': scenarioName
                    },
                    function onSuccess(stepDiffInfos) {
                        DiffInfoService.enrichPagesAndStepsWithDiffInfos(pagesAndSteps, scenarioDiffInfo.removedElements, stepDiffInfos);
                    }
                );
            }, function onFailure(error) {
                throw error;
            }
        );
    }

    function isAddedUseCase(buildDiffInfo) {
        // ES 2015 find() method would be required here...
        var isAddedUseCase = false;
        angular.forEach(buildDiffInfo.addedElements, function (addedElement) {
            if (addedElement === useCaseName) {
                isAddedUseCase = true;
            }
        });
        return isAddedUseCase;
    }

    function markPagesAndStepsAsAdded(pagesAndSteps) {
        angular.forEach(pagesAndSteps, function (pageAndStep) {
            pageAndStep.page.diffInfo = {isAdded: true};
            angular.forEach(pageAndStep.steps, function (step) {
                step.diffInfo = {isAdded: true};
            });
        });
    }

    function loadDiffInfoData(pagesAndSteps, baseBranchName, baseBuildName, comparisonName) {
        if (pagesAndSteps && baseBranchName && baseBuildName && useCaseName && scenarioName) {
            BuildDiffInfoResource.get(
                {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
                function onSuccess(buildDiffInfo) {
                    comparisonBranchName = buildDiffInfo.comparisonBranchName;
                    comparisonBuildName = buildDiffInfo.comparisonBuildName;

                    if (isAddedUseCase(buildDiffInfo)) {
                        markPagesAndStepsAsAdded(pagesAndSteps);
                    } else {
                        loadStepDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps);
                    }
                }, function onFailure(error) {
                    throw error;
                }
            );
        }
    }

    function loadRelatedIssues(){
        RelatedIssueResource.query({
            branchName: SelectedBranchAndBuildService.selected().branch,
            buildName: SelectedBranchAndBuildService.selected().build,
            useCaseName: $routeParams.useCaseName,
            scenarioName: $routeParams.scenarioName
        }, function(result){
            vm.relatedIssues = result;
            vm.hasAnyRelatedIssues = vm.relatedIssues.length > 0;
        });
    }

    function goToIssue(issue) {
        var selectedBranch = SelectedBranchAndBuildService.selected().branch;
        SketchIdsResource.get(
            {'branchName': selectedBranch, 'issueId': issue.id },
            function onSuccess(result) {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    }

    function getLabelStyle(labelName) {
        var labelConfig = labelConfigurations[labelName];
        if (labelConfig) {
            return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
        }
    }
}
