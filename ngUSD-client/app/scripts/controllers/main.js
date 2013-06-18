'use strict';

angular.module('ngUSDClientApp')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];

    $scope.useCases = [
        {"name": "UseCase Name A",
         "description": "Description use case A.",
         "status": "ok"},
        {"name": "UseCase Name B",
         "description": "Description use case B.",
         "status": "failed"}
    ];

    $scope.onButton1Click = function() {
        $scope.searchFieldText = "";
    }

        $scope.searchFieldText = "";
  });
