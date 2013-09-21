'use strict';

NgUsdClientApp.controller('MainCtrl', function ($scope, $q, $location, Config, UseCaseService, BuildStateService) {

    var selectedBranch = Config.selectedBranch($location);
    var selectedBuild = Config.selectedBuild($location);
    $q.all([selectedBranch, selectedBuild]).then(function (result) {
        $scope.useCaseScenariosList = UseCaseService.findAllUseCases({'branchName': result[0], 'buildName': result[1]});
    });

    var states = BuildStateService.ListBuildStates();
    $scope.getStatusType = function(status){
        states.then(function() {
            if (states[status]) {
                return states[status] + ' label';
            } else {
                return 'label';
            }
        });
    };

    $scope.go = function(useCaseName) {
        $location.path('/usecase/' +useCaseName);
    };

    $scope.table = {search: {$: ''}, sort: {column: 'useCase.name', reverse: false}, filtering: false};

    $scope.resetSearchField = function() {
        $scope.table.search = {$: ''};
    };

    $scope.switchFilter = function() {
        $scope.table.filtering=!$scope.table.filtering;
        if (!$scope.table.filtering) {
            var temp = $scope.table.search.$;
            $scope.table.search = { $: temp };
        }
    };

});

