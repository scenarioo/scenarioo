'use strict';

NgUsdClientApp.controller('MainCtrl', function ($scope, $location, Config, UseCaseService) {

    $scope.searchFieldText;

    $scope.useCaseScenariosList = UseCaseService.findAllUseCases(Config.selectedBranch($location), Config.selectedBuild($location));


    $scope.getStatusType = function(status){
        switch (status) {
            case "success": return "label label-success";
            case "failed":  return "label label-important";
            default: return "label";
        }
    }

    $scope.go = function(useCaseName) {
        $location.path('/usecase/' +useCaseName);
    }

    $scope.table = {search: {$: ''}, sort: {column: 'useCase.name', reverse: false}, filtering: false};

    $scope.resetSearchField = function() {
        $scope.table.search = {$: ''};
    }

    $scope.switchFilter = function() {
        $scope.table.filtering=!$scope.table.filtering
        if (!$scope.table.filtering) {
            var temp = $scope.table.search.$;
            $scope.table.search = { $: temp };
        }
    }


});

