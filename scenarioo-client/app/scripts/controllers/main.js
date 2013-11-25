'use strict';

angular.module('scenarioo.controllers').controller('MainCtrl', function ($scope, $location, SelectedBranchAndBuild, UseCasesResource, BranchesAndBuilds) {

    SelectedBranchAndBuild.callOnSelectionChange(loadUseCases);

    function loadUseCases(selected){
        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function(result) {
                $scope.useCases = result;
            });

        // TODO: refactor getBranchesAndBuilds()
        $scope.branchesAndBuilds = BranchesAndBuilds.getBranchesAndBuilds();
    }

    $scope.goToUseCase = function (useCaseName) {
        $location.path('/usecase/' + useCaseName);
    };

    $scope.table = {search: {searchTerm: ''}, sort: {column: 'useCase.name', reverse: false}, filtering: false};

    $scope.resetSearchField = function () {
        $scope.table.search = {searchTerm: ''};
    };

    $scope.toggleFilter = function () {
        $scope.table.filtering = !$scope.table.filtering;
        if (!$scope.table.filtering) {
            // Removes filter values when filter is switched off
            var searchTerm = $scope.table.search.searchTerm;
            $scope.table.search = { searchTerm: searchTerm };
        }
    };

});