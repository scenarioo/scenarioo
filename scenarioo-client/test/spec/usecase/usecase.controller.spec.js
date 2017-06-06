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

describe('FeatureController', function () {

    var BRANCH = 'branch_123',
        BUILD = 'build_123',
        FEATURE = 'LogIn';

    var $scope, routeParams, controller, ScenarioResource, RelatedIssueResource, FeatureDiffInfoResource, ScenarioDiffInfosResource, SelectedBranchAndBuildService, $location, $httpBackend, HostnameAndPort;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function ($rootScope, $routeParams, $controller, _ScenarioResource_, _RelatedIssueResource_, _FeatureDiffInfoResource_, _ScenarioDiffInfosResource_,
                                ConfigMock, _SelectedBranchAndBuildService_, _$location_, LocalStorageService, _$httpBackend_, _HostnameAndPort_) {
            $scope = $rootScope.$new();
            routeParams = $routeParams;
            routeParams.featureName = FEATURE;
            ScenarioResource = _ScenarioResource_;
            RelatedIssueResource = _RelatedIssueResource_;
            FeatureDiffInfoResource = _FeatureDiffInfoResource_;
            ScenarioDiffInfosResource = _ScenarioDiffInfosResource_;
            SelectedBranchAndBuildService = _SelectedBranchAndBuildService_;
            $location = _$location_;
            $httpBackend = _$httpBackend_;
            HostnameAndPort = _HostnameAndPort_;

            LocalStorageService.clearAll();

            controller = $controller('FeatureController', {
                $scope: $scope,
                $routeParams: routeParams,
                ConfigService: ConfigMock,
                ScenarioResource: ScenarioResource,
                RelatedIssueResource: RelatedIssueResource,
                FeatureDiffInfoResource: FeatureDiffInfoResource,
                ScenarioDiffInfosResource: ScenarioDiffInfosResource,
                SelectedBranchAndBuildService: SelectedBranchAndBuildService
            });
        }
    ));
/*
    it('should load all scenarios and and the selected feature', function () {
        spyOn(ScenarioResource, 'get').and.callFake(getFindAllScenariosFake());
        spyOn(RelatedIssueResource, 'query').and.callFake(queryRelatedIssuesFake());
        spyOn(FeatureDiffInfoResource, 'get').and.callFake(getEmptyData());
        spyOn(ScenarioDiffInfosResource, 'get').and.callFake(getEmptyData());
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/labelconfigurations').respond({});

        expect(SelectedBranchAndBuildService.selected().branch).toBeUndefined();
        expect(SelectedBranchAndBuildService.selected().build).toBeUndefined();

        $location.url('/new/path/?branch=' + BRANCH + '&build=' + BUILD);
        $httpBackend.flush();
        $scope.$apply();

        expect(SelectedBranchAndBuildService.selected().branch).toBe(BRANCH);
        expect(SelectedBranchAndBuildService.selected().build).toBe(BUILD);

        $scope.$apply();

        expect(ScenarioResource.get).toHaveBeenCalledWith({
            'branchName': BRANCH,
            'buildName': BUILD,
            'featureName': FEATURE
        }, jasmine.any(Function));
        expect(controller.feature).toBeDefined();
        expect(controller.scenarios).toBeDefined();
        expect(controller.propertiesToShow).toBeDefined();
    });
*/
    function getFindAllScenariosFake() {
        var DATA = {
            feature: 'feature',
            scenarios: getFakeScenarios()
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

    function getFakeScenarios() {
        var scenarios = [];
        scenarios.push({
            name: 'scenario'
        });
        return scenarios;
    }

    function getEmptyData() {
        var DATA = {

        };

        return function(params, onSuccess) {
            onSuccess(DATA);
        };
    }

});
