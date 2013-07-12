'use strict';

NgUsdClientApp.controller('ConfigCtrl', function ($scope, ConfigService) {

    $scope.data = ConfigService.GetConfiguration();

    $scope.resetConfiguration = function() {
        $scope.data = ConfigService.GetConfiguration();
    }

    $scope.updateConfiguration = function(){
        ConfigService.UpdateConfiguration($scope.data);
    }

});
;
