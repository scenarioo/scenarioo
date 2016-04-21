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

describe('Controller :: useCase', function () {

    var BRANCH = 'branch_123',
        BUILD = 'build_123',
        USE_CASE = 'LogIn';

    var $scope, routeParams, controller, ScenarioResource, RelatedIssueResource, SelectedBranchAndBuild, $location, $httpBackend, HostnameAndPort;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function ($rootScope, $routeParams, $controller, _ScenarioResource_, _RelatedIssueResource_,
                                ConfigMock, _SelectedBranchAndBuild_, _$location_, scLocalStorage, _$httpBackend_, _HostnameAndPort_) {
            $scope = $rootScope.$new();
            routeParams = $routeParams;
            routeParams.useCaseName = USE_CASE;
            ScenarioResource = _ScenarioResource_;
            RelatedIssueResource = _RelatedIssueResource_;
            SelectedBranchAndBuild = _SelectedBranchAndBuild_;
            $location = _$location_;
            $httpBackend = _$httpBackend_;
            HostnameAndPort = _HostnameAndPort_;

            scLocalStorage.clearAll();

            controller = $controller('UseCaseCtrl', {
                $scope: $scope,
                $routeParams: routeParams,
                Config: ConfigMock,
                ScenarioResource: ScenarioResource,
                RelatedIssueResource: RelatedIssueResource,
                SelectedBranchAndBuild: SelectedBranchAndBuild
            });
        }
    ));

    it('should load all scenarios and and the selected use case', function () {
        spyOn(ScenarioResource, 'get').and.callFake(getFindAllScenariosFake());
        spyOn(RelatedIssueResource, 'query').and.callFake(queryRelatedIssuesFake());
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/labelconfigurations').respond({});

        expect(SelectedBranchAndBuild.selected().branch).toBeUndefined();
        expect(SelectedBranchAndBuild.selected().build).toBeUndefined();

        $location.url('/new/path/?branch=' + BRANCH + '&build=' + BUILD);
        $httpBackend.flush();
        $scope.$apply();

        expect(SelectedBranchAndBuild.selected().branch).toBe(BRANCH);
        expect(SelectedBranchAndBuild.selected().build).toBe(BUILD);

        $scope.$apply();

        expect(ScenarioResource.get).toHaveBeenCalledWith({
            'branchName': BRANCH,
            'buildName': BUILD,
            'usecaseName': USE_CASE
        }, jasmine.any(Function));
        expect(controller.useCase).toBeDefined();
        expect(controller.scenarios).toBeDefined();
        expect(controller.propertiesToShow).toBeDefined();
    });

    function getFindAllScenariosFake() {
        var DATA = {
            useCase: 'useCase',
            scenarios: 'scenarios'
        };

        return function (params, onSuccess) {
            onSuccess(DATA);
        };
    }

    function queryRelatedIssuesFake() {
        var DATA = {
            0:
                {
                    id: '1',
                    name: 'fakeTestingIssue',
                    firstScenarioSketchId: '1'
                }
        };

        return function(params, onSuccess) {
            onSuccess(DATA);
        };
    }

});
