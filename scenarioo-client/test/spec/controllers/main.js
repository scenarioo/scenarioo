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

            $scope = $rootScope.$new();
            MainCtrl = $controller('MainCtrl', {$scope: $scope});
        }
    ));

    it('has no usecases and builds set in the beginning', function () {
        expect($scope.useCases).toBeUndefined();
        expect($scope.branchesAndBuilds).toBeUndefined();
    });

    it('navigates to use case when link is clicked', function () {
        expect($location.path()).toBe('');

        $scope.goToUseCase('DisplayWeather');

        expect($location.path()).toBe('/usecase/DisplayWeather');
    });

    it('loads use cases and builds when branch and build selection changes', function () {
        var USECASES_URL = HostnameAndPort.forTest() + '/scenarioo/rest/branches/release-branch-2014-01-16/builds/example-build/usecases';
        var BRANCHES_URL = HostnameAndPort.forTest() + '/scenarioo/rest/branches';
        var OBJECT_DESCRIPTIONS_URL = HostnameAndPort.forTest() + '/scenarioo/rest/branches/release-branch-2014-01-16/builds/example-build/objects/service';

        $httpBackend.whenGET(USECASES_URL).respond(TestData.USECASES);
        $httpBackend.whenGET(BRANCHES_URL).respond(TestData.BRANCHES);
        $httpBackend.whenGET(OBJECT_DESCRIPTIONS_URL).respond(TestData.OBJECT_DESCRIPTIONS);

        $location.url('/?branch=release-branch-2014-01-16&build=example-build');
        $scope.$apply();

        $httpBackend.flush();

        expect($scope.useCases).toEqualData(TestData.USECASES);
        expect($scope.branchesAndBuilds.branches).toEqualData(TestData.BRANCHES);
        expect($scope.branchesAndBuilds.selectedBranch).toEqualData(TestData.BRANCHES[1]);
        expect($scope.branchesAndBuilds.selectedBuild).toEqualData(TestData.BRANCHES[1].builds[0]);
        expect($scope.objectDescriptions).toEqualData(TestData.OBJECT_DESCRIPTIONS);
    });

});