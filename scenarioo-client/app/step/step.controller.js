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

angular.module('scenarioo.controllers').controller('StepController', StepController);

function StepController($scope, $routeParams, $location, $route, StepResource, SelectedBranchAndBuildService,
                        $filter, ApplicationInfoPopupService, GlobalHotkeysService, LabelConfigurationsResource,
                        SharePageService, SketcherContextService, RelatedIssueResource, SketchIdsResource,
                        SketcherLinkService, BranchesAndBuildsService, ScreenshotUrlService, SelectedComparison, BuildDiffInfoResource,
                        StepDiffInfoResource, DiffInfoService, localStorageService, ConfigService) {

    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    var selectedBranchAndBuild = {};
    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var labels = $location.search().labels;

    $scope.pageName = $routeParams.pageName;
    $scope.pageOccurrence = parseInt($routeParams.pageOccurrence, 10);
    $scope.stepInPageOccurrence = parseInt($routeParams.stepInPageOccurrence, 10);
    $scope.comparisonInfo = SelectedComparison.info;
    $scope.activeTab = getActiveTab();

    $scope.comparisonViewOptions = {
        viewId : getLocalStorageValue('diffViewerStepComparisonViewId', 'SideBySide'),
        changesHighlighted : getLocalStorageBool('diffViewerStepComparisonChangesHighlighted'),
        diffImageColor: undefined
     };

    activate();

    function activate() {
        SketcherLinkService.showCreateOrEditSketchLinkInBreadcrumbs('Create Sketch', createSketch);
    }

    function createSketch() {
        $location.path('/editor/').search('mode', 'create');
    }

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge'
    };

    $scope.getPageNameUrlEncoded = function() {
        return encodeURIComponent($scope.pageName);
    };

    LabelConfigurationsResource.query({}, function (labelConfigurations) {
        $scope.labelConfigurations = labelConfigurations;
    });

    $scope.getLabelStyle = function (labelName) {
        if ($scope.labelConfigurations) {
            var labelConfig = $scope.labelConfigurations[labelName];
            if (labelConfig) {
                return {'background-color': labelConfig.backgroundColor, 'color': labelConfig.foregroundColor};
            }
        }
    };

    $scope.showApplicationInfoPopup = function (tab) {
        ApplicationInfoPopupService.showApplicationInfoPopup(tab);
    };

    SelectedBranchAndBuildService.callOnSelectionChange(loadStep);

    function loadStep(selected) {
        selectedBranchAndBuild = selected;
        bindStepNavigation();
        loadStepFromServer(selected);
    }

    function loadStepFromServer(selected) {
        StepResource.get(
            {
                'branchName': selected.branch,
                'buildName': selected.build,
                'usecaseName': useCaseName,
                'scenarioName': scenarioName,
                'pageName': $scope.pageName,
                'pageOccurrence': $scope.pageOccurrence,
                'stepInPageOccurrence': $scope.stepInPageOccurrence,
                'labels': labels
            },
            function success(result) {
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
                loadRelatedIssues();
                initScreenshotUrl();
                if(SelectedComparison.isDefined()) {
                    loadDiffInfoData(selected.branch, selected.build, SelectedComparison.selected());
                }

                $scope.hasAnyLabels = function () {
                    var hasAnyUseCaseLabels = $scope.useCaseLabels.labels.length > 0;
                    var hasAnyScenarioLabels = $scope.scenarioLabels.labels.length > 0;
                    var hasAnyStepLabels = $scope.step.stepDescription.labels.labels.length > 0;
                    var hasAnyPageLabels = $scope.step.page.labels.labels.length > 0;

                    return hasAnyUseCaseLabels || hasAnyScenarioLabels || hasAnyStepLabels || hasAnyPageLabels;
                };

                SharePageService.setPageUrl($scope.getCurrentUrlForSharing());
                SharePageService.setImageUrl($scope.getScreenshotUrlForSharing());

                updateSketcherContextService();
            },
            function error(result) {
                $scope.stepNotFound = true;
                $scope.httpResponse = {
                    status: result.status,
                    method: result.config.method,
                    url: result.config.url,
                    data: result.data
                };
            }
        );
    }

    function updateSketcherContextService() {
        SketcherContextService.stepIdentifier = {
            branchName: selectedBranchAndBuild.branch,
            buildName: selectedBranchAndBuild.build,
            usecaseName: useCaseName,
            scenarioName: scenarioName,
            pageName: $scope.pageName,
            pageOccurrence: $scope.pageOccurrence,
            stepInPageOccurrence: $scope.stepInPageOccurrence
        };

        SketcherContextService.screenshotURL = $scope.screenShotUrl;
    }

    function createStepInformationTree(result) {
        var stepDescription = result.stepDescription;

        var stepInformation = {};

        if (angular.isDefined(stepDescription.title)) {
            stepInformation['Step title'] = stepDescription.title;
        }

        if (angular.isDefined(result.page)) {
            var pageToRender = angular.copy(result.page);
            // Will be displayed separately
            delete pageToRender.labels;
            stepInformation['Page name'] = pageToRender;
        }

        if (angular.isDefined(stepDescription.details)) {
            angular.forEach(stepDescription.details, function (value, key) {
                stepInformation[key] = value;
            });
        }

        if (angular.isDefined(stepDescription.status)) {
            stepInformation['Build status'] = stepDescription.status;
        }

        return transformMetadataToTree(stepInformation);
    }

    function bindStepNavigation() {
        $scope.isFirstStep = isFirstStep;
        $scope.isFirstPage = isFirstPage;
        $scope.isLastPage = isLastPage;
        $scope.goToPreviousStep = goToPreviousStep;
        $scope.goToNextStep = goToNextStep;
        $scope.goToPreviousPage = goToPreviousPage;
        $scope.goToNextPage = goToNextPage;
        $scope.goToFirstStep = goToFirstStep;
        $scope.goToLastStep = goToLastStep;
        $scope.isFirstPageVariantStep = isFirstPageVariantStep;
        $scope.goToPreviousVariant = goToPreviousVariant;
        $scope.isLastPageVariantStep = isLastPageVariantStep;
        $scope.goToNextVariant = goToNextVariant;
        $scope.getCurrentStepIndexForDisplay = getCurrentStepIndexForDisplay;
        $scope.getCurrentPageIndexForDisplay = getCurrentPageIndexForDisplay;
        $scope.getStepIndexInCurrentPageForDisplay = getStepIndexInCurrentPageForDisplay;
        $scope.getNumberOfStepsInCurrentPageForDisplay = getNumberOfStepsInCurrentPageForDisplay;
        $scope.isLastStep = isLastStep;

        GlobalHotkeysService.registerPageHotkeyCode(37, goToPreviousStep); // left arrow
        GlobalHotkeysService.registerPageHotkeyCode(39, goToNextStep); // right arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+37', goToPreviousPage); // control + left arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+39', goToNextPage); // control + right arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+36', goToFirstStep); // control + HOME
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+35', goToLastStep); // control + END
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+38', goToPreviousVariant); // control + up arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+40', goToNextVariant); // control + down arrow

        function isFirstStep() {
            return $scope.stepNavigation && $scope.stepNavigation.stepIndex === 0;
        }

        function isLastStep() {
            return $scope.stepNavigation && $scope.stepNavigation.stepIndex === $scope.stepStatistics.totalNumberOfStepsInScenario - 1;
        }

        function isFirstPage() {
            return $scope.stepNavigation && $scope.stepNavigation.pageIndex === 0;
        }

        function isLastPage() {
            return $scope.stepNavigation && $scope.stepNavigation.pageIndex === $scope.stepStatistics.totalNumberOfPagesInScenario - 1;
        }

        function goToPreviousStep() {
            if (!$scope.stepNavigation || !$scope.stepNavigation.previousStep) {
                return;
            }
            $scope.go($scope.stepNavigation.previousStep);
        }

        function goToNextStep() {
            if (!$scope.stepNavigation || !$scope.stepNavigation.nextStep) {
                return;
            }
            $scope.go($scope.stepNavigation.nextStep);
        }


        function goToPreviousPage() {
            if (!$scope.stepNavigation || !$scope.stepNavigation.previousPage) {
                return;
            }
            $scope.go($scope.stepNavigation.previousPage);
        }

        function goToNextPage() {
            if (!$scope.stepNavigation || !$scope.stepNavigation.nextPage) {
                return;
            }
            $scope.go($scope.stepNavigation.nextPage);
        }

        function goToFirstStep() {
            if (!$scope.stepNavigation || !$scope.stepNavigation.firstStep) {
                return;
            }
            $scope.go($scope.stepNavigation.firstStep);
        }

        function goToLastStep() {
            if (!$scope.stepNavigation || !$scope.stepNavigation.lastStep) {
                return;
            }
            $scope.go($scope.stepNavigation.lastStep);
        }

        function isFirstPageVariantStep() {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.previousStepVariant === null;
        }

        function goToPreviousVariant() {
            if($scope.isFirstPageVariantStep()) {
                return;
            }
            $scope.go($scope.stepNavigation.previousStepVariant);
        }

        function isLastPageVariantStep() {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.nextStepVariant === null;
        }

        function goToNextVariant() {
            if($scope.isLastPageVariantStep()) {
                return;
            }
            $scope.go($scope.stepNavigation.nextStepVariant);
        }

        function getCurrentStepIndexForDisplay() {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.stepIndex + 1;
        }

        function getCurrentPageIndexForDisplay() {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.pageIndex + 1;
        }

        function getStepIndexInCurrentPageForDisplay() {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.stepInPageOccurrence + 1;
        }

        function getNumberOfStepsInCurrentPageForDisplay() {
            if (angular.isUndefined($scope.stepStatistics)) {
                return '?';
            }
            return $scope.stepStatistics.totalNumberOfStepsInPageOccurrence;
        }
    }

    //  $route.reload necessary because of align diff and real image size
    $scope.setActiveTab = function (activeTab) {
        storeActiveTab(activeTab);
        $route.reload();
    };

    //  $route.reload necessary because of annotation calculation
    $scope.setDefaultTab = function() {
        storeActiveTab(0);
        $route.reload();
    };

    function storeActiveTab(activeTab){
        sessionStorage.setItem('activeTab', activeTab);
    }
    function getActiveTab() {
        var activeTab = sessionStorage.getItem('activeTab');
        if (activeTab == null){
            return 0;
        }
        if(activeTab == 2 && !$scope.comparisonInfo.isDefined) {
            return 0;
        }
        return angular.isDefined(activeTab) ? parseInt(activeTab) : 0;
    }

    // This URL is only used internally, not for sharing
    function initScreenshotUrl(){
        if (angular.isUndefined($scope.step)) {
            return undefined;
        }

        var imageName = $scope.step.stepDescription.screenshotFileName;

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        var selected = SelectedBranchAndBuildService.selected();

        $scope.screenShotUrl = 'rest/branch/' + selected.branch + '/build/' + selected.build + '/usecase/' + $scope.stepIdentifier.usecaseName + '/scenario/' + $scope.stepIdentifier.scenarioName + '/image/' + imageName;
    }

    // This URL is only used internally, not for sharing
    function initScreenshotURLs(){
        initComparisonScreenshotUrl ();
        initDiffScreenShotUrl();
    }

    function initComparisonScreenshotUrl () {
        $scope.comparisonScreenShotUrl = ScreenshotUrlService.getComparisonScreenShotUrl($scope.comparisonBranchName, $scope.comparisonBuildName, $scope.stepIdentifier.usecaseName, $scope.stepIdentifier.scenarioName, $scope.comparisonScreenshotName);
        $scope.comparisonScreenShotDescription = $scope.branch;
    }

    function initBaseBuildName() {
        BranchesAndBuildsService.getDisplayNameForBuildName(SelectedBranchAndBuildService.selected().branch, SelectedBranchAndBuildService.selected().build).then(function(result){
            $scope.baseBuildName = result;
        });
    }

    function initBaseBuild(){
        BranchesAndBuildsService.getBuild(SelectedBranchAndBuildService.selected().branch, SelectedBranchAndBuildService.selected().build).then(function(result){
            if(result){
                $scope.baseBuild = result.build;
            }
        });
    }

    function initComparisonBuild(){
        BranchesAndBuildsService.getBuild($scope.comparisonBranchName, $scope.comparisonBuildName).then(function(result){
            if(result){
                $scope.comparisonBuild = result.build;
            }
        });
    }

    // This URL is only used internally, not for sharing
    function initDiffScreenShotUrl() {
        if (!$scope.step.diffInfo.changeRate || $scope.step.diffInfo.changeRate === 0 || $scope.step.diffInfo.isAdded){
            $scope.diffScreenShotUrl = $scope.screenShotUrl;
        } else if ($scope.stepIdentifier) {
            var branchAndBuild = SelectedBranchAndBuildService.selected();
            var comparisonName = SelectedComparison.selected();
            $scope.diffScreenShotUrl = ScreenshotUrlService.getDiffScreenShotUrl($scope.step, branchAndBuild, comparisonName, $scope.stepIdentifier.usecaseName, $scope.stepIdentifier.scenarioName, $scope.stepIndex );
        }
    }

    function loadDiffInfoData(baseBranchName, baseBuildName, comparisonName) {
        BuildDiffInfoResource.get(
            {'baseBranchName': baseBranchName, 'baseBuildName': baseBuildName, 'comparisonName': comparisonName},
            function onSuccess(buildDiffInfo) {
                $scope.comparisonName = buildDiffInfo.name;
                $scope.comparisonBranchName = buildDiffInfo.comparisonBranchName;
                $scope.comparisonBuildName = buildDiffInfo.comparisonBuildName;
                initBaseBuildName();
                initBaseBuild();
                initComparisonBuild();
                loadStepDiffInfo();
            }, function onFailure(){
                $scope.comparisonName = '';
                $scope.comparisonBranchName = '';
                $scope.comparisonBuildName = '';
            }
        );
    }

    function loadStepDiffInfo() {
        // TODO danielsuter this will log an error 500 for added screenshots
        // failure function will be executed nevertheless, why is that?
        // We can not know if a screenshot is added, before we execute the call
        // see http://stackoverflow.com/questions/22113286/prevent-http-errors-from-being-logged-in-browser-console
        StepDiffInfoResource.get(
            {
            baseBranchName: SelectedBranchAndBuildService.selected().branch,
            baseBuildName: SelectedBranchAndBuildService.selected().build,
            comparisonName: $scope.comparisonName,
            useCaseName: useCaseName,
            scenarioName: scenarioName,
            stepIndex: $scope.stepIndex
            },
            function onSuccess(result){
                $scope.comparisonScreenshotName = result.comparisonScreenshotName;
                DiffInfoService.enrichChangedStepWithDiffInfo($scope.step, result);
                initScreenshotURLs();
            },
            function onFailure() {
                DiffInfoService.enrichChangedStepWithDiffInfo($scope.step, null);
                initDiffScreenShotUrl();
            });
    }

     $scope.setComparisonView = function(viewId) {
        $scope.comparisonViewOptions.viewId = viewId;
        setLocalStorageValue('diffViewerStepComparisonViewId', viewId);
    };

    $scope.isComparisonView = function(viewId) {
        return $scope.comparisonViewOptions.viewId === viewId;
    };

    $scope.switchComparisonSingleScreenView = function() {
        var viewId = $scope.isComparisonView('CurrentScreen') ? 'OtherScreen' : 'CurrentScreen';
        $scope.setComparisonView(viewId);
    };

    $scope.isComparisonChangesToBeHighlightedAvailable = function() {
        return $scope.step.diffInfo.changeRate !== 0 && !$scope.step.diffInfo.isAdded;
    };

    $scope.isComparisonChangesHighlighted = function() {
        // highlighting is turned on, and there are changes in this screenshot to be highlighted
        return $scope.isComparisonChangesToBeHighlightedAvailable() && $scope.comparisonViewOptions.changesHighlighted;
    };

    $scope.toggleComparisonChangesHighlighted = function() {
        $scope.comparisonViewOptions.changesHighlighted = !$scope.comparisonViewOptions.changesHighlighted;
        setLocalStorageValue('diffViewerStepComparisonChangesHighlighted', $scope.comparisonViewOptions.changesHighlighted);
    };

    $scope.getComparisonViewHighlightChangesColor = function() {
        return ConfigService.diffViewerDiffImageColor();
    };

    function getLocalStorageBool(storageKey){
        return localStorageService.get(storageKey) !== 'false';
    }

    function getLocalStorageValue(storageKey, value){
        return localStorageService.get(storageKey) || value;
    }

    function setLocalStorageValue(storageKey, value){
        localStorageService.set(storageKey, '' + value);
    }

    $scope.go = function (step) {
        $location.path('/step/' + (step.useCaseName || useCaseName) + '/' + (step.scenarioName || scenarioName) + '/' + step.pageName + '/' + step.pageOccurrence + '/' + step.stepInPageOccurrence);
    };

    $scope.getCurrentUrlForSharing = function () {
        return $location.absUrl() + createLabelUrl('&', getAllLabels());
    };

    $scope.getCurrentUrl = function () {
        return $location.absUrl();
    };

    $scope.getScreenshotUrlForSharing = function () {
        if (SelectedBranchAndBuildService.isDefined() !== true) {
            return undefined;
        }

        return 'rest/branch/' + SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY] +
            '/build/' + SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY] +
            '/usecase/' + encodeURIComponent(useCaseName) +
            '/scenario/' + encodeURIComponent(scenarioName) +
            '/pageName/' + encodeURIComponent($scope.pageName) +
            '/pageOccurrence/' + $scope.pageOccurrence +
            '/stepInPageOccurrence/' + $scope.stepInPageOccurrence + '/image.' + getImageFileExtension() + createLabelUrl('?', getAllLabels());
    };

    var getImageFileExtension = function () {
        if (angular.isUndefined($scope.step)) {
            return '';
        }

        var imageFileName = $scope.step.stepDescription.screenshotFileName;

        if (!angular.isString(imageFileName)) {
            return '';
        }

        var fileNameParts = imageFileName.split('.');
        return fileNameParts[fileNameParts.length - 1];
    };

    var getAllLabels = function () {
        var allLabels = [];
        if ($scope.useCaseLabels && $scope.scenarioLabels && $scope.step) {
            allLabels = allLabels.concat($scope.useCaseLabels.labels).concat($scope.scenarioLabels.labels).concat($scope.step.stepDescription.labels.labels).concat($scope.step.page.labels.labels);
        }
        return allLabels;
    };

    var createLabelUrl = function (prefix, labelsForUrl) {
        if (angular.isUndefined(labelsForUrl) || !angular.isArray(labelsForUrl) || labelsForUrl.length === 0) {
            return '';
        }

        return prefix + 'labels=' + labelsForUrl.map(encodeURIComponent).join();
    };

    $scope.$on('$destroy', function () {
        SharePageService.invalidateUrls();
        SketcherLinkService.hideCreateOrEditSketchLinkInBreadcrumbs();
    });

    function loadRelatedIssues() {
        RelatedIssueResource.query({
            branchName: SelectedBranchAndBuildService.selected().branch,
            buildName: SelectedBranchAndBuildService.selected().build,
            useCaseName: useCaseName,
            scenarioName: scenarioName,
            pageName: $scope.pageName,
            pageOccurence: $scope.pageOccurrence,
            stepInPageOccurrence: $scope.stepInPageOccurrence
        }, function(result){
            $scope.relatedIssues = result;
            $scope.hasAnyRelatedIssues = function(){
                return $scope.relatedIssues.length > 0;
            };
            $scope.goToIssue = goToIssue;
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

}
