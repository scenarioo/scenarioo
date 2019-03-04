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

import {Observable} from "rxjs";
declare var angular: angular.IAngularStatic;

describe('BuildController', () => {


    let $location, TestData, $scope, BuildController;

    let ConfigResourceMock = {
        get: () => Observable.of(angular.copy(TestData.CONFIG))
    };

    beforeEach(angular.mock.module('scenarioo.controllers'));
    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.
        $provide.value("ConfigResource", ConfigResourceMock);
    }));


    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(inject(($controller, $rootScope, _$location_, _TestData_) => {
            $location = _$location_;
            TestData = _TestData_;

            $scope = $rootScope.$new();
            BuildController = $controller('BuildController', {$scope: $scope});
        }
    ));

    it('has no builds set in the beginning', () => {
        expect($scope.branchesAndBuilds).toBeUndefined();
    });

    it('has a first static tab that is initialized with main use cases content', () => {
        $scope.$apply();
        expect(BuildController.getLazyTabContentViewUrl(BuildController.tabs[0].index)).toEqual('build/useCasesTab.html');
    });

    it('has additional dynamic custom tabs as configured in configuration, that are lazy loaded', () => {
        $scope.$apply();
        expect(BuildController.tabs[1].tabId).toEqual('calls');
        expect(BuildController.getLazyTabContentViewUrl(BuildController.tabs[1])).toEqual(null);
    });

    it('loads custom tab when url parameter for tab points to a custom tab', () => {
        $location.url('/?tab=calls');
        $scope.$apply();

        expect(BuildController.tabs.length).toEqual(3);
        expect(BuildController.tabs[1].tabId).toEqual('calls');
        expect(BuildController.getLazyTabContentViewUrl(BuildController.tabs[1].index)).toEqual('build/customTab.html');
    });
});
