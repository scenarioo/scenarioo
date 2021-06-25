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
import {StepDiffInfoService} from '../diffViewer/services/step-diff-info.service';
import {RelatedIssueResource, RelatedIssueSummary} from '../shared/services/relatedIssueResource.service';

declare var angular: angular.IAngularStatic;

angular.module('scenarioo.controllers').controller('StepController', OldstepController);

function OldstepController($scope, $routeParams, $location, $route, StepResource, SelectedBranchAndBuildService,
                           $filter, ApplicationInfoPopupService, LabelConfigurationsResource,
                           SharePageService, SketcherContextService, SketchIdsResource,
                           SketcherLinkService, SelectedComparisonService,
                           RelatedIssueResource: RelatedIssueResource) {

    const transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    const transformMetadataToTree = $filter('scMetadataTreeCreator');

    let selectedBranchAndBuild: any = {};
    const useCaseName = $routeParams.useCaseName;
    const scenarioName = $routeParams.scenarioName;
    const labels = $location.search().labels;

    $scope.step = null; // loaded later, in activation
    $scope.pageName = $routeParams.pageName;
    $scope.pageOccurrence = parseInt($routeParams.pageOccurrence, 10);
    $scope.stepInPageOccurrence = parseInt($routeParams.stepInPageOccurrence, 10);
    $scope.comparisonInfo = SelectedComparisonService.info;
    $scope.activeTab = getActiveTab();
    $scope.refreshIfComparisonActive = refreshIfComparisonActive;
    $scope.goToNextStep = goToNextStep;
    $scope.collapsePanel = collapsePanel;

    activate();

    function activate() {
        SketcherLinkService.showCreateOrEditSketchLinkInBreadcrumbs('Create Sketch ...', createSketch);
        SelectedBranchAndBuildService.callOnSelectionChange(loadStep);
    }

    /**
     * reload the page to ensure that highlighted changes are properly aligned
     * if comparison highlights are activated and we are on the comparisons tab.
     */
    function refreshIfComparisonActive() {
        if ($scope.isComparisonChangesHighlighted() && $scope.activeTab === 2) {
            $route.reload();
        }
    }

    function createSketch() {
        $location.path('/editor/').search('mode', 'create');
    }

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge',
    };

    LabelConfigurationsResource.query()
        .subscribe((labelConfigurations) => {
            $scope.labelConfigurations = labelConfigurations;
        });

    $scope.showApplicationInfoPopup = (tab) => {
        ApplicationInfoPopupService.showApplicationInfoPopup(tab);
    };

    function loadStep(selected) {
        selectedBranchAndBuild = selected;
        loadStepFromServer(selected);
    }

    function stepResultToVm(result, selected) {
        $scope.stepIdentifier = result.stepIdentifier;
        $scope.fallback = result.fallback;
        $scope.step = result.step;
        $scope.metadataTree = transformMetadataToTreeArray(result.step.metadata.details);
        $scope.stepInformationTree = createStepInformationTree(result.step);
        $scope.pageTree = transformMetadataToTree(result.step.page);
        $scope.stepNavigation = result.stepNavigation;
        $scope.stepStatistics = result.stepStatistics;
        $scope.stepIndex = result.stepNavigation.stepIndex;
        $scope.useCaseLabels = result.useCaseLabels;
        $scope.scenarioLabels = result.scenarioLabels;
        $scope.selectedBuild = selected.buildName;
        $scope.getCurrentStepIndexForDisplay = getCurrentStepIndexForDisplay;
        loadRelatedIssues();
        initScreenshotUrl();

        $scope.hasAnyLabels = () => {
            const hasAnyUseCaseLabels = $scope.useCaseLabels.labels.length > 0;
            const hasAnyScenarioLabels = $scope.scenarioLabels.labels.length > 0;
            const hasAnyStepLabels = $scope.step.stepDescription.labels.labels.length > 0;
            const hasAnyPageLabels = $scope.step.page.labels.labels.length > 0;

            return hasAnyUseCaseLabels || hasAnyScenarioLabels || hasAnyStepLabels || hasAnyPageLabels;
        };

        SharePageService.setPageUrl($scope.getCurrentUrlForSharing());
        SharePageService.setImageUrl($scope.getScreenshotUrlForSharing());

        updateSketcherContextService();
    }

    function loadStepFromServer(selected) {

        StepResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
            },
            useCaseName,
            scenarioName,
            $scope.pageName,
            $scope.pageOccurrence,
            $scope.stepInPageOccurrence,
            labels,
        ).subscribe((result) => stepResultToVm(result, selected),
                (error) => {
                    $scope.stepNotFound = true;
                    $scope.httpResponse = {
                        status: error.status,
                        method: 'GET',
                        url: error.url,
                        data: error.error,
                    };
                },
            );
    }

    function updateSketcherContextService() {
        SketcherContextService.stepIdentifier = {
            branchName: selectedBranchAndBuild.branch,
            buildName: selectedBranchAndBuild.build,
            usecaseName: useCaseName,
            scenarioName,
            pageName: $scope.pageName,
            pageOccurrence: $scope.pageOccurrence,
            stepInPageOccurrence: $scope.stepInPageOccurrence,
        };

        SketcherContextService.screenshotURL = $scope.screenShotUrl;
    }

    function createStepInformationTree(result) {
        const stepDescription = result.stepDescription;

        const stepInformation = {};

        if (angular.isDefined(stepDescription.title)) {
            stepInformation['Step title'] = stepDescription.title;
        }

        if (angular.isDefined(result.page)) {
            const pageToRender = angular.copy(result.page);
            // Will be displayed separately
            delete pageToRender.labels;
            stepInformation['Page name'] = pageToRender;
        }

        if (angular.isDefined(stepDescription.details)) {
            angular.forEach(stepDescription.details, (value, key) => {
                stepInformation[key] = value;
            });
        }

        if (angular.isDefined(stepDescription.status)) {
            stepInformation['Build status'] = stepDescription.status;
        }

        return transformMetadataToTree(stepInformation);
    }

    //  $route.reload necessary because of align diff and real image size
    $scope.setActiveTab = (activeTab) => {
        storeActiveTab(activeTab);
        $route.reload();
    };

    //  $route.reload necessary because of annotation calculation
    $scope.setDefaultTab = () => {
        storeActiveTab(0);
        $route.reload();
    };

    function storeActiveTab(activeTab) {
        sessionStorage.setItem('activeTab', activeTab);
    }

    function getActiveTab() {
        const activeTab: string = sessionStorage.getItem('activeTab');
        if (activeTab == null) {
            return 0;
        }
        if (activeTab === '2' && !$scope.comparisonInfo.isDefined) {
            return 0;
        }
        return angular.isDefined(activeTab) ? parseInt(activeTab, 10) : 0;
    }

    function goToNextStep() {
        if (!$scope.stepNavigation || !$scope.stepNavigation.nextStep) {
            return;
        }
        $scope.go($scope.stepNavigation.nextStep);
    }

    $scope.go = (step) => {
        $location.path('/step/' + (step.useCaseName || useCaseName) + '/' + (step.scenarioName || scenarioName) + '/' + step.pageName + '/' + step.pageOccurrence + '/' + step.stepInPageOccurrence);
    };

    // This URL is only used internally, not for sharing
    function initScreenshotUrl() {
        if (angular.isUndefined($scope.step)) {
            return undefined;
        }

        const imageName = $scope.step.stepDescription.screenshotFileName;

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        const selected = SelectedBranchAndBuildService.selected();

        $scope.screenShotUrl = 'rest/branch/' + selected.branch + '/build/' + selected.build + '/usecase/' + $scope.stepIdentifier.usecaseName + '/scenario/' + $scope.stepIdentifier.scenarioName + '/image/' + imageName;
    }

    $scope.getCurrentUrlForSharing = () => $location.absUrl() + createLabelUrl('&', getAllLabels());

    $scope.getScreenshotUrlForSharing = () => {
        if (SelectedBranchAndBuildService.isDefined() !== true) {
            return undefined;
        }

        return getUrlPartBeforeHash($location.absUrl()) + 'rest/branch/' + encodeURIComponent(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]) +
            '/build/' + encodeURIComponent(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]) +
            '/usecase/' + encodeURIComponent(useCaseName) +
            '/scenario/' + encodeURIComponent(scenarioName) +
            '/pageName/' + encodeURIComponent($scope.pageName) +
            '/pageOccurrence/' + $scope.pageOccurrence +
            '/stepInPageOccurrence/' + $scope.stepInPageOccurrence + '/image.' + getImageFileExtension() + createLabelUrl('?', getAllLabels());
    };

    function getUrlPartBeforeHash(url) {
        return url.split('#')[0];
    }

    const getImageFileExtension = () => {
        if (angular.isUndefined($scope.step)) {
            return '';
        }

        const imageFileName = $scope.step.stepDescription.screenshotFileName;

        if (!angular.isString(imageFileName)) {
            return '';
        }

        const fileNameParts = imageFileName.split('.');
        return fileNameParts[fileNameParts.length - 1];
    };

    const getAllLabels = () => {
        let allLabels = [];
        if ($scope.useCaseLabels && $scope.scenarioLabels && $scope.step) {
            allLabels = allLabels.concat($scope.useCaseLabels.labels).concat($scope.scenarioLabels.labels).concat($scope.step.stepDescription.labels.labels).concat($scope.step.page.labels.labels);
        }
        return allLabels;
    };

    const createLabelUrl = (prefix, labelsForUrl) => {
        if (angular.isUndefined(labelsForUrl) || !angular.isArray(labelsForUrl) || labelsForUrl.length === 0) {
            return '';
        }

        return prefix + 'labels=' + labelsForUrl.map(encodeURIComponent).join();
    };

    $scope.$on('$destroy', () => {
        SharePageService.invalidateUrls();
        SketcherLinkService.hideCreateOrEditSketchLinkInBreadcrumbs();
    });

    function loadRelatedIssues() {
        RelatedIssueResource.get({
            branchName: SelectedBranchAndBuildService.selected().branch,
            buildName: SelectedBranchAndBuildService.selected().build,
            },
            useCaseName,
            scenarioName,
            $scope.pageName,
            $scope.pageOccurrence,
            $scope.stepInPageOccurrence,
        ).subscribe((relatedIssueSummary: RelatedIssueSummary[]) => {
            $scope.relatedIssues = relatedIssueSummary;
            $scope.hasAnyRelatedIssues = $scope.relatedIssues != null && $scope.relatedIssues.length > 0;
            $scope.goToIssue = goToIssue;
        }, (error) => {
            throw error;
        });
    }

    function goToIssue(issue) {
        const selectedBranch = SelectedBranchAndBuildService.selected().branch;
        SketchIdsResource.get(
            {branchName: selectedBranch, issueId: issue.id},
            (result) => {
                $location.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
    }

    function collapsePanel(event) {
        $scope.isPanelCollapsed = event;
    }

    function getCurrentStepIndexForDisplay() {
        if (angular.isUndefined($scope.stepNavigation)) {
            return '?';
        }
        return $scope.stepNavigation.stepIndex + 1;
    }

}
