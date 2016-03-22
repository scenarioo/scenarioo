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

describe('Controller MainCtrl', function () {

    var $location, $httpBackend, HostnameAndPort, TestData, $scope, MainCtrl;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function ($controller, $rootScope, _$location_, _$httpBackend_, _HostnameAndPort_, _TestData_) {
            $location = _$location_;
            $httpBackend = _$httpBackend_;
            HostnameAndPort = _HostnameAndPort_;
            TestData = _TestData_;

            var BRANCHES_URL = HostnameAndPort.forTest() + 'rest/branches';
            $httpBackend.whenGET(BRANCHES_URL).respond(TestData.BRANCHES);

            $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/configuration').respond(TestData.CONFIG);

            $scope = $rootScope.$new();
            MainCtrl = $controller('MainCtrl', {$scope: $scope});
        }
    ));

    it('has no builds set in the beginning', function () {
        expect($scope.branchesAndBuilds).toBeUndefined();
    });

    it('has a first static tab that is initialized with main use cases content', function () {
        $scope.$apply();
        $httpBackend.flush();
        expect(MainCtrl.getLazyTabContentViewUrl(MainCtrl.tabs[0].index)).toEqual('views/mainUseCasesTab.html');
    });

    it('has additional dynamic custom tabs as configured in configuration, that are lazy loaded', function () {
        $scope.$apply();
        $httpBackend.flush();
        expect(MainCtrl.tabs[1].tabId).toEqual('calls');
        expect(MainCtrl.getLazyTabContentViewUrl(MainCtrl.tabs[1])).toEqual(null);
    });

    it('loads custom tab when url parameter for tab points to a custom tab', function () {
        $location.url('/?tab=calls');
        $scope.$apply();
        $httpBackend.flush();
        expect(MainCtrl.tabs[1].tabId).toEqual('calls');
        expect(MainCtrl.getLazyTabContentViewUrl(MainCtrl.tabs[1].index)).toEqual('views/mainCustomTab.html');
    });
});
