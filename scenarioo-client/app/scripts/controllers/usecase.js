'use strict';

angular.module('scenarioo.controllers').controller('UseCaseCtrl', function ($scope, $q, $filter, $routeParams, $location, ScenarioResource, Config, SelectedBranchAndBuild) {

    SelectedBranchAndBuild.callOnSelectionChange(loadScenariosAndUseCase);

    function loadScenariosAndUseCase(selected) {
        var useCaseName = $routeParams.useCaseName;
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName
            },
            function onSuccess(result) {
                $scope.useCase = result.useCase;
                $scope.scenarios = result.scenarios;
            }
        );

        $scope.propertiesToShow = Config.scenarioPropertiesInOverview();
    }

    $scope.goToScenario = function (useCaseName, scenarioName) {
        $location.path('/scenario/' + useCaseName + '/' + scenarioName);
    };

    $scope.goToFirstStep = function (useCaseName, scenarioName) {
        var selected = SelectedBranchAndBuild.selected();

        //FIXME This could be improved, if the scenario service for finding all scenarios would also retrieve the name of the first page
        ScenarioResource.get(
            {
                branchName: selected.branch,
                buildName: selected.build,
                usecaseName: useCaseName,
                scenarioName: scenarioName
            },
            function onSuccess(scenarioResult) {
                $location.path('/step/' + useCaseName + '/' + scenarioName + '/' + encodeURIComponent(scenarioResult.pagesAndSteps[0].page.name) + '/0/0');
            }
        );
    };
    $scope.table = {search: {$: ''}, sort: {column: 'name', reverse: false}, filtering: false};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    $scope.toggleFilter = function () {
        $scope.table.filtering = !$scope.table.filtering;
        if (!$scope.table.filtering) {
            var temp = $scope.table.search.searchTerm;
            $scope.table.search = { searchTerm: temp };
        }
    };

});
