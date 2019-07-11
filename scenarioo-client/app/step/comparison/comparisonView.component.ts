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
import {ConfigurationService} from '../../services/configuration.service';
import {BuildDiffInfoService} from '../../diffViewer/services/build-diff-info.service';
import {StepDiffInfoService} from '../../diffViewer/services/step-diff-info.service';

angular
    .module('scenarioo.directives')
    .component('scComparisonView', {
        bindings: {
            step: '<',
            stepIdentifier: '<',
            stepIndex: '<',
        },
        template: require('./comparisonView.html'),
        controller: ComparisonViewController,
    });
function ComparisonViewController($scope, $routeParams, localStorageService, SelectedBranchAndBuildService, ScreenshotUrlService,
                                  SelectedComparison, BranchesAndBuildsService, DiffInfoService,
                                  BuildDiffInfoResource: BuildDiffInfoService,
                                  StepDiffInfoResource: StepDiffInfoService) {
    const ctrl = this;

    const useCaseName = $routeParams.useCaseName;
    const scenarioName = $routeParams.scenarioName;

    // https://ultimatecourses.com/blog/angular-1-5-lifecycle-hooks#onChanges
    this.$onChanges = function (changes) {
        if(ctrl.step && ctrl.stepIdentifier) {
            console.log('onChanges: ');
            console.log(ctrl.step)
            updateStep();
        }
    };

    ctrl.comparisonViewOptions = {
        viewId: getLocalStorageValue('diffViewerStepComparisonViewId', 'SideBySide'),
        changesHighlighted: getLocalStorageBool('diffViewerStepComparisonChangesHighlighted'),
        diffImageColor: undefined,
    };

    ctrl.isComparisonView = (viewId) => ctrl.step && ctrl.step.diffInfo && ctrl.step.diffInfo.isAdded
        ? viewId === 'SideBySide' // fixed side by side view for added steps
        : ctrl.comparisonViewOptions.viewId === viewId;

    function updateStep() {
        const selectedBranchAndBuild = SelectedBranchAndBuildService.selected();
        initScreenshotUrl(selectedBranchAndBuild);
        loadDiffInfoData(selectedBranchAndBuild.branch, selectedBranchAndBuild.build, SelectedComparison.selected());
    }

    function loadDiffInfoData(baseBranchName, baseBuildName, comparisonName) {
        BuildDiffInfoResource.get(baseBranchName, baseBuildName, comparisonName)
            .subscribe((buildDiffInfo) => {
                ctrl.comparisonName = buildDiffInfo.name;
                ctrl.comparisonBranchName = buildDiffInfo.compareBuild.branchName;
                ctrl.comparisonBuildName = buildDiffInfo.compareBuild.buildName;
                initBaseBuildName();
                initBaseBuild();
                initComparisonBuild();
                loadStepDiffInfo();
            }, () => {
                ctrl.comparisonName = '';
                ctrl.comparisonBranchName = '';
                ctrl.comparisonBuildName = '';
            });
    }

    function loadStepDiffInfo() {
        // TODO danielsuter this will log an error 500 for added screenshots
        // failure function will be executed nevertheless, why is that?
        // We can not know if a screenshot is added, before we execute the call
        // see http://stackoverflow.com/questions/22113286/prevent-http-errors-from-being-logged-in-browser-console
        StepDiffInfoResource.get(SelectedBranchAndBuildService.selected().branch,
            SelectedBranchAndBuildService.selected().build,
            ctrl.comparisonName,
            useCaseName,
            scenarioName,
            ctrl.stepIndex)
            .subscribe((result) => {
                    ctrl.comparisonScreenshotName = result.comparisonScreenshotName;
                    DiffInfoService.enrichChangedStepWithDiffInfo(ctrl.step, result);
                    initScreenshotURLs();
                },
                () => {
                    DiffInfoService.enrichChangedStepWithDiffInfo(ctrl.step, null);
                    initDiffScreenShotUrl();
                });
    }

    // This URL is only used internally, not for sharing
    function initScreenshotURLs() {
        initComparisonScreenshotUrl();
        initDiffScreenShotUrl();
    }

    function initComparisonScreenshotUrl() {
        ctrl.comparisonScreenShotUrl = ScreenshotUrlService.getComparisonScreenShotUrl(ctrl.comparisonBranchName, ctrl.comparisonBuildName, ctrl.stepIdentifier.usecaseName, ctrl.stepIdentifier.scenarioName, ctrl.comparisonScreenshotName);
        ctrl.comparisonScreenShotDescription = ctrl.branch;
    }

    // This URL is only used internally, not for sharing
    function initScreenshotUrl(selected: any) {
        if (angular.isUndefined(ctrl.step)) {
            return undefined;
        }

        const imageName = ctrl.step.stepDescription.screenshotFileName;

        if (angular.isUndefined(imageName)) {
            return undefined;
        }

        ctrl.screenShotUrl = 'rest/branch/' + selected.branch + '/build/' + selected.build + '/usecase/' + ctrl.stepIdentifier.usecaseName + '/scenario/' + ctrl.stepIdentifier.scenarioName + '/image/' + imageName;
    }

    // This URL is only used internally, not for sharing
    function initDiffScreenShotUrl() {
        if (!ctrl.step.diffInfo.changeRate || ctrl.step.diffInfo.changeRate === 0 || ctrl.step.diffInfo.isAdded) {
            ctrl.diffScreenShotUrl = ctrl.screenShotUrl;
        } else if (ctrl.stepIdentifier) {
            const branchAndBuild = SelectedBranchAndBuildService.selected();
            const comparisonName = SelectedComparison.selected();
            ctrl.diffScreenShotUrl = ScreenshotUrlService.getDiffScreenShotUrl(ctrl.step, branchAndBuild, comparisonName, ctrl.stepIdentifier.usecaseName, ctrl.stepIdentifier.scenarioName, ctrl.stepIndex);
        }
    }
    function initBaseBuildName() {
        BranchesAndBuildsService.getDisplayNameForBuildName(SelectedBranchAndBuildService.selected().branch, SelectedBranchAndBuildService.selected().build).then((result) => {
            ctrl.baseBuildName = result;
        });
    }

    function initBaseBuild() {
        BranchesAndBuildsService.getBuild(SelectedBranchAndBuildService.selected().branch, SelectedBranchAndBuildService.selected().build).then((result) => {
            if (result) {
                ctrl.baseBuild = result.build;
            }
        });
    }

    function initComparisonBuild() {
        BranchesAndBuildsService.getBuild(ctrl.comparisonBranchName, ctrl.comparisonBuildName).then((result) => {
            if (result) {
                ctrl.comparisonBuild = result.build;
            }
        });
    }

    function getLocalStorageBool(storageKey) {
        return localStorageService.get(storageKey) !== 'false';
    }

    function getLocalStorageValue(storageKey, value) {
        return localStorageService.get(storageKey) || value;
    }
}
