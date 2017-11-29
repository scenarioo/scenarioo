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

    var $httpBackend, TestData, $scope, ComparisonsController;

    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, _TestData_) {
            $httpBackend = _$httpBackend_;
            TestData = _TestData_;

            $scope = $rootScope.$new();

            var COMPARISONS_REST_URL = 'rest/comparisons';
            $httpBackend.whenGET(COMPARISONS_REST_URL).respond(TestData.COMPARISONS);

            ComparisonsController = $controller('ComparisonsController', {$scope: $scope});
        }
    ));

    it('loads comparisions in the beginning', function () {
        $scope.$apply();

        $httpBackend.flush();

        expect(angular.equals(ComparisonsController.comparisons, TestData.COMPARISONS)).toBeTruthy();
    });

});
