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

    it('does return the expected src-URL to load tab content for first tab', function () {
        expect($scope.getLazyTabContentViewUrl(TestData.TABS[0]).toEqual(TestData.TABS[0].contentViewUrl));
    });

    it('has no builds set in the beginning', function () {
        expect($scope.branchesAndBuilds).toBeUndefined();
    });

    it('loads builds when branch and build selection changes', function () {
        var BRANCHES_URL = HostnameAndPort.forTest() + 'rest/branches';
        $httpBackend.whenGET(BRANCHES_URL).respond(TestData.BRANCHES);
        $location.url('/?branch=release-branch-2014-01-16&build=example-build');
        $scope.$apply();

        $httpBackend.flush();

        expect($scope.branchesAndBuilds.branches).toEqualData(TestData.BRANCHES);
        expect($scope.branchesAndBuilds.selectedBranch).toEqualData(TestData.BRANCHES[1]);
        expect($scope.branchesAndBuilds.selectedBuild).toEqualData(TestData.BRANCHES[1].builds[0]);

    });
});