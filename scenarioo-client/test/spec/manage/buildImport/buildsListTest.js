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

describe('Controller BuildsListCtrl', function () {

    var $location, $httpBackend, HostnameAndPort, TestData, $scope;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function ($controller, $rootScope, _$location_, _$httpBackend_, _HostnameAndPort_, _TestData_) {
            $location = _$location_;
            $httpBackend = _$httpBackend_;
            HostnameAndPort = _HostnameAndPort_;
            TestData = _TestData_;

            $scope = $rootScope.$new();

            var BUILD_IMPORT_STATES_URL = HostnameAndPort.forTest() + 'rest/builds/buildImportSummaries';

            $httpBackend.whenGET(BUILD_IMPORT_STATES_URL).respond(TestData.BUILD_IMPORT_STATES);

            $controller('BuildsListCtrl', {$scope: $scope, $uibModal: null });
        }
    ));

    it('loads builds in the beginning', function () {

        $location.url('/?branch=release-branch-2014-01-16&build=example-build');
        $scope.$apply();

        $httpBackend.flush();

        expect($scope.buildImportStates).toEqualData(TestData.BUILD_IMPORT_STATES);
    });

});
