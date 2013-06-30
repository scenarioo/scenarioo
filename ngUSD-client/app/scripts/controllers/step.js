'use strict';

NgUsdClientApp.controller('StepCtrl', function ($scope, $routeParams, $location, Config, ScenarioService) {
    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    $scope.pageName = decodeURIComponent($routeParams.pageName);
    $scope.pageIndex = $routeParams.pageOccurenceInScenario;
    $scope.stepIndex = $routeParams.stepIndex;

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
});
