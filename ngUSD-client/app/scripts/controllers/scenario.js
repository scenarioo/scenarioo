'use strict';

NgUsdClientApp.controller('ScenarioCtrl', ['$scope', '$filter', '$routeParams', '$location', 'ScenarioService','Config', function ($scope, $filter, $routeParams, $location, ScenarioService, Config) {
    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var pagesAndScenarios = ScenarioService.getScenario(Config.selectedBranch($location), Config.selectedBuild($location), useCaseName, scenarioName, function(pagesAndScenarios) {
        // Add page to the step to allow search for step- as well as page-properties
        populatePageAndSteps(pagesAndScenarios);

        $scope.resetSearchField = function() {
            $scope.searchFieldText = '';
        }

        $scope.showingSteps = [];

        $scope.go = function(pageSteps, pageIndex, stepIndex) {
            var pageName = pageSteps.page.name;
            $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) + '/' + pageIndex + '/' + stepIndex);
        }

        $scope.getScreenShotUrl = function(imgName) {
            return "http://localhost:8050/ngusd/rest/branches/"+Config.selectedBranch($location)+"/builds/"+Config.selectedBuild($location)+"/usecases/"+useCaseName+"/scenarios/"+scenarioName+"/image/"+imgName;
        }
    });

    function populatePageAndSteps(pagesAndScenarios) {
        for (var indexPage = 0; indexPage < pagesAndScenarios.pagesAndSteps.length; indexPage++) {
            var page = pagesAndScenarios.pagesAndSteps[indexPage];
            page.index = indexPage+1;
            for (var indexStep = 0; indexStep < page.steps.length; indexStep++) {
                var step = page.steps[indexStep];
                step.page = page.page;
                step.index = indexStep;
                step.number = (indexStep==0) ? page.index : page.index + "." + indexStep;
                if (!step.title) {
                    step.title = 'undefined';
                }
            }
        }

        $scope.scenario = pagesAndScenarios.scenario;
        $scope.pagesAndSteps = pagesAndScenarios.pagesAndSteps;
    }

    $scope.openScreenshotModal = function() {
        $scope.showingScreenshotModal = true;
    }

    $scope.closeScreenshotModal = function() {
        $scope.showingScreenshotModal = false;
    }

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass:'modal modal-huge'
    };

    $scope.openScreenshotModal = function(step) {
        step.showingScreenshotModal = true;
    }

    $scope.closeScreenshotModal = function(step) {
        step.showingScreenshotModal = false;
    }

    $scope.modalScreenshotOptions = {
        backdropFade: true,
        dialogClass:'modal modal-huge'
    };
}]);
