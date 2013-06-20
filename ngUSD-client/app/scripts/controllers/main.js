'use strict';

NgUsdClientApp.controller('MainCtrl', function ($scope, UseCaseService) {

    $scope.searchFieldText;

    $scope.useCases = UseCaseService.findAllUseCases("trunk", "current");

    $scope.resetSearchField = function() {
            $scope.searchFieldText = '';
    }

    $scope.getStatusType = function(status){
        switch (status) {
            case "success": return "label label-success";
            case "failed":  return "label label-important";
            default: return "label";
        }
    }

  });

