'use strict';

angular.module('scenarioo.controllers').controller('UseCaseCtrl', function ($scope, $q, $filter, $routeParams, $location, ScenarioService, Config, SelectedBranchAndBuild) {

    SelectedBranchAndBuild.callOnSelectionChange(loadScenariosAndUseCase);

    function loadScenariosAndUseCase(selected) {
        var useCaseName = $routeParams.useCaseName;
        var useCaseAndScenarios = ScenarioService.findAllScenarios({'branchName': selected.branch, 'buildName': selected.build, 'usecaseName': useCaseName});

        useCaseAndScenarios.then(function (resultScenarios) {
            $scope.useCase = resultScenarios.useCase;
            $scope.scenarios = resultScenarios.scenarios;
        });

        $scope.propertiesToShow = Config.scenarioPropertiesInOverview();
    };

    $scope.goToScenario = function (useCaseName, scenarioName) {
        $location.path('/scenario/' + useCaseName + '/' + scenarioName);
    };

    $scope.goToFirstStep = function (useCaseName, scenarioName) {
        var selected = SelectedBranchAndBuild.selected();

        //FIXME This could be improved, if the scenario service for finding all scenarios would also retrieve the name of the first page
        var scenario = ScenarioService.getScenario({'branchName': selected.branch, 'buildName': selected.build, 'usecaseName': useCaseName, 'scenarioName': scenarioName});
        scenario.then(function (scenarioResult) {
            $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(scenarioResult.pagesAndSteps[0].page.name) + '/0/0');
        });
    };
    $scope.table = {search: {$: ''}, sort: {column: 'name', reverse: false}, filtering: false};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    $scope.switchFilter = function () {
        $scope.table.filtering = !$scope.table.filtering;
        if (!$scope.table.filtering) {
            var temp = $scope.table.search.searchTerm;
            $scope.table.search = { searchTerm: temp };
        }
    };

});
