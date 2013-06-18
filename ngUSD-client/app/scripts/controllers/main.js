'use strict';

angular.module('ngUSDClientApp')
  .controller('MainCtrl', function ($scope) {
        $scope.useCases = [
            {"name": "UseCase Name A",
                "description": "Description use case A.",
                "status": "ok"},
            {"name": "UseCase Name B",
                "description": "Description use case B.",
                "status": "failed"}
        ];

        $scope.resetSearchField = function() {
            $scope.searchFieldText = "";
        }

        $scope.searchFieldText;
  });
