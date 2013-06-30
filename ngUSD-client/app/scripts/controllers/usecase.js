'use strict';

NgUsdClientApp.controller('UseCaseCtrl', ['$scope', '$routeParams', '$location', 'ScenarioService', 'BuildStateService', 'Config', function ($scope, $routeParams, $location, ScenarioService, BuildStateService, Config) {
    var useCaseName = $routeParams.useCaseName;
    var useCase = ScenarioService.findAllScenarios(Config.selectedBranch($location), Config.selectedBuild($location), useCaseName, function (useCaseAndScenarios) {
        $scope.useCase = useCaseAndScenarios.useCase;
        $scope.scenarios = useCaseAndScenarios.scenarios;
        var states = BuildStateService.ListBuildStates(function (states) {

            angular.forEach($scope.scenarios, function (scenario) {
                scenario.buildStateClass = states[scenario.status];
            });
        });
    });

    $scope.propertiesToShow = Config.scenarioPropertiesInOverview;

    $scope.resetSearchField = function() {
        $scope.searchFieldText = '';
    }

    $scope.go = function(useCaseName, scenarioName) {
        $location.path('/scenario/' +useCaseName + '/' + scenarioName);
    }

    $scope.goToFirstStep = function(useCaseName, scenarioName) {
        //FIXME This could be improved, if the scenario service for finding all scenarios would also retrieve the name of the first page
        var pagesAndScenarios = ScenarioService.getScenario(Config.selectedBranch($location), Config.selectedBuild($location), useCaseName, scenarioName, function(pagesAndScenarios) {
            $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(pagesAndScenarios.pagesAndSteps[0].page.name) + '/0/0');
        });
    }


}]);
