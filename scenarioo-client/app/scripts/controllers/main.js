'use strict';

angular.module('scenarioo.controllers').controller('MainCtrl', function ($scope, $location, SelectedBranchAndBuild, UseCasesResource, BranchesAndBuilds, ObjectsForTypeResource) {

    SelectedBranchAndBuild.callOnSelectionChange(loadUseCases);

    function loadUseCases(selected) {

        UseCasesResource.query(
            {'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(result) {
                $scope.useCases = result;
            });

        BranchesAndBuilds.getBranchesAndBuilds(
            function onSuccess(branchesAndBuilds) {
                $scope.branchesAndBuilds = branchesAndBuilds;
            }
        );

        // TODO: rename method or split it.
        ObjectsForTypeResource.query({'branchName': selected.branch, 'buildName': selected.build},
            function onSuccess(result) {
                $scope.objectDescriptions = result;
            }
        );

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

    $scope.genericObjectsTypes = {selected: null};
    $scope.genericObjectsTypeFilter = function (genericObjectTabs) {
        if ($scope.genericObjectsTypes.selected) {
            if ($scope.genericObjectsTypes.selected.objectType === genericObjectTabs.type) return true;
        }  else {
            return true;
        }
    };

    $scope.genericObjectTabs = [
        {index: '0', label:'Object Descriptions', objectTypes:[
            {index:0, label:'Business Operations', objectType:'businessOperation'},
            {index:1, label:'Services', objectType:'service'},
            {index:2, label:'UI Actions', objectType:'action'} ,
            {index:3, label:'HTTP Requests', objectType:'httpAction'}]}
        //,{index: '1', label:'Simulation Configs', objectType: 'httpAction'}
    ];

});