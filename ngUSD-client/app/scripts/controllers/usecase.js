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


    $scope.resetSearchField = function() {
        $scope.searchFieldText = '';
    }

    $scope.go = function(useCaseName, scenarioName) {
        $location.path('/scenario/' +useCaseName + '/' + scenarioName);
    }

}]);
