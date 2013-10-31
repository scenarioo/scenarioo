'use strict';

angular.module('ngUSDClientApp.controllers').controller('UseCaseCtrl', function ($scope, $q, $filter, $routeParams, $location, ScenarioService, Config) {

    $scope.$watch(function () {
        return '' + Config.selectedBranch() + Config.selectedBuild();
    }, function () {
        loadScenariosAndUseCase(Config.selectedBranch(), Config.selectedBuild());
    });

    function loadScenariosAndUseCase(selectedBranch, selectedBuild) {
        var useCaseName = $routeParams.useCaseName;
        var useCaseAndScenarios = ScenarioService.findAllScenarios({'branchName': selectedBranch, 'buildName': selectedBuild, 'usecaseName': useCaseName});

        useCaseAndScenarios.then(function (resultScenarios) {
            $scope.useCase = resultScenarios.useCase;
            $scope.scenarios = resultScenarios.scenarios;
        });

        $scope.propertiesToShow = Config.scenarioPropertiesInOverview();
    };

    $scope.go = function (useCaseName, scenarioName) {
        $location.path('/scenario/' + useCaseName + '/' + scenarioName);
    };

    $scope.goToFirstStep = function (useCaseName, scenarioName) {
         var selectedBranch = Config.selectedBranch();
         var selectedBuild = Config.selectedBuild();

         //FIXME This could be improved, if the scenario service for finding all scenarios would also retrieve the name of the first page
         var scenario = ScenarioService.getScenario({'branchName': selectedBranch, 'buildName': selectedBuild, 'usecaseName': useCaseName, 'scenarioName': scenarioName});
         scenario.then(function (scenarioResult) {
            $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(scenarioResult.pagesAndSteps[0].page.name) + '/0/0');
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
