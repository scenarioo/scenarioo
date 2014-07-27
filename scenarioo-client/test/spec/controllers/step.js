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

describe('StepCtrl', function () {

    var $scope, $routeParams, $location, $q, $window, Config, ScenarioResource, StepService,
        HostnameAndPort, SelectedBranchAndBuild, $httpBackend, TestData;
    var StepCtrl;

    var METADATA_TYPE = 'some_type';
    var STEP_INFORMATION_TREE = { childNodes : [
        { nodeLabel : 'Step title', nodeValue : 'Search results' },
        { nodeLabel : 'Page name', childNodes : [  ], nodeValue : 'startSearch.jsp', nodeObjectName : 'startSearch.jsp' },
        { nodeLabel : 'URL', nodeValue : 'http://www.wikipedia.org' },
        { nodeLabel : 'Build status', nodeValue : 'success' }
    ] };

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$rootScope_, _$routeParams_, _$location_, _$q_, _$window_, _Config_, _ScenarioResource_, _StepService_, _HostnameAndPort_, _SelectedBranchAndBuild_, $controller, _$httpBackend_, _TestData_, localStorageService) {
        $scope = _$rootScope_.$new();
        $routeParams = _$routeParams_;
        $location = _$location_;
        $q = _$q_;
        $window = _$window_;
        Config = _Config_;
        ScenarioResource = _ScenarioResource_;
        StepService = _StepService_;
        HostnameAndPort = _HostnameAndPort_;
        SelectedBranchAndBuild = _SelectedBranchAndBuild_;
        $httpBackend = _$httpBackend_;
        TestData = _TestData_;

        $routeParams.useCaseName = 'uc';
        $routeParams.scenarioName = 'sc';
        $routeParams.pageName = 'pn';
        $routeParams.pageIndex = 0;
        $routeParams.stepIndex = 0;

        localStorageService.clearAll();

        StepCtrl = $controller('StepCtrl', {$scope: $scope, $routeParams: $routeParams, $location: $location,
            $q: $q, $window: $window, Config: Config, ScenarioResource: ScenarioResource, StepService: StepService, HostnameAndPort: HostnameAndPort,
            SelectedBranchAndBuild: SelectedBranchAndBuild, ScApplicationInfoPopup: {}});
    }));

    it('loads the step data', function () {
        loadPageContent();
        expect($scope.step).toEqualData(TestData.STEP.step);
    });

    it('shows specific step information', function () {
        loadPageContent();
        expect($scope.stepInformationTree).toEqual(STEP_INFORMATION_TREE);
    });

    function loadPageContent() {
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/configuration').respond(TestData.CONFIG);
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/branches/trunk/builds/current/usecases/uc/scenarios/sc').respond(TestData.SCENARIO);
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/branches/trunk/builds/current/usecases/uc/scenarios/sc/steps/0').respond(TestData.STEP);

        Config.load();
        $httpBackend.flush();
    }

});

