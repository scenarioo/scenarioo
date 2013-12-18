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

    var $scope, $routeParams, $location, $q, $window, Config, ScenarioResource, PageVariantService, StepService,
        HostnameAndPort, SelectedBranchAndBuild, $httpBackend, TestData;
    var StepCtrl;

    var METADATA_TYPE = 'some_type';
    var STEP_INFORMATION_TREE = { childNodes: [
        { nodeLabel: 'Step Title', nodeValue: 'Search results' },
        { nodeLabel: 'Page', childNodes: [  ], nodeValue: 'startSearch.jsp' },
        { nodeLabel: 'URL', nodeValue: 'http://www.wikipedia.org' },
        { nodeLabel: 'Build Status', nodeValue: 'success' }
    ] };

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$rootScope_, _$routeParams_, _$location_, _$q_, _$window_, _Config_, _ScenarioResource_, _PageVariantService_, _StepService_, _HostnameAndPort_, _SelectedBranchAndBuild_, $controller, _$httpBackend_, _TestData_) {
        $scope = _$rootScope_.$new();
        $routeParams = _$routeParams_;
        $location = _$location_;
        $q = _$q_;
        $window = _$window_;
        Config = _Config_;
        ScenarioResource = _ScenarioResource_;
        PageVariantService = _PageVariantService_;
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

        StepCtrl = $controller('StepCtrl', {$scope: $scope, $routeParams: $routeParams, $location: $location,
            $q: $q, $window: $window, Config: Config, ScenarioResource: ScenarioResource,
            PageVariantService: PageVariantService, StepService: StepService, HostnameAndPort: HostnameAndPort,
            SelectedBranchAndBuild: SelectedBranchAndBuild});
    }));

    it('shows all metadata sections collapsed first', function () {
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeTruthy();
    });

    it('collapses metadata sections on click', function () {
        $scope.toggleMetadataCollapsed(METADATA_TYPE);
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeFalsy();

        $scope.toggleMetadataCollapsed(METADATA_TYPE);
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeTruthy();
    });

    it('loads the step data', function () {
        loadPageContent();
        expect($scope.step).toEqualData(TestData.STEP);
    });

    it('shows specific step information', function () {
        loadPageContent();
        expect($scope.stepInformationTree).toEqual(STEP_INFORMATION_TREE);
    });

    function loadPageContent() {
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/configuration').respond(TestData.CONFIG);
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/branches/trunk/builds/current/search/pagevariants').respond(TestData.PAGE_VARIANTS);
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/branches/trunk/builds/current/usecases/uc/scenarios/sc').respond(TestData.SCENARIO);
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/branches/trunk/builds/current/usecases/uc/scenarios/sc/steps/0').respond(TestData.STEP);

        Config.load();
        $httpBackend.flush();
    }

});

