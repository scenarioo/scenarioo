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

angular.module('scenarioo.controllers').controller('StepCtrl', function ($scope, $routeParams, $location, $q, $window, localStorageService, Config, ScenarioResource, StepService, HostnameAndPort, SelectedBranchAndBuild, $filter, ScApplicationInfoPopup, GlobalHotkeysService) {

    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;

    var transformMetadataToTreeArray = $filter('scMetadataTreeListCreator');
    var transformMetadataToTree = $filter('scMetadataTreeCreator');

    $scope.pageName = decodeURIComponent($routeParams.pageName);
    $scope.pageOccurrence = parseInt($routeParams.pageOccurrence, 10);
    $scope.stepInPageOccurrence = parseInt($routeParams.stepInPageOccurrence, 10);

    // TODO  [#238] It does not make sense to have the pageOccurence and stepInPageOccurence here,
    // so I commented it out. What shall we do with it?
    // $scope.title = ($scope.pageOccurrence + 1) + '.' + $scope.stepInPageOccurrence + ' - ' + $scope.pageName;
    $scope.title = $scope.pageName;

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge'
    };

    $scope.showApplicationInfoPopup = function (tab) {
        ScApplicationInfoPopup.showApplicationInfoPopup(tab);
    };

    SelectedBranchAndBuild.callOnSelectionChange(loadStep);

    function loadStep(selected) {

        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName,
                scenarioName: scenarioName
            },
            function (result) {
                processScenarioResult(result);
            }
        );

        function processScenarioResult(result) {

            // TODO #197: client should not have to resolve step index from URL, this must be done on server side.
            // if this is done properly it should even not be necessary to load the whole scenario page steps on the client for current step,
            // instead we should enhance the StepNavigation data structure that is already loaded on loading step's data

            $scope.scenario = result.scenario;
            $scope.pagesAndSteps = result.pagesAndSteps;
            // TODO loop through the pages and find the correct one
            // TODO get step description from step resource
            // $scope.stepDescription = result.pagesAndSteps[$scope.pageIndex].steps[$scope.stepIndex];
            $scope.stepDescription = 'TODO';

            $scope.stepsCountOverall = 0;
            $scope.stepsBeforePage = [];
            for (var indexPage = 0; indexPage < $scope.pagesAndSteps.length; indexPage++) {
                $scope.stepsBeforePage[indexPage] = $scope.stepsCountOverall;
                $scope.stepsCountOverall = $scope.stepsCountOverall + $scope.pagesAndSteps[indexPage].steps.length;
            }

            bindStepNavigation();

            var stepPromise = StepService.getStep({'branchName': selected.branch, 'buildName': selected.build,
                'usecaseName': useCaseName, 'scenarioName': scenarioName, 'pageName': $scope.pageName,
                'pageOccurrence': $scope.pageOccurrence, 'stepInPageOccurrence': $scope.stepInPageOccurrence});
            stepPromise.then(function (result) {
                $scope.step = result.step;
                $scope.metadataTree = transformMetadataToTreeArray(result.step.metadata.details);
                $scope.stepInformationTree = createStepInformationTree(result.step);
                $scope.pageTree = transformMetadataToTree(result.step.page);
                $scope.stepNavigation = result.stepNavigation;
                $scope.stepStatistics = result.stepStatistics;
                $scope.stepIndex = result.stepNavigation.stepIndex;
                beautify(result.step.html);
            });
        }

        $scope.getScreenShotUrl = function (imgName) {
            if (angular.isDefined(imgName)) {
                return HostnameAndPort.forLink() + 'rest/branches/' + selected.branch + '/builds/' + selected.build + '/usecases/' + useCaseName + '/scenarios/' + scenarioName + '/image/' + imgName;
            } else {
                return '';
            }
        };
    }

    function createStepInformationTree(result) {
        var stepDescription = result.stepDescription;

        var stepInformation = {};

        if (angular.isDefined(stepDescription.title)) {
            stepInformation['Step title'] = stepDescription.title;
        }

        if (angular.isDefined(result.page)) {
            stepInformation['Page name'] = result.page;
        }

        if (angular.isDefined(stepDescription.details.url)) {
            stepInformation.URL = stepDescription.details.url;
        }

        if (angular.isDefined(stepDescription.status)) {
            stepInformation['Build status'] = stepDescription.status;
        }

        return transformMetadataToTree(stepInformation);
    }

    function beautify(html) {
        var source = html.htmlSource;
        var opts = {};

        opts.indent_size = 1;
        opts.indent_char = '\t';
        opts.max_preserve_newlines = 0;
        opts.preserve_newlines = opts.max_preserve_newlines !== -1;
        opts.keep_array_indentation = true;
        opts.break_chained_methods = true;
        opts.indent_scripts = 'normal';
        opts.brace_style = 'collapse';
        opts.space_before_conditional = true;
        opts.unescape_strings = true;
        opts.wrap_line_length = 0;
        opts.space_after_anon_function = true;

        // TODO: fix html_beautify, issue #66
        // var output = $window.html_beautify(source, opts);
        // $scope.formattedHtml = output;

        $scope.formattedHtml = source;
    }

    function bindStepNavigation() {

        GlobalHotkeysService.registerPageHotkeyCode(37, function () {
            // left arrow
            $scope.goToPreviousStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode(39, function () {
            // right arrow
            $scope.goToNextStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+37', function () {
            // control + left arrow
            $scope.goToPreviousPage();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+39', function () {
            // control + right arrow
            $scope.goToNextPage();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+36', function () {
            // control + Home
            $scope.goToFirstStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+35', function () {
            // control + down arrow
            $scope.goToLastStep();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+38', function () {
            // control + up arrow
            $scope.goToPreviousVariant();
        });
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+40', function () {
            // control + down arrow
            $scope.goToNextVariant();
        });

        $scope.isFirstStep = function () {
            return $scope.stepNavigation && $scope.stepNavigation.stepIndex === 0;
        };

        $scope.isLastStep = function () {
            return $scope.stepNavigation && $scope.stepNavigation.stepIndex === $scope.stepStatistics.totalNumberOfStepsInScenario - 1;
        };

        $scope.isFirstPage = function () {
            return $scope.stepNavigation && $scope.stepNavigation.pageIndex === 0;
        };

        $scope.isLastPage = function () {
            return $scope.stepNavigation && $scope.stepNavigation.pageIndex === $scope.stepStatistics.totalNumberOfPagesInScenario - 1;
        };

        $scope.goToPreviousStep = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.previousStep) {
                return;
            }
            $scope.go($scope.stepNavigation.previousStep);
        };

        $scope.goToNextStep = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.nextStep) {
                return;
            }
            $scope.go($scope.stepNavigation.nextStep);
        };

        $scope.goToPreviousPage = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.previousPage) {
                return;
            }
            $scope.go($scope.stepNavigation.previousPage);
        };

        $scope.goToNextPage = function () {
            if (!$scope.stepNavigation || !$scope.stepNavigation.nextPage) {
                return;
            }
            $scope.go($scope.stepNavigation.nextPage);
        };

        $scope.goToFirstStep = function () {
            // TODO
            $scope.go('firstStep', 0, 0);
        };

        $scope.goToLastStep = function () {
            // TODO
            $scope.go('lastStep', 1, 1);
        };

        $scope.isFirstPageVariantStep = function () {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.previousStepVariant === null;
        };

        $scope.goToPreviousVariant = function () {
            var previousVariant = $scope.stepNavigation.previousStepVariant;
            $location.path('/step/' + previousVariant.useCaseName + '/' + previousVariant.scenarioName + '/' + encodeURIComponent(previousVariant.pageName) + '/' + previousVariant.pageIndex + '/' + previousVariant.pageStepIndex);
        };

        $scope.isLastPageVariantStep = function () {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.nextStepVariant === null;
        };

        $scope.goToNextVariant = function () {
            var nextStepVariant = $scope.stepNavigation.nextStepVariant;
            $location.path('/step/' + nextStepVariant.useCaseName + '/' + nextStepVariant.scenarioName + '/' + encodeURIComponent(nextStepVariant.pageName) + '/' + nextStepVariant.pageIndex + '/' + nextStepVariant.pageStepIndex);
        };

        $scope.getCurrentStepIndexForDisplay = function () {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.stepIndex + 1;
        };

        $scope.getCurrentPageIndexForDisplay = function () {
            if (angular.isUndefined($scope.stepNavigation)) {
                return '?';
            }
            return $scope.stepNavigation.pageIndex + 1;
        }
    }

    $scope.go = function (step) {
        $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(step.pageName) + '/' + step.pageOccurrence + '/' + step.stepInPageOccurrence);
    };

    var STEP_METADATA_SECTION_EXPANDED = 'scenarioo-stepMetadataSectionExpanded-';
    var STEP_METADATA_VISIBLE = 'scenarioo-stepMetadataVisible';

    $scope.isMetadataExpanded = function (type) {
        var metadataExpanded = localStorageService.get(STEP_METADATA_SECTION_EXPANDED + type);
        if (metadataExpanded === 'true') {
            return true;
        } else {
            return false;
        }
    };

    $scope.toggleMetadataExpanded = function (type) {
        var metadataExpanded = !$scope.isMetadataExpanded(type);
        localStorageService.set(STEP_METADATA_SECTION_EXPANDED + type, '' + metadataExpanded);
    };

    $scope.isMetadataCollapsed = function (type) {
        return !$scope.isMetadataExpanded(type);
    };

    $scope.toggleShowingMetadata = function () {
        $scope.showingMetaData = !$scope.showingMetaData;
        localStorageService.set(STEP_METADATA_VISIBLE, '' + $scope.showingMetaData);
    };

    /**
     * Init metadata visibility and expanded sections from local storage on startup.
     */
    function initMetadataVisibilityAndExpandedSections() {

        // Init metadata visibility from local storage
        var metadataVisible = localStorageService.get(STEP_METADATA_VISIBLE);
        if (metadataVisible === 'true') {
            $scope.showingMetaData = true;
        }
        else if (metadataVisible === 'false') {
            $scope.showingMetaData = false;
        } else {
            // default
            $scope.showingMetaData = $window.innerWidth > 800;
        }

        // Set special step metadata to expanded by default.
        var majorStepPropertiesExpanded = localStorageService.get(STEP_METADATA_SECTION_EXPANDED + 'sc-step-properties');
        var isMajorStepPropertiesExpandedSetToFalse = majorStepPropertiesExpanded === 'false';
        if (!isMajorStepPropertiesExpandedSetToFalse) {
            localStorageService.set(STEP_METADATA_SECTION_EXPANDED + 'sc-step-properties', 'true');
        }

    }

    initMetadataVisibilityAndExpandedSections();

});