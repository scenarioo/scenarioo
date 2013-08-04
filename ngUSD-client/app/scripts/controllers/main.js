'use strict';

NgUsdClientApp.controller('MainCtrl', function ($scope, $location, Config, UseCaseService) {

    $scope.searchFieldText;

    $scope.useCaseScenariosList = UseCaseService.findAllUseCases(Config.selectedBranch($location), Config.selectedBuild($location));

    $scope.resetSearchField = function() {
            $scope.searchFieldText = '';
    }

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

    $scope.sort = {column: 'useCase.name', reverse: false};

});

