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

angular.module('scenarioo.controllers').controller('StepCtrl', function ($scope, $routeParams, $location, $q, $window, Config, ScenarioResource, PageVariantService, StepService, HostnameAndPort, SelectedBranchAndBuild, $filter, ScApplicationInfoPopup, GlobalHotkeysService) {
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

    $scope.showingMetaData = $window.innerWidth > 1000;
    var metadataExpanded = [];
    metadataExpanded['sc-step-properties'] = true;

    SelectedBranchAndBuild.callOnSelectionChange(loadStep);

    function loadStep(selected) {
        $scope.pageVariantCounts = PageVariantService.getPageVariantCount({'branchName': selected.branch, 'buildName': selected.build});

        //FIXME this is could be improved. Add information to the getStep call. however with caching it could be fixed as well
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
            $scope.scenario = result.scenario;
            $scope.pagesAndSteps = result.pagesAndSteps;
            $scope.stepDescription = result.pagesAndSteps[$scope.pageIndex].steps[$scope.stepIndex];
            bindStepNavigation(result.pagesAndSteps);

            $scope.goToPreviousVariant = function () {
                var previousVariant = $scope.stepDescription.previousStepVariant;
                $location.path('/step/' + previousVariant.useCaseName + '/' + previousVariant.scenarioName + '/' + encodeURIComponent(previousVariant.pageName) + '/' + previousVariant.occurence + '/' + previousVariant.relativeIndex);
            };

            $scope.goToNextVariant = function () {
                var nextStepVariant = $scope.stepDescription.nextStepVariant;
                $location.path('/step/' + nextStepVariant.useCaseName + '/' + nextStepVariant.scenarioName + '/' + encodeURIComponent(nextStepVariant.pageName) + '/' + nextStepVariant.occurence + '/' + nextStepVariant.relativeIndex);
            };

            var step = StepService.getStep({'branchName': selected.branch, 'buildName': selected.build, 'usecaseName': useCaseName, 'scenarioName': scenarioName, 'stepIndex': $scope.stepDescription.index});
            step.then(function (result) {
                $scope.step = result;
                $scope.metadataTree = transformMetadataToTreeArray(result.metadata.details);
                $scope.stepInformationTree = createStepInformationTree(result);
                $scope.pageTree = transformMetadataToTree(result.page);
                beautify(result.html);
            });
        }

        $scope.getScreenShotUrl = function (imgName) {
            if (angular.isDefined(imgName)) {
                return HostnameAndPort.forLink() + '/scenarioo/rest/branches/' + selected.branch + '/builds/' + selected.build + '/usecases/' + useCaseName + '/scenarios/' + scenarioName + '/image/' + imgName;
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
            stepInformation['Page name'] = result.page;
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

        GlobalHotkeysService.registerPageHotKeyCode(37, function () {
            // left arrow
            $scope.goToPreviousStep();
        });
        GlobalHotkeysService.registerPageHotKeyCode(39, function () {
            // right arrow
            $scope.goToNextStep();
        });
        GlobalHotkeysService.registerPageHotKeyCode('shift+37', function () {
            // left arrow
            $scope.goToPreviousPage();
        });
        GlobalHotkeysService.registerPageHotKeyCode('shift+39', function () {
            // right arrow
            $scope.goToNextPage();
        });
        GlobalHotkeysService.registerPageHotKeyCode(38, function () {
            // up arrow
            $scope.goToPreviousVariant();
        });
        GlobalHotkeysService.registerPageHotKeyCode(4, function () {
            // down arrow
            $scope.goToNextVariant();
        });


        $scope.goToPreviousPage = function () {
            var pageIndex = $scope.pageIndex - 1;
            var stepIndex = 0;
            if (pageIndex < 0) {
                pageIndex = 0;
            }
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
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

        $scope.goToNextStep = function () {
            var pageIndex = $scope.pageIndex;
            var stepIndex = $scope.stepIndex + 1;
            if (stepIndex >= pagesAndSteps[$scope.pageIndex].steps.length) {
                pageIndex = $scope.pageIndex + 1;
                stepIndex = 0;
            }
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.goToNextPage = function () {
            var pageIndex = $scope.pageIndex + 1;
            var stepIndex = 0;
            if (pageIndex >= $scope.pagesAndSteps.length) {
                pageIndex = $scope.pagesAndSteps.length - 1;
            }
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.goToFirstPage = function () {
            var pageIndex = 0;
            var stepIndex = 0;
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };

        $scope.goToLastPage = function () {
            var pageIndex = $scope.pagesAndSteps.length - 1;
            var stepIndex = 0;
            $scope.go(pagesAndSteps[pageIndex], pageIndex, stepIndex);
        };
    }

    $scope.go = function (pageSteps, pageIndex, stepIndex) {
        var pageName = pageSteps.page.name;
        $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) + '/' + pageIndex + '/' + stepIndex);
    };

    $scope.isMetadataCollapsed = function (type) {
        var collapsed = angular.isUndefined(metadataExpanded[type]) || metadataExpanded[type] === false;
        return collapsed;
    };

    $scope.toggleMetadataCollapsed = function (type) {
        var currentValue = metadataExpanded[type];
        if (angular.isUndefined(currentValue)) {
            currentValue = false;
        }
        var newValue = !currentValue;
        metadataExpanded[type] = newValue;
    };

});