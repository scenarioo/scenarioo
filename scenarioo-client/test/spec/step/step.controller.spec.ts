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
declare var angular: angular.IAngularStatic;
import 'rxjs/add/observable/of';
import {Observable} from "rxjs";

describe('StepController', function () {

    let $scope, $routeParams, $location, $q, $window, ConfigService,
        StepResource,
        BuildDiffInfoResource, StepDiffInfoResource,
        SelectedBranchAndBuildService, DiffInfoService, BranchesResource,
        $controller, $httpBackend, TestData,
        RelatedIssueResource;

    const STEP_INFORMATION_TREE = {
        childNodes: [
            {nodeLabel: 'Step title', nodeValue: 'Search results'},
            {
                nodeLabel: 'Page name',
                childNodes: [],
                nodeValue: 'searchResults.jsp',
                nodeObjectName: 'searchResults.jsp',
            },
            {nodeLabel: 'url', nodeValue: 'http://en.wikipedia.org/wiki/Special:Search?search=yourSearchText&go=Go'},
            {nodeLabel: 'Build status', nodeValue: 'success'},
        ],
    };


    let ConfigResourceMock = {
        get: () => Observable.of(angular.copy(TestData.CONFIG)),
    };

    let LabelConfigurationsResourceMock = {
        query: () => Observable.of({}),
    };
    let ScenarioResourceMock = {
        get: () => Observable.of({}),
        getUseCaseScenarios: () => Observable.of({})
    };


    beforeEach(angular.mock.module('scenarioo.controllers'));
    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.
        $provide.value("BranchesResource", {
            query: () => {
            },
        });
        $provide.value("ConfigResource", ConfigResourceMock);
        $provide.value("LabelConfigurationsResource", LabelConfigurationsResourceMock);
        $provide.value("ScenarioResource", ScenarioResourceMock);
    }));

    beforeEach(inject(function (_$rootScope_, _$routeParams_, _$location_, _$q_, _$window_, _ConfigService_,
                                _StepResource_, _BuildDiffInfoResource_, _StepDiffInfoResource_,
                                _SelectedBranchAndBuildService_, _DiffInfoService_, _$controller_, _$httpBackend_,
                                _TestData_, LocalStorageService, _RelatedIssueResource_, _BranchesResource_) {
        $scope = _$rootScope_.$new();
        $routeParams = _$routeParams_;
        $location = _$location_;
        $q = _$q_;
        $window = _$window_;
        ConfigService = _ConfigService_;
        RelatedIssueResource = _RelatedIssueResource_;
        StepResource = _StepResource_;
        BuildDiffInfoResource = _BuildDiffInfoResource_;
        StepDiffInfoResource = _StepDiffInfoResource_;
        BranchesResource = _BranchesResource_;
        SelectedBranchAndBuildService = _SelectedBranchAndBuildService_;
        DiffInfoService = _DiffInfoService_;
        $controller = _$controller_;
        $httpBackend = _$httpBackend_;
        TestData = _TestData_;

        $routeParams.useCaseName = 'uc';
        $routeParams.scenarioName = 'sc';
        $routeParams.pageName = 'pn';
        $routeParams.pageOccurrence = 0;
        $routeParams.stepInPageOccurrence = 1;

        LocalStorageService.clearAll();
    }));

    describe('scenario is found', function () {

        beforeEach(function () {
            $routeParams.stepInPageOccurrence = 1;
            $controller('StepController', {
                $scope: $scope,
                $routeParams: $routeParams,
                $location: $location,
                $q: $q,
                $window: $window,
                ConfigService: ConfigService,
                ScenarioResource: ScenarioResourceMock,
                StepResource: StepResource,
                SelectedBranchAndBuildService: SelectedBranchAndBuildService,
                DiffInfoService: DiffInfoService,
                ApplicationInfoPopupService: {},
                SharePagePopupService: {},
            });
            spyOn(RelatedIssueResource, 'query').and.callFake(queryRelatedIssuesFake());
            spyOn(BranchesResource, 'query').and.returnValue(Observable.of({}));
            spyOn(BuildDiffInfoResource, 'get').and.callFake(getEmptyData());
            spyOn(StepDiffInfoResource, 'get').and.callFake(getEmptyData());
        });

        it('loads the step data', function () {
            loadPageContent();
            expect($scope.step).toEqual(TestData.STEP.step);
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

        it('getCurrentUrlForSharing returns the current URL plus the step labels.', function () {
            loadPageContent();

            const url = $scope.getCurrentUrlForSharing();

            expect(url).toBe('http://server/#?branch=trunk&build=current&comparison=Disabled&labels=normal-case,no%20results,step-label-0,public,page-label1,page-label2');
        });

        it('getScreenshotUrlForSharing returns the correct URL for sharing, including the image file extension.', function () {
            loadPageContent();

            const url = $scope.getScreenshotUrlForSharing();

            expect(url).toBe('http://server/rest/branch/trunk/build/current/usecase/uc/scenario/sc/pageName/pn/pageOccurrence/0/stepInPageOccurrence/1/image.png?labels=normal-case,no%20results,step-label-0,public,page-label1,page-label2');
        });

        function loadPageContent() {
            $httpBackend.whenGET('rest/configuration').respond(TestData.CONFIG);
            $httpBackend.whenGET('rest/branch/trunk/build/current/usecase/uc/scenario/sc').respond(TestData.SCENARIO);
            $httpBackend.whenGET('rest/branch/trunk/build/current/usecase/uc/scenario/sc/pageName/pn/pageOccurrence/0/stepInPageOccurrence/1').respond(TestData.STEP);

            ConfigService.load();
            $httpBackend.flush();
            expect($scope.stepNotFound).toBeFalsy();
        }

        function queryRelatedIssuesFake() {
            const DATA = {
                0:
                    {
                        id: '1',
                        name: 'fakeTestingIssue',
                        firstScenarioSketchId: '1',
                    },
            };

            return function (params, onSuccess) {
                onSuccess(DATA);
            };
        }
    });

    describe('step is not found', function () {

        beforeEach(function () {
            $routeParams.stepInPageOccurrence = 42;
            $controller('StepController', {
                $scope: $scope,
                $routeParams: $routeParams,
                $location: $location,
                $q: $q,
                $window: $window,
                ConfigService: ConfigService,
                ScenarioResource: ScenarioResourceMock,
                StepResource: StepResource,
                SelectedBranchAndBuildService: SelectedBranchAndBuildService,
                ApplicationInfoPopupService: {},
                SharePagePopupService: {},
            });
        });

        it('requested step is not found', function () {
            tryToLoadNotExistingStep();

            $scope.$apply();
            expect($scope.stepNotFound).toBeTruthy();
            expect($scope.httpResponse.status).toEqual(500);
            expect($scope.httpResponse.method).toEqual('GET');
            expect($scope.httpResponse.url).toEqual('rest/branch/trunk/build/current/usecase/uc/scenario/sc/pageName/pn/pageOccurrence/0/stepInPageOccurrence/42');
            expect($scope.httpResponse.data).toEqual('');
            expect($scope.getCurrentUrl()).toEqual(
                'http://server/#?branch=trunk&build=current&comparison=Disabled',
            );
        });

        function tryToLoadNotExistingStep() {
            $httpBackend.whenGET('rest/configuration').respond(TestData.CONFIG);
            $httpBackend.whenGET('rest/branch/trunk/build/current/usecase/uc/scenario/sc').respond(TestData.SCENARIO);
            $httpBackend.whenGET('rest/branch/trunk/build/current/usecase/uc/scenario/sc/pageName/pn/pageOccurrence/0/stepInPageOccurrence/42').respond(500, '');

            ConfigService.load();
            $httpBackend.flush();
        }

    });

    function getEmptyData() {
        const DATA = {};

        return function (params, onSuccess) {
            onSuccess(DATA);
        };
    }

});

