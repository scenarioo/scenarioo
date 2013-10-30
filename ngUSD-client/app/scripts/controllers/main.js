'use strict';

angular.module('ngUSDClientApp.controllers').controller('MainCtrl', function (CONFIG_LOADED_EVENT, $scope, $location, Config, UseCaseService) {

    function loadUseCases(){
        $scope.useCaseScenariosList = UseCaseService.findAllUseCases({'branchName': Config.selectedBranch(), 'buildName': Config.selectedBuild()});
    }

    if(Config.isLoaded()){
        loadUseCases();
    }

    $scope.$on(CONFIG_LOADED_EVENT, function () {
        loadUseCases();
    });

    $scope.go = function (useCaseName) {
        $location.path('/usecase/' + useCaseName);
    };

    $scope.table = {search: {$: ''}, sort: {column: 'useCase.name', reverse: false}, filtering: false};

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