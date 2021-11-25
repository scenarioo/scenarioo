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

import {ConfigurationService} from '../services/configuration.service';
import {BuildDiffInfoService} from '../diffViewer/services/build-diff-info.service';
import {UseCaseDiffInfoService} from '../diffViewer/services/use-case-diff-info.service';
import {ScenarioDiffInfoService} from '../diffViewer/services/scenario-diff-info.service';
import {StepDiffInfosService} from '../diffViewer/services/step-diff-infos.service';
import {forkJoin} from 'rxjs';
import {RelatedIssueResource, RelatedIssueSummary} from '../shared/services/relatedIssueResource.service';
import {SketchIdsResource} from '../shared/services/sketchIdsResource.service';
import {ISketchIds} from '../generated-types/backend-types';
import {Url} from '../shared/utils/url';

declare var angular: angular.IAngularStatic;

angular.module('scenarioo.controllers').controller('ScenarioController', ScenarioController);

function ScenarioController($filter, $routeParams,
                            $location, ScenarioResource, SelectedBranchAndBuildService, SelectedComparison,
                            PagesAndStepsService, DiffInfoService, LabelConfigurationsResource,
                            SketchIdsResource: SketchIdsResource,
                            BuildDiffInfoResource: BuildDiffInfoService,
                            ScenarioDiffInfoResource: ScenarioDiffInfoService,
                            UseCaseDiffInfoResource: UseCaseDiffInfoService,
                            StepDiffInfosResource: StepDiffInfosService,
                            ConfigurationService: ConfigurationService,
                            RelatedIssueResource: RelatedIssueResource) {
    const vm = this;
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
    vm.labelConfigurations = {};
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
    vm.comparisonInfo = SelectedComparison.info;

    const useCaseName = $routeParams.useCaseName;
    const scenarioName = $routeParams.scenarioName;
    let comparisonBranchName;
    let comparisonBuildName;
    let selectedBranchAndBuild;
    let pagesAndScenarios: any = [];
    let scenarioStatistics: any = {};
    const showAllSteps = [];
    const transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    const transformMetadataToTree = $filter('scMetadataTreeCreator');

    SelectedBranchAndBuildService.callOnSelectionChange(loadScenario);

    LabelConfigurationsResource.query()
        .subscribe((queriedlabelConfigurations) => {
            vm.labelConfigurations = queriedlabelConfigurations;
        });

    function loadScenario(selected) {
        selectedBranchAndBuild = selected;
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
            },
            useCaseName,
            scenarioName,
        ).subscribe((result) => {
                // Add page to the step to allow search for step- as well as page-properties
                pagesAndScenarios = PagesAndStepsService.populatePagesAndStepsService(result);
                vm.useCaseDescription = result.useCase.description;
                vm.scenario = pagesAndScenarios.scenario;
                vm.pagesAndSteps = pagesAndScenarios.pagesAndSteps;
                vm.useCase = result.useCase;
                vm.metadataTree = transformMetadataToTreeArray(pagesAndScenarios.scenario.details);
                vm.scenarioInformationTree = createScenarioInformationTree(vm.scenario, result.scenarioStatistics, vm.useCase);
                scenarioStatistics = result.scenarioStatistics;

                if (SelectedComparison.isDefined()) {
                    const selectedBrandAndBuild = SelectedBranchAndBuildService.selected();
                    loadDiffInfoData(vm.pagesAndSteps, selectedBrandAndBuild.branch, selectedBrandAndBuild.build, SelectedComparison.selected());
                }

                loadRelatedIssues();

                const hasAnyUseCaseLabels = vm.useCase.labels.labels.length > 0;
                const hasAnyScenarioLabels = vm.scenario.labels.labels.length > 0;
                vm.hasAnyLabels = hasAnyUseCaseLabels || hasAnyScenarioLabels;

                if (ConfigurationService.expandPagesInScenarioOverview()) {
                    vm.expandAll();
                }
            },
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

        for (let i = 0; i < vm.pagesAndSteps.length; i++) {
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

        for (let i = 0; i < vm.pagesAndSteps.length; i++) {
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
        const numberOfPages = scenarioStatistics.numberOfPages;
        for (let i = 0; i < numberOfPages; i++) {
            showAllSteps[i] = true;
        }
    }

    function collapseAll() {
        for (let i = 0; i < showAllSteps.length; i++) {
            showAllSteps[i] = false;
        }
    }

    function getScreenShotUrl(imgName, stepIsRemoved) {
        if (angular.isUndefined(selectedBranchAndBuild)) {
            return undefined;
        }
        let branch = selectedBranchAndBuild.branch;
        let build = selectedBranchAndBuild.build;
        if (SelectedComparison.isDefined() && stepIsRemoved) {
            branch = comparisonBranchName;
            build = comparisonBuildName;
        }

        return Url.encodeComponents `rest/branch/${branch}/build/${build}/usecase/${useCaseName}/scenario/${scenarioName}/image/${imgName}`;
    }

    function getLinkToStep(pageName, pageOccurrence, stepInPageOccurrence) {
        return Url.encodeComponents `#/step/${useCaseName}/${scenarioName}/${pageName}/${pageOccurrence}/${stepInPageOccurrence}`;
    }

    function resetSearchField() {
        vm.searchFieldText = '';
    }

    function createScenarioInformationTree(scenario, statistics, useCase) {
        const stepInformation: any = {};
        stepInformation['Use Case'] = useCase.name;
        if (useCase.description) {
            stepInformation['Use Case Description'] = useCase.description;
        }
        stepInformation.Scenario = $filter('scHumanReadable')(scenario.name);
        if (scenario.description) {
            stepInformation['Scenario Description'] = scenario.description;
        }

        stepInformation['Number of Pages'] = statistics.numberOfPages;
        stepInformation['Number of Steps'] = statistics.numberOfSteps;
        stepInformation.Status = scenario.status;
        return transformMetadataToTree(stepInformation);
    }

    function loadUseCaseDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps) {
        UseCaseDiffInfoResource.get(baseBranchName, baseBuildName, comparisonName, useCaseName)
            .subscribe((useCaseDiffInfo) => {
                if (isAddedScenario(useCaseDiffInfo)) {
                    markPagesAndStepsAsAdded(pagesAndSteps);
                } else {
                    loadStepDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps);
                }
            }, (error) => {
                throw error;
            });

    }

    function loadStepDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps) {
        forkJoin([
            ScenarioDiffInfoResource.get(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
            StepDiffInfosResource.get(baseBranchName, baseBuildName, comparisonName, useCaseName, scenarioName),
        ])
            .subscribe(([scenarioDiffInfo, stepDiffInfos]) => {
                DiffInfoService.enrichPagesAndStepsWithDiffInfos(pagesAndSteps, scenarioDiffInfo.removedElements, stepDiffInfos);
            });
    }

    function isAddedUseCase(buildDiffInfo) {
        return buildDiffInfo.addedElements.find((addedElement) => {
            return addedElement === useCaseName;
        });
    }

    function isAddedScenario(useCaseDiffInfo) {
        return useCaseDiffInfo.addedElements.find((addedElement) => {
            return addedElement === scenarioName;
        });
    }

    function markPagesAndStepsAsAdded(pagesAndSteps) {
        angular.forEach(pagesAndSteps, (pageAndStep) => {
            pageAndStep.page.diffInfo = {isAdded: true};
            angular.forEach(pageAndStep.steps, (step) => {
                step.diffInfo = {isAdded: true};
            });
        });
    }

    function loadDiffInfoData(pagesAndSteps, baseBranchName, baseBuildName, comparisonName) {
        if (pagesAndSteps && baseBranchName && baseBuildName && useCaseName && scenarioName) {
            BuildDiffInfoResource.get(baseBranchName, baseBuildName, comparisonName)
                .subscribe((buildDiffInfo) => {
                    comparisonBranchName = buildDiffInfo.compareBuild.branchName;
                    comparisonBuildName = buildDiffInfo.compareBuild.buildName;

                    if (isAddedUseCase(buildDiffInfo)) {
                        markPagesAndStepsAsAdded(pagesAndSteps);
                    } else {
                        loadUseCaseDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps);
                    }
                });
        }
    }

    function loadRelatedIssues() {
        RelatedIssueResource.getForStepsOverview({
                branchName: SelectedBranchAndBuildService.selected().branch,
                buildName: SelectedBranchAndBuildService.selected().build,
            },
            $routeParams.useCaseName,
            $routeParams.scenarioName,
        ).subscribe((relatedIssueSummary: RelatedIssueSummary[]) => {
            vm.relatedIssues = relatedIssueSummary;
            vm.hasAnyRelatedIssues = relatedIssueSummary != null && relatedIssueSummary.length > 0;
        }, (error) => {
            throw error;
        });
    }

    function goToIssue(issue: RelatedIssueSummary) {
        const selectedBranch = SelectedBranchAndBuildService.selected().branch;
        SketchIdsResource.get(
            selectedBranch,
            issue.id,
        ).subscribe((result: ISketchIds) => {
            $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
        });
    }
}
