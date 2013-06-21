'use strict';

NgUsdClientApp.controller('ScenarioCtrl', ['$scope', '$routeParams', '$location', 'PagesService', function ($scope, $routeParams, PagesService) {

    var pagesAndScenarios = PagesService.listAllPages();
    $scope.scenario = pagesAndScenarios.scenario;
    $scope.pagesAndSteps = pagesAndScenarios.pagesAndSteps;


    $scope.resetSearchField = function() {
        $scope.searchFieldText = '';
    }

    $scope.go = function(useCaseName, scenarioName) {
        $location.path('/scenario/' +useCaseName + '/' + scenarioName);
    }
}]);
