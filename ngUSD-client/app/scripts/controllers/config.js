'use strict';

NgUsdClientApp.controller('ConfigCtrl', function ($scope, ConfigService, Config) {

    $scope.data = ConfigService.getConfiguration();

    $scope.resetConfiguration = function() {
        $scope.data = ConfigService.getConfiguration();
    }

    $scope.updateConfiguration = function(){
        ConfigService.updateConfiguration($scope.data);
        Config.updateConfiguration($scope.data);
    }

});
;
