'use strict';

NgUsdClientApp.controller('StepCtrl', function ($scope, $routeParams, $location, Config, ScenarioService) {
    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    $scope.pageName = decodeURIComponent($routeParams.pageName);
    $scope.pageIndex = parseInt($routeParams.pageOccurenceInScenario);
    $scope.stepIndex = parseInt($routeParams.stepIndex);

    var pagesAndScenarios = ScenarioService.getScenario(Config.selectedBranch($location), Config.selectedBuild($location), useCaseName, scenarioName, function(pagesAndScenarios) {
        $scope.scenario = pagesAndScenarios.scenario;
        $scope.pagesAndSteps = pagesAndScenarios.pagesAndSteps;
        $scope.step = getStep($scope.pagesAndSteps, $scope.pageIndex, $scope.stepIndex);
    });

    function getStep(pagesAndSteps, pageNr, stepNr) {
        //FIXME handle if page does (no longer) exist
        if (pagesAndSteps.length<=pageNr) {
            return null;
        }
        return pagesAndSteps[pageNr].steps[stepNr];
    }


    $scope.getScreenShotUrl = function(imgName) {
        return "http://localhost:8050/ngusd/rest/branches/"+Config.selectedBranch($location)+"/builds/"+Config.selectedBuild($location)+"/usecases/"+useCaseName+"/scenarios/"+scenarioName+"/image/"+imgName;
    }

    $scope.go = function(pageSteps, pageIndex, stepIndex) {
        var pageName = pageSteps.page.name;
        $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) + '/' + pageIndex + '/' + stepIndex);
    }

    $scope.goToPreviousPage = function() {
        var pageIndex = $scope.pageIndex-1;
        var stepIndex = 0;
        if (pageIndex<0) {
            pageIndex = 0;
        }
        $scope.go($scope.pagesAndSteps[pageIndex], pageIndex, stepIndex);
    }

    $scope.goToPreviousStep = function() {
        var pageIndex = $scope.pageIndex;
        var stepIndex = $scope.stepIndex-1;
        if ($scope.stepIndex==0) {
            pageIndex = $scope.pageIndex-1;
            stepIndex = $scope.pagesAndSteps[pageIndex].steps.length-1;
        }
        $scope.go($scope.pagesAndSteps[pageIndex], pageIndex, stepIndex);
    }

    $scope.goToNextStep = function() {
        var pageIndex = $scope.pageIndex;
        var stepIndex = $scope.stepIndex+1;
        if (stepIndex >= $scope.pagesAndSteps[$scope.pageIndex].steps.length) {
            pageIndex = $scope.pageIndex+1;
            stepIndex = 0;
        }
        $scope.go($scope.pagesAndSteps[pageIndex], pageIndex, stepIndex);
    }

    $scope.goToNextPage = function() {
        var pageIndex = $scope.pageIndex+1;
        var stepIndex = 0;
        if (pageIndex >= $scope.pagesAndSteps.length) {
            pageIndex = $scope.pagesAndSteps.length-1;
        }
        $scope.go($scope.pagesAndSteps[pageIndex], pageIndex, stepIndex);
    }
});
