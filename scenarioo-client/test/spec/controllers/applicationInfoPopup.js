'use strict';

describe('Controller: ApplicationInfoPopupCtrl', function () {

    beforeEach(module('ngUSDClientApp.controllers'));

    var ApplicationInfoPopupCtrl,
        $scope,
        Config;

    beforeEach(inject(function ($controller, $rootScope, ConfigMock) {
        Config = ConfigMock;
        $scope = $rootScope.$new();
        ApplicationInfoPopupCtrl = $controller('ApplicationInfoPopupCtrl', {
            $scope: $scope, Config: ConfigMock
        });
    }));

    it('should update applicationInformation if it changes in Config', function () {

        expect($scope.applicationInformation).toBeUndefined();

        Config.setApplicationInformation('abc');

        $scope.$digest();

        expect($scope.applicationInformation).toBe('abc');
    });

});
