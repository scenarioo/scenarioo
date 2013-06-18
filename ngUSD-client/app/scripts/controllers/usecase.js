'use strict';

angular.module('ngUSDClientApp')
    .controller('UseCaseCtrl', function ($scope, $routeParams) {
        $scope.useCaseId = $routeParams.useCaseId;
    });
