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

describe('ComparisonDetailsController', function () {

    var $httpBackend, $scope, $controller;


    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(inject(function (_$controller_, $rootScope, _$httpBackend_) {
            $httpBackend = _$httpBackend_;
            $scope = $rootScope.$new();
            $controller = _$controller_;
        }
    ));

    it('loads log in the beginning', function () {
        var ComparisonDetailsController = createController();

        var COMPARISON_LOG_REST_URL = 'rest/builds/develop/2018-05-08/comparisons/To%20most%20recent%20develop/log';
        $httpBackend.expectGET(COMPARISON_LOG_REST_URL).respond('Test log');
        $httpBackend.flush();

        expect(ComparisonDetailsController.log).toEqual('Test log')
    });

    function createController() {
        var comparison = {
            baseBuild: {
                branchName: 'develop',
                buildName: '2018-05-08'
            },
            comparisonConfiguration: {
                name: 'To most recent develop'
            }
        };
        return $controller('ComparisonDetailsController', {$scope: $scope, $uibModalInstance: null, comparison: comparison});
    }
});
