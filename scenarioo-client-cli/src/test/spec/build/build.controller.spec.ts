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
import * as angular from 'angular';
import 'angular-mocks';

describe('BuildController', function () {

    var $location, $httpBackend, TestData, $scope, BuildController;

    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(inject(function ($controller, $rootScope, _$location_, _$httpBackend_, _TestData_) {
            $location = _$location_;
            $httpBackend = _$httpBackend_;
            TestData = _TestData_;

            var BRANCHES_URL = 'rest/branches';
            $httpBackend.whenGET(BRANCHES_URL).respond(TestData.BRANCHES);

        $httpBackend.whenGET('rest/configuration').respond(TestData.CONFIG);

            $scope = $rootScope.$new();
            BuildController = $controller('BuildController', {$scope: $scope});
        }
    ));

    fit('works', () => {
      console.log('test');
    });

    it('has no builds set in the beginning', function () {
        expect($scope.branchesAndBuilds).toBeUndefined();
    });

    it('has a first static tab that is initialized with main use cases content', function () {
        $scope.$apply();
        $httpBackend.flush();
        expect(BuildController.getLazyTabContentViewUrl(BuildController.tabs[0].index)).toEqual('build/useCasesTab.html');
    });

    it('has additional dynamic custom tabs as configured in configuration, that are lazy loaded', function () {
        $scope.$apply();
        $httpBackend.flush();
        expect(BuildController.tabs[1].tabId).toEqual('calls');
        expect(BuildController.getLazyTabContentViewUrl(BuildController.tabs[1])).toEqual(null);
    });

    it('loads custom tab when url parameter for tab points to a custom tab', function () {
        $location.url('/?tab=calls');
        $scope.$apply();
        $httpBackend.flush();
        expect(BuildController.tabs[1].tabId).toEqual('calls');
        expect(BuildController.getLazyTabContentViewUrl(BuildController.tabs[1].index)).toEqual('build/customTab.html');
    });
});
