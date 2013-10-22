'use strict';

angular.module('ngUSDClientApp.controllers').controller('MainCtrl', function (CONFIG_LOADED_EVENT, $scope, $q, $location, Config, UseCaseService, BuildStateService) {

    $scope.$on(CONFIG_LOADED_EVENT, function () {
        $scope.useCaseScenariosList = UseCaseService.findAllUseCases({'branchName': Config.selectedBranch(), 'buildName': Config.selectedBuild()});
    });

    var states = BuildStateService.ListBuildStates();
    $scope.getStatusType = function (status) {
        states.then(function () {
            if (states[status]) {
                return states[status] + ' label';
            } else {
                return 'label';
            }
        });
    };

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