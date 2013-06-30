'use strict';

NgUsdClientApp.controller('ScenarioCtrl', ['$scope', '$routeParams', '$location', 'ScenarioService','Config', function ($scope, $routeParams,$location, ScenarioService, Config) {
    var useCaseName = $routeParams.useCaseName;
    var scenarioName = $routeParams.scenarioName;
    var pagesAndScenarios = ScenarioService.getScenario(Config.selectedBranch($location), Config.selectedBuild($location), useCaseName, scenarioName, function(pagesAndScenarios) {
        $scope.scenario = pagesAndScenarios.scenario;
        $scope.pagesAndSteps = pagesAndScenarios.pagesAndSteps;


        $scope.resetSearchField = function() {
            $scope.searchFieldText = '';
        }

        $scope.go = function(pageSteps, pageIndex, stepIndex) {
            var pageName = pageSteps.page.name;
            $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pageName) + '/' + pageIndex + '/' + stepIndex);
        }

        $scope.getScreenShotUrl = function(imgName) {
            return "http://localhost:8050/ngusd/rest/branches/"+Config.selectedBranch($location)+"/builds/"+Config.selectedBuild($location)+"/usecases/"+useCaseName+"/scenarios/"+scenarioName+"/image/"+imgName;
        }
    });

}]);
