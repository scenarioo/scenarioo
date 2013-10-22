'use strict';

angular.module('ngUSDClientApp.controllers').controller('UseCaseCtrl', function ($scope, $q, $filter, $routeParams, $location, ScenarioService, BuildStateService, Config) {
    var useCaseName = $routeParams.useCaseName;
    var selectedBranch = Config.selectedBranch($location);
    var selectedBuild = Config.selectedBuild($location);

    $q.all([selectedBranch, selectedBuild]).then(function (result) {

        var useCaseAndScenarios = ScenarioService.findAllScenarios({'branchName': result[0], 'buildName': result[1], 'usecaseName': useCaseName});
        var propertiesToShow = Config.scenarioPropertiesInOverview();
        var states = BuildStateService.ListBuildStates();

        useCaseAndScenarios.then(function (resultScenarios) {
            $scope.useCase = resultScenarios.useCase;
            $scope.scenarios = resultScenarios.scenarios;

            propertiesToShow.then(function (resultProperties) {
                $scope.propertiesToShow = resultProperties;
                var properties = new Array(resultProperties.length);
                for (var i = 0; i < resultProperties.length; i++) {
                    var property = resultProperties[i];
                    properties[i] = {text: $filter('toHumanReadable')(property), property: 'details.properties.' + property, attr: property};
                }
                $scope.columns = [
                    {text: 'Status', property: 'status'},
                    {text: 'Name', property: 'name'},
                    {text: 'Actions'},
                    {text: 'Description'}
                ]
                    .concat(properties)
                    .concat([
                        {text: '# Steps', property: 'calculatedData.numberOfSteps'}
                    ]);
            });
        });

        states.then(function (result) {
            $scope.getStatusType = function (status) {
                return result[status];
            };
        });
    });

    $scope.go = function (useCaseName, scenarioName) {
        $location.path('/scenario/' + useCaseName + '/' + scenarioName);
    };

    $scope.goToFirstStep = function (useCaseName, scenarioName) {
        $q.all([selectedBranch, selectedBuild]).then(function (result) {
            //FIXME This could be improved, if the scenario service for finding all scenarios would also retrieve the name of the first page
            var scenario = ScenarioService.getScenario({'branchName': result[0], 'buildName': result[1], 'usecaseName': useCaseName, 'scenarioName': scenarioName});
            scenario.then(function (scenarioResult) {
                $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(scenarioResult.pagesAndSteps[0].page.name) + '/0/0');
            });
        });
    };
    $scope.table = {search: {$: ''}, sort: {column: 'name', reverse: false}, filtering: false};

    $scope.resetSearchField = function () {
        $scope.table.search = {$: ''};
    };

    $scope.switchFilter = function () {
        $scope.table.filtering = !$scope.table.filtering;
        if (!$scope.table.filtering) {
            var temp = $scope.table.search.$;
            $scope.table.search = { $: temp };
        }
    };

});
