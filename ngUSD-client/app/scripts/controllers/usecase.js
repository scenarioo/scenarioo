'use strict';

NgUsdClientApp.controller('UseCaseCtrl', ['$scope', '$location', '$routeParams', 'UseCaseService', 'BuildStateService', 'Config', function ($scope, $location, $routeParams, UseCaseService, BuildStateService, Config) {
    var useCase = UseCaseService.getUseCase(Config.selectedBranch($location), Config.selectedBuild($location), $routeParams.usecaseName, function (usecase) {
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

}]);
