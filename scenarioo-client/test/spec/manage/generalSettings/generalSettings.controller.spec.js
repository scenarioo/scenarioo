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

describe('GeneralSettingsController', function () {

    var $rootScope, $controller, BranchesResource, ConfigService, $httpBackend, $scope, ConfigCtrl, HostnameAndPort, TestData;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$rootScope_, _$controller_, _BranchesResource_, _ConfigService_, _$httpBackend_, _HostnameAndPort_, _TestData_) {
        $rootScope = _$rootScope_;
        $controller = _$controller_;
        BranchesResource = _BranchesResource_;
        ConfigService = _ConfigService_;
        $httpBackend = _$httpBackend_;
        HostnameAndPort = _HostnameAndPort_;
        TestData = _TestData_;

        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/branches').respond(TestData.BRANCHES);
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/configuration').respond(TestData.CONFIG);

        $scope = $rootScope.$new();
        ConfigCtrl = $controller('GeneralSettingsController', {$scope: $scope, BranchesResource: BranchesResource, ConfigService: ConfigService});
    }));

    describe('when page is loaded', function () {
        it('loads and displays the config from the server', function () {
            expect(ConfigCtrl).toBeDefined();
            expect($scope.configuration).toBeUndefined();

            $httpBackend.flush();

            expect($scope.configuration).toEqualData(TestData.CONFIG);
        });

        it('loads all branches and builds', function () {
            expect($scope.branches).toBeUndefined();
            expect($scope.configuredBranch).toBeUndefined();

            $httpBackend.flush();

            expect($scope.branches).toBeDefined();
            expect($scope.configuredBranch).toBeDefined();
        });
    });

    describe('when reset button is clicked', function () {
        it('resets the config to the loaded values', function () {
            $httpBackend.flush();

            changeAllValues();

            $scope.resetConfiguration();

            expect($scope.configuration).toEqualData(TestData.CONFIG);
        });
    });

    describe('when the save button is clicked', function () {
        it('saves the edited config', function () {
            spyOn(ConfigService, 'updateConfiguration');

            $httpBackend.flush();

            changeAllValues();

            $scope.updateConfiguration();

            expect(ConfigService.updateConfiguration).toHaveBeenCalled();
        });
    });

    function changeAllValues() {
        $scope.configuration.defaultBuildName = 'new build';
        $scope.configuration.defaultBranchName = 'new branch';
        $scope.configuration.scenarioPropertiesInOverview = 'abc';
        $scope.configuration.applicationInformation = 'new information';
        $scope.configuration.testDocumentationDirPath = 'new path';
    }

});