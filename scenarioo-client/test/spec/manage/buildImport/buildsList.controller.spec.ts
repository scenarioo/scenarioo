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

import * as angular from "angular";
import {Observable} from "rxjs";

describe('BuildsListController', () => {

    let $location, TestData, $scope, BuildsListController;

    beforeEach(angular.mock.module('scenarioo.controllers'));
    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.
        $provide.value("BuildImportStatesResource", {
            get: () => {
            }
        });
        $provide.value("BuildImportService", {});
        $provide.value("BuildReimportResource", {});
        $provide.value("BuildImportLogResource", {});
    }));

    beforeEach(inject(($controller, $rootScope, _$location_, _TestData_, _BuildImportStatesResource_) => {
            $location = _$location_;
            TestData = _TestData_;
            spyOn(_BuildImportStatesResource_, 'get')
                .and
                .returnValue(Observable.of(TestData.BUILD_IMPORT_STATES));

            $scope = $rootScope.$new();

            BuildsListController = $controller('BuildsListController', {$scope: $scope, $uibModal: null});
        }
    ));

    it('loads builds in the beginning', () => {

        $location.url('/?branch=release-branch-2014-01-16&build=example-build');
        $scope.$apply();

        expect(angular.equals(BuildsListController.buildImportStates, TestData.BUILD_IMPORT_STATES)).toBeTruthy();
    });

});
