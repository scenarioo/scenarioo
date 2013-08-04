'use strict';

NgUsdClientApp.controller('UseCaseCtrl', ['$scope', '$filter', '$routeParams', '$location', 'ScenarioService', 'BuildStateService', 'Config', function ($scope, $filter, $routeParams, $location, ScenarioService, BuildStateService, Config) {
    var useCaseName = $routeParams.useCaseName;
    var useCase = ScenarioService.findAllScenarios(Config.selectedBranch($location), Config.selectedBuild($location), useCaseName, function (useCaseAndScenarios) {
        $scope.useCase = useCaseAndScenarios.useCase;
        $scope.scenarios = useCaseAndScenarios.scenarios;
        var states = BuildStateService.ListBuildStates(function (states) {

            angular.forEach($scope.scenarios, function (scenario) {
                scenario.buildStateClass = states[scenario.status];
            });
        });

        $scope.propertiesToShow = Config.scenarioPropertiesInOverview;
        var properties = new Array($scope.propertiesToShow.length);
        var propLength = 0;
        for(var i=0; i<$scope.propertiesToShow.length; i++) {
            var property = $scope.propertiesToShow[i];
            properties[i] = {text: $filter('toHumanReadable')(property), property: "details.properties."+property, attr: property};
        }
        $scope.columns = [
            {text: 'Status', property: 'status'},
            {text: 'Name', property: 'name'},
            {text: 'Actions'},
            {text: 'Description'}]
            .concat(properties)
            .concat([
                {text: '# Steps', property: 'calculatedData.numberOfSteps'}]);

    });


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
    $scope.sort = {column: 'name', reverse: false};

}]);
