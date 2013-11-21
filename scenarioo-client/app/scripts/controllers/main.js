'use strict';

angular.module('scenarioo.controllers').controller('MainCtrl', function ($scope, $location, SelectedBranchAndBuild, UseCaseService, BranchesAndBuilds) {

    SelectedBranchAndBuild.callOnSelectionChange(loadUseCases);

    function loadUseCases(selected){
        $scope.useCaseScenariosList = UseCaseService.findAllUseCases({'branchName': selected.branch, 'buildName': selected.build});
        $scope.branchesAndBuilds = BranchesAndBuilds.getBranchesAndBuilds();
    }

    $scope.goToUseCase = function (useCaseName) {
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