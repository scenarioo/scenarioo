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

describe('Controller: ApplicationInfoController', function () {

    beforeEach(angular.mock.module('scenarioo.controllers'));

    var $scope,
        ConfigService,
        $httpBackend,
        TestData;

    beforeEach(inject(function ($controller, $rootScope, ConfigMock, _$httpBackend_, _TestData_) {
        ConfigService = ConfigMock;
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        TestData = _TestData_;
        $controller('ApplicationInfoController', {
            $scope: $scope,
            ConfigService: ConfigMock,
            $uibModalInstance: null
        });
    }));

    it('should update applicationInformation if it changes in ConfigService', function () {
        var VERSION_URL = 'rest/version';
        $httpBackend.whenGET(VERSION_URL).respond(TestData.VERSION);
        $httpBackend.flush();

        expect($scope.applicationInformation).toBeUndefined();

        ConfigService.setApplicationInformation('abc');

        $scope.$digest();

        /**
         * $scope.applicationInfo is $sce wrapped!
         */
        expect($scope.applicationInformation.$$unwrapTrustedValue()).toBe('abc');
    });

});
