'use strict';

NgUsdClientApp.controller('UseCaseCtrl', ['$scope', '$routeParams', '$location', '$cookieStore', 'ScenarioService', 'BuildStateService', function ($scope, $routeParams, $location, $cookieStore, ScenarioService, BuildStateService) {
    var branch = $routeParams.branch;
    var build = $routeParams.build;
    if(!branch) {
        branch = $cookieStore.get('branch');
    }
    if(!branch) {
        branch = 'trunk';
    }
    $cookieStore.put(branch);
    if(!build) {
        build = $cookieStore.get('build');
    }
    if(!build) {
        build = 'current';
    }
    $cookieStore.put(build);
    var useCaseName = $routeParams.useCaseName;
    var useCase = ScenarioService.findAllScenarios('trunk', 'current', useCaseName, function (useCaseAndScenarios) {
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
