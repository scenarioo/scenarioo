'use strict';

NgUsdClientApp.controller('UseCaseCtrl', ['$scope', '$routeParams', '$location', 'UseCaseService', 'BuildStateService', function ($scope, $routeParams, $location, UseCaseService, BuildStateService) {
    var useCase = UseCaseService.getUseCase('asdf', '123', $routeParams.usecaseName, function (usecase) {
        $scope.usecase = usecase;
        var states = BuildStateService.ListBuildStates(function (states) {

            angular.forEach($scope.usecase.scenarios, function (scenario) {
                var s = {"successful": "sdfsdf"};
                var t = s["successful"];
                scenario.buildStateClass = states[scenario.status];
            });
        });
    });


    $scope.resetSearchField = function() {
        $scope.searchFieldText = '';
    }

    $scope.go = function(useCaseName, scenarioName) {
        $location.path('/scenario/' +useCaseName + '/' + scenarioName);
    }

}]);
