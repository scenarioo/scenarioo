/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

describe('Service: ScApplicationInfoPopup', function () {

    beforeEach(module('scenarioo.services'));

    beforeEach(module(function ($provide) {
        $provide.value('$uibModal', {
                open: function () {
                }
            }
        );
        $provide.value('ApplicationInfoCtrl', {});
    }));

    var ScApplicationInfoPopup, scLocalStorage, $uibModal, dummyPromise = {
        result: {
            finally: function () {
            }
        }
    };
    beforeEach(inject(function (_ScApplicationInfoPopup_, _scLocalStorage_, _$uibModal_) {
        ScApplicationInfoPopup = _ScApplicationInfoPopup_;
        scLocalStorage = _scLocalStorage_;
        $uibModal = _$uibModal_;
    }));

    it('shows the application info popup on first visit of the app', function () {
        scLocalStorage.clearAll();

        spyOn($uibModal, 'open').and.returnValue(dummyPromise);

        ScApplicationInfoPopup.showApplicationInfoPopupIfRequired();

        expect($uibModal.open).toHaveBeenCalled();
    });

    it('does not show the application info popup when the user returns to the app', function () {
        scLocalStorage.clearAll();
        scLocalStorage.set(ScApplicationInfoPopup.PREVIOUSLY_VISITED_COOKIE_NAME, true);

        spyOn($uibModal, 'open').and.returnValue(dummyPromise);

        ScApplicationInfoPopup.showApplicationInfoPopupIfRequired();

        expect($uibModal.open).not.toHaveBeenCalled();
    });

    it('shows opens a modal dialog', function () {
        spyOn($uibModal, 'open').and.returnValue(dummyPromise);

        ScApplicationInfoPopup.showApplicationInfoPopup();

        expect($uibModal.open).toHaveBeenCalled();
    });

});

describe('Controller: ApplicationInfoCtrl', function () {

    beforeEach(module('scenarioo.controllers'));

    var $scope,
        Config,
        $httpBackend,
        HostnameAndPort,
        TestData;

    beforeEach(inject(function ($controller, $rootScope, ConfigMock, _$httpBackend_, _HostnameAndPort_, _TestData_) {
        Config = ConfigMock;
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        HostnameAndPort = _HostnameAndPort_;
        TestData = _TestData_;
        $controller('ApplicationInfoCtrl', {
            $scope: $scope,
            Config: ConfigMock,
            $uibModalInstance: null
        });
    }));

    it('should update applicationInformation if it changes in Config', function () {
        var VERSION_URL = HostnameAndPort.forTest() + 'rest/version';
        $httpBackend.whenGET(VERSION_URL).respond(TestData.VERSION);
        $httpBackend.flush();

        expect($scope.applicationInformation).toBeUndefined();

        Config.setApplicationInformation('abc');

        $scope.$digest();

        /**
         * $scope.applicationInfo is $sce wrapped!
         */
        expect($scope.applicationInformation.$$unwrapTrustedValue()).toBe('abc');
    });

});
