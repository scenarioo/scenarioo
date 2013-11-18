'use strict';
/*
describe('Controller: NavigationCtrl', function () {

    var branches, $httpBackend;

    // load the controller's module
    beforeEach(module('ngUSDClientApp'));

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
