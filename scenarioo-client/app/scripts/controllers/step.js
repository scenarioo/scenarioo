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
    $scope.pageIndex = parseInt($routeParams.pageIndex, 10);
    $scope.stepIndex = parseInt($routeParams.stepIndex, 10);
    $scope.title = ($scope.pageIndex + 1) + '.' + $scope.stepIndex + ' - ' + $scope.pageName;

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge'
    };

    $scope.showApplicationInfoPopup = function(tab) {
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
            function(result) {
                processScenarioResult(result);
            }
        );

        function processScenarioResult(result) {

            // TODO #197: client should not have to resolve step index from URL, this must be done on server side.
            // if this is done properly it should even not be necessary to load the whole scenario page steps on the client for current step,
            // instead we should enhance the StepNavigation data structure that is already loaded on loading step's data
            $scope.scenario = result.scenario;
            $scope.pagesAndSteps = result.pagesAndSteps;
            $scope.stepDescription = result.pagesAndSteps[$scope.pageIndex].steps[$scope.stepIndex];

            $scope.stepsCountOverall = 0;
            $scope.stepsBeforePage = [];
            for (var indexPage = 0; indexPage < $scope.pagesAndSteps.length; indexPage++) {
                $scope.stepsBeforePage[indexPage] = $scope.stepsCountOverall;
                $scope.stepsCountOverall = $scope.stepsCountOverall + $scope.pagesAndSteps[indexPage].steps.length;
            }

            bindStepNavigation(result.pagesAndSteps);

            var stepPromise = StepService.getStep({'branchName': selected.branch, 'buildName': selected.build, 'usecaseName': useCaseName, 'scenarioName': scenarioName, 'stepIndex': $scope.stepDescription.index});
            stepPromise.then(function (result) {
                $scope.step = result.step;
                $scope.metadataTree = transformMetadataToTreeArray(result.step.metadata.details);
                $scope.stepInformationTree = createStepInformationTree(result.step);
                $scope.pageTree = transformMetadataToTree(result.step.page);
                $scope.stepNavigation = result.stepNavigation;
                $scope.useCaseLabels = result.useCaseLabels;
                $scope.scenarioLabels = result.scenarioLabels;
                beautify(result.step.html);


                $scope.hasAnyLabels = function() {
                    var hasAnyUseCaseLabels = $scope.useCaseLabels.labels.length > 0;
                    var hasAnyScenarioLabels = $scope.scenarioLabels.labels.length > 0;
                    var hasAnyStepLabels = $scope.step.labels.labels.length > 0;
                    var hasAnyPageLabels = $scope.step.page.labels.labels.length > 0;

                    return hasAnyUseCaseLabels || hasAnyScenarioLabels || hasAnyStepLabels || hasAnyPageLabels;
                };
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

        if(angular.isDefined(stepDescription.title)) {
            stepInformation['Step title'] = stepDescription.title;
        }

        if(angular.isDefined(result.page)) {
            var pageToRender = angular.copy(result.page);
            // Will be displayed separately
            delete pageToRender.labels;
            stepInformation['Page name'] = pageToRender;
        }

        if(angular.isDefined(stepDescription.details.url)) {
            stepInformation.URL = stepDescription.details.url;
        }

        if(angular.isDefined(stepDescription.status)) {
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

    function bindStepNavigation(pagesAndSteps) {

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

        $scope.isFirstStep = function() {
            return $scope.stepIndex === 0 && $scope.isFirstPage();
        };

        $scope.goToPreviousStep = function () {
            var pageIndex = $scope.pageIndex;
            var stepIndex = $scope.stepIndex - 1;
            if ($scope.stepIndex === 0) {
                if ($scope.pageIndex === 0) {
                    pageIndex = 0;
                    stepIndex = 0;
                } else {
                    pageIndex = $scope.pageIndex - 1;
                    stepIndex = pagesAndSteps[pageIndex].steps.length - 1;
                }
            }
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.isLastStep = function() {
            var isLastPageOfScenario = $scope.isLastPage();
            var isLastStepOfPage = $scope.stepIndex + 1 >= pagesAndSteps[$scope.pageIndex].steps.length;
            return isLastStepOfPage && isLastPageOfScenario;
        };

        $scope.goToNextStep = function () {
            var pageIndex = $scope.pageIndex;
            var stepIndex = $scope.stepIndex + 1;

            if (stepIndex >= pagesAndSteps[$scope.pageIndex].steps.length) {
                pageIndex = $scope.pageIndex + 1;
                stepIndex = 0;
            }
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.isFirstPage = function() {
            return $scope.pageIndex === 0;
        };

        $scope.goToPreviousPage = function () {
            var pageIndex = $scope.pageIndex - 1;
            var stepIndex = 0;
            if (pageIndex < 0) {
                pageIndex = 0;
            }
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.isLastPage = function() {
            var isLastPageOfScenario = $scope.pageIndex + 1 >= $scope.pagesAndSteps.length;
            return isLastPageOfScenario;
        };

        $scope.goToNextPage = function () {
            var pageIndex = $scope.pageIndex + 1;
            var stepIndex = 0;
            if (pageIndex >= $scope.pagesAndSteps.length) {
                pageIndex = $scope.pagesAndSteps.length - 1;
            }
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.goToFirstStep = function () {
            var pageIndex = 0;
            var stepIndex = 0;
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.goToLastStep = function () {
            var lastPageIndex = $scope.pagesAndSteps.length - 1;
            var lastStepIndex = pagesAndSteps[lastPageIndex].steps.length - 1;
            $scope.go(pagesAndSteps[lastPageIndex], lastPageIndex, lastStepIndex);
        };

        $scope.isFirstPageVariantStep = function() {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.previousStepVariant === null;
        };

        $scope.goToPreviousVariant = function () {
            var previousVariant = $scope.stepNavigation.previousStepVariant;
            $location.path('/step/' + previousVariant.useCaseName + '/' + previousVariant.scenarioName + '/' + encodeURIComponent(previousVariant.pageName) + '/' + previousVariant.pageIndex + '/' + previousVariant.pageStepIndex);
        };

        $scope.isLastPageVariantStep = function() {
            return angular.isUndefined($scope.stepNavigation) || $scope.stepNavigation.nextStepVariant === null;
        };

        $scope.goToNextVariant = function () {
            var nextStepVariant = $scope.stepNavigation.nextStepVariant;
            $location.path('/step/' + nextStepVariant.useCaseName + '/' + nextStepVariant.scenarioName + '/' + encodeURIComponent(nextStepVariant.pageName) + '/' + nextStepVariant.pageIndex + '/' + nextStepVariant.pageStepIndex);
        };

        $scope.getCurrentStepIndex = function() {
            return $scope.stepsBeforePage[$scope.pageIndex] + $scope.stepIndex;
        };

    }

    $scope.go = function (pageSteps, pageIndex, stepIndex) {
        var pageName = pageSteps.page.name;
        $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) + '/' + pageIndex + '/' + stepIndex);
    };

});