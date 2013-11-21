'use strict';

angular.module('scenarioo.controllers').controller('ApplicationInfoPopupCtrl', function ($scope, Config) {

    $scope.$watch(function() {
        return Config.applicationInformation();
    }, function(applicationInformation) {
        $scope.applicationInformation = applicationInformation;
    });

});
