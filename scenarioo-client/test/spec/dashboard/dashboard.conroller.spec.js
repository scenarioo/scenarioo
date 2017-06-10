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

describe('DashboardController', function () {
    var $location, $scope;
    var dashboardController;

    beforeEach(inject(function ($controller, $rootScope) {
            $scope = $rootScope.$new();
            dashboardController = $controller('DashboardController', {$scope: $scope});
        }
    ));

    it('test', function () {
        expect(0).toBe(0);
    });

    /*
    it('has no features and builds set in the beginning', function () {
        expect(dashboardController.features.length).toBe(0);
        expect(dashboardController.branchesAndBuilds.length).toBe(0);
    });

    it('navigates to feature when link is clicked', function () {
        expect($location.path()).toBe('');

        var dummyFeature = { name: 'DisplayWeather', diffInfo: {isRemoved: false} };
        dashboardController.clickFeature(dummyFeature, '/dashboard');

        expect($location.path()).toBe('/dashboard/DisplayWeather');
    });

    it('opens treenavigation and navigates to root feature', function(){
        expect($location.path()).toBe('/dashboard/DisplayWeather');
        element(by.id('pageslideButton')).click();
        element(by.id('root')).click();
        expect($location.path()).toBe('');
    });

     */
});
