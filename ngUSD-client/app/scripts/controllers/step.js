'use strict';

angular.module('ngUSDClientApp.controllers').controller('StepCtrl', function ($scope, $routeParams, $location, $q, $window, Config, ScenarioService, PageVariantService, StepService) {
    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var selectedBranch = Config.selectedBranch($location);
    var selectedBuild = Config.selectedBuild($location);
    var stepIndex = parseInt($routeParams.stepIndex);
    $scope.pageName = decodeURIComponent($routeParams.pageName);
    $scope.pageIndex = parseInt($routeParams.pageOccurenceInScenario);
    $scope.stepIndex = stepIndex;

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass: 'modal modal-huge'
    };

    $scope.showingMetaData = $window.innerWidth > 1000;


    $q.all([selectedBranch, selectedBuild]).then(function (result) {
        $scope.pageVariantCounts = PageVariantService.getPageVariantCount({'branchName': result[0], 'buildName': result[1]});

        //FIXME this is could be improved. Add information to the getStep call. however with caching it could be fixed as well
        var pagesAndScenarios = ScenarioService.getScenario({'branchName': result[0], 'buildName': result[1], 'usecaseName': useCaseName, 'scenarioName': scenarioName});

        pagesAndScenarios.then(function (result) {
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
        });

        var step = StepService.getStep({'branchName': result[0], 'buildName': result[1], 'usecaseName': useCaseName, 'scenarioName': scenarioName, 'stepIndex': stepIndex});
        step.then(function (result) {
            $scope.step = result;
            beautify(result.html);
        });

        $scope.getScreenShotUrl = function (imgName) {
            return 'rest/branches/' + result[0] + '/builds/' + result[1] + '/usecases/' + useCaseName + '/scenarios/' + scenarioName + '/image/' + imgName;
        };
    });
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

        var output = $window.html_beautify(source, opts);
        $scope.formattedHtml = output;
    }

    function bindStepNavigation(pagesAndSteps) {
        var w = angular.element($window);
        w.bind('keypress', function (event) {
            var keyCode = event.keyCode;
            if (keyCode === 37) {
                if (event.ctrlKey === 1) {
                    $scope.goToPreviousVariant();
                } else {
                    $scope.goToPreviousStep();
                }
            } else if (keyCode === 38) {
                $scope.goToPreviousPage();
            } else if (keyCode === 39) {
                if (event.ctrlKey === 1) {
                    $scope.goToNextVariant();
                } else {
                    $scope.goToNextStep();
                }
            } else if (keyCode === 40) {
                $scope.goToNextPage();
            }
            $scope.$apply();
            return false;
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

    $scope.openScreenshotModal = function () {
        $scope.showingScreenshotModal = true;
    };

    $scope.closeScreenshotModal = function () {
        $scope.showingScreenshotModal = false;
    };
});
