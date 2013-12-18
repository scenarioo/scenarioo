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
/*
describe('Controller: NavigationCtrl', function () {

    var branches, $httpBackend;

    // load the controller's module
    beforeEach(module('scenarioo'));

    // Initialize the controller and a mock scope
    beforeEach(inject(function () {
    }));

    it('selected branch & build should be set', inject(function (_$httpBackend_, $controller, $rootScope) {
        var scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        var branches = [{
            name: "Branch 1",
            builds: [
                { name: "Build 1a" },
                { name: "Build 1b" },
                { name: "Build 1c" },
                { name: "Build 1d" },
                { name: "Build 1e" },
                { name: "Build 1f" },
                { name: "Build 1g" }
            ]
        }, {
            name: "Branch 2",
            builds: [
                { name: "Build 2a" },
                { name: "Build 2b" }
            ]
        }];
        $httpBackend.expectGET("http://localhost:8050/scenarioo/rest/branches").respond(branches);

        console.log("Mocked BranchService Response: "+ angular.toJson(branches));
        var NavigationCtrl = $controller('NavigationCtrl', {
            $scope: scope
        });
        $httpBackend.flush();

        expect(angular.toJson(scope.branches)).toEqual(angular.toJson(branches));
        expect(scope.selectedBranch.name).toBe("Branch 1");
        expect(scope.selectedBuild.name).toBe("Build 1a");
    }));

    afterEach(function() {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });
});
*/
