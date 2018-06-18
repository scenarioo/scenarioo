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

describe('ComparisonsController', function () {

    var $httpBackend, $controller, $scope, TestData;

    beforeEach(function() {
        angular.mock.module('scenarioo.controllers')
        inject(function (_$controller_, $rootScope, _$httpBackend_, _TestData_) {
                $httpBackend = _$httpBackend_;
                TestData = _TestData_;
                $controller = _$controller_;
                $scope = $rootScope.$new();
            }
        );
    });

    it('loads comparisions in the beginning', function () {
        var ComparisonsController = createController();

        var COMPARISONS_REST_URL = 'rest/comparisons';
        $httpBackend.whenGET(COMPARISONS_REST_URL).respond(TestData.COMPARISONS);
        $httpBackend.whenGET('rest/configuration/applicationStatus').respond({
            'searchEngineRunning':false,
            'version': TestData.VERSION,
            'configuration': TestData.CONFIG
        });

        $httpBackend.flush();

        expect(angular.equals(ComparisonsController.comparisons, TestData.COMPARISONS)).toBeTruthy();
    });

    function createController() {
        return $controller('ComparisonsController', {$scope: $scope, $uibModal: null});
    }
});
