'use strict';

NgUsdClientApp.controller('MainCtrl', function ($scope, UseCaseService) {

    $scope.searchFieldText;

    $scope.useCases = UseCaseService.findAllUseCases("trunk", "current");

    $scope.resetSearchField = function() {
            $scope.searchFieldText = '';
    }

  });

