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
    var STEP_INFORMATION_TREE = {
        childNodes : [
            { nodeLabel: 'Step title', nodeValue: 'Search results' },
            { nodeLabel: 'Page name', childNodes: [  ], nodeValue: 'searchResults.jsp', nodeObjectName: 'searchResults.jsp' },
            { nodeLabel: 'URL', nodeValue: 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go' },
            { nodeLabel: 'Build status', nodeValue: 'success' }
        ]
    };

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
        $routeParams.pageOccurrence = 0;
        $routeParams.stepInPageOccurrence = 1;

        localStorageService.clearAll();

        StepCtrl = $controller('StepCtrl', {$scope: $scope, $routeParams: $routeParams, $location: $location,
            $q: $q, $window: $window, Config: Config, ScenarioResource: ScenarioResource, StepService: StepService, HostnameAndPort: HostnameAndPort,
            SelectedBranchAndBuild: SelectedBranchAndBuild, ScApplicationInfoPopup: {}});
    }));

    it('shows all metadata sections collapsed first', function () {
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeTruthy();
    });

    it('collapses metadata sections on click', function () {
        $scope.toggleMetadataExpanded(METADATA_TYPE);
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeFalsy();

        $scope.toggleMetadataExpanded(METADATA_TYPE);
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeTruthy();
    });

    it('loads the step data', function () {
        loadPageContent();
        expect($scope.step).toEqualData(TestData.STEP.step);
    });

    it('shows specific step information', function () {
        loadPageContent();
        expect($scope.stepInformationTree).toEqual(STEP_INFORMATION_TREE);
    });

    it('loads the stepNavigation and the stepStatistics into scope', function () {
        loadPageContent();
        expect($scope.stepNavigation).toEqual(TestData.STEP.stepNavigation);
        expect($scope.stepStatistics).toEqual(TestData.STEP.stepStatistics);

        expect($scope.getCurrentStepIndexForDisplay()).toBe(3);
        expect($scope.getCurrentPageIndexForDisplay()).toBe(2);
        expect($scope.getStepIndexInCurrentPageForDisplay()).toBe(1);
        expect($scope.getNumberOfStepsInCurrentPageForDisplay()).toBe(2);
    });

    it('isFirstStep()', function () {
        loadPageContent();
        expect($scope.isFirstStep()).toBeFalsy();

        $scope.stepNavigation.stepIndex = 0;
        expect($scope.isFirstStep()).toBeTruthy();
    });

    it('isLastStep()', function () {
        loadPageContent();
        expect($scope.isLastStep()).toBeFalsy();

        $scope.stepNavigation.stepIndex = $scope.stepStatistics.totalNumberOfStepsInScenario - 1;
        expect($scope.isLastStep()).toBeTruthy();
    });

    it('isFirstPage()', function () {
        loadPageContent();
        expect($scope.isFirstPage()).toBeFalsy();

        $scope.stepNavigation.pageIndex = 0;
        expect($scope.isFirstPage()).toBeTruthy();
    });

    it('isLastPage()', function () {
        loadPageContent();
        expect($scope.isLastPage()).toBeFalsy();

        $scope.stepNavigation.pageIndex = $scope.stepStatistics.totalNumberOfPagesInScenario - 1;
        expect($scope.isLastPage()).toBeTruthy();
    });


    it('goToPreviousStep()', function () {
        loadPageContent();

        $scope.goToPreviousStep();

        expect($location.path()).toBe('/step/uc/sc/startSearch.jsp/0/1');
    });

    it('goToNextStep()', function () {
        loadPageContent();

        $scope.goToNextStep();

        expect($location.path()).toBe('/step/uc/sc/searchResults.jsp/0/1');
    });

    it('goToPreviousPage()', function () {
        loadPageContent();

        $scope.goToPreviousPage();

        expect($location.path()).toBe('/step/uc/sc/startSearch.jsp/0/1');
    });

    it('goToNextPage()', function () {
        loadPageContent();

        $scope.goToNextPage();

        expect($location.path()).toBe('/step/uc/sc/contentPage.jsp/0/0');
    });

    it('goToPreviousVariant()', function () {
        loadPageContent();

        $scope.goToPreviousVariant();

        expect($location.path()).toBe('/step/Find Page/find_page_no_result/searchResults.jsp/0/0');
    });

    it('goToNextVariant()', function () {
        loadPageContent();

        $scope.goToNextVariant();

        expect($location.path()).toBe('/step/Find Page/find_page_with_text_on_page_from_multiple_results/searchResults.jsp/0/1');
    });

    function loadPageContent() {
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/configuration').respond(TestData.CONFIG);
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/branch/trunk/build/current/usecase/uc/scenario/sc').respond(TestData.SCENARIO);
        $httpBackend.whenGET(HostnameAndPort.forTest() + 'rest/branch/trunk/build/current/usecase/uc/scenario/sc/pageName/pn/pageOccurrence/0/stepInPageOccurrence/1').respond(TestData.STEP);

        Config.load();
        $httpBackend.flush();
    }

});

