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

describe('ScenarioController', function () {

    var $scope, $httpBackend, $routeParams, ConfigService, TestData, HostNameAndPort, ScenarioController;

    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(inject(function ($rootScope, $controller, _$httpBackend_, _$routeParams_, _ConfigService_, _TestData_, _HostnameAndPort_, LocalStorageService) {
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        $routeParams = _$routeParams_;
        ConfigService = _ConfigService_;
        TestData = _TestData_;
        HostNameAndPort = _HostnameAndPort_;

        $routeParams.useCaseName = 'SearchUseCase';
        $routeParams.scenarioName = 'NotFoundScenario';

        LocalStorageService.clearAll();

        ScenarioController = $controller('ScenarioController', {$scope: $scope, ConfigService: ConfigService});
    }));

    it('clears search field when resetSearchField() is called', function () {
        ScenarioController.searchFieldText = 'test';
        ScenarioController.resetSearchField();
        expect(ScenarioController.searchFieldText).toBe('');
    });

    it('creates the correct link to a step', function () {
        var link = ScenarioController.getLinkToStep('searchPage.html', 2, 0);
        expect(link).toBe('#/step/SearchUseCase/NotFoundScenario/searchPage.html/2/0');
    });

    it('creates empty image link, if branch and build selection is unknown', function () {
        var imageLink = ScenarioController.getScreenShotUrl('img.jpg');
        expect(imageLink).toBeUndefined();
    });

    it('creates the correct image link, if selected branch and build is known', function () {
        givenScenarioIsLoaded();

        var imageLink = ScenarioController.getScreenShotUrl('img.jpg');
        expect(imageLink).toBe(HostNameAndPort.forLink() + 'rest/branch/trunk/build/current/usecase/SearchUseCase/scenario/NotFoundScenario/image/img.jpg');
    });

    it('does not show all steps of a page by default', function () {
        expect(ScenarioController.showAllStepsForPage(0)).toBeFalsy();
        expect(ScenarioController.showAllStepsForPage(1)).toBeFalsy();
        expect(ScenarioController.showAllStepsForPage(2)).toBeFalsy();
    });

    it('can toggle the showPageForAllSteps property', function () {
        ScenarioController.toggleShowAllStepsForPage(5);
        expect(ScenarioController.showAllStepsForPage(5)).toBeTruthy();
        ScenarioController.toggleShowAllStepsForPage(5);
        expect(ScenarioController.showAllStepsForPage(5)).toBeFalsy();
    });

    it('hides the "expand all" button, if all expandable pages are already expanded', function () {
        givenScenarioIsLoaded();

        ScenarioController.toggleShowAllStepsForPage(0);
        ScenarioController.toggleShowAllStepsForPage(1);

        expect(ScenarioController.isExpandAllPossible()).toBeFalsy();
    });

    it('shows the "expand all" button, if at least one expandable page is collapsed', function () {
        givenScenarioIsLoaded();

        expect(ScenarioController.isExpandAllPossible()).toBeTruthy();
    });


    it('hides the "collapse all" button, if all pages are collapsed already', function () {
        givenScenarioIsLoaded();

        // all pages are collapsed by default

        expect(ScenarioController.isCollapseAllPossible()).toBeFalsy();
    });

    it('shows the "collapse all" button, if at least one collapsable page is expanded', function () {
        givenScenarioIsLoaded();

        ScenarioController.toggleShowAllStepsForPage(1);

        expect(ScenarioController.isCollapseAllPossible()).toBeTruthy();
    });

    it('collapses all pages if the user clicks "collapse all"', function () {
        ScenarioController.toggleShowAllStepsForPage(2);
        ScenarioController.toggleShowAllStepsForPage(5);
        ScenarioController.collapseAll();
        expect(ScenarioController.showAllStepsForPage(2)).toBeFalsy();
        expect(ScenarioController.showAllStepsForPage(5)).toBeFalsy();
    });


    it('expands all pages if the user clicks "expand all"', function () {
        givenScenarioIsLoaded();

        ScenarioController.expandAll();
        expectAllPagesAreExpanded();
    });

    it('expands all pages, if this is the default set in the config', function () {
        ConfigService.getRaw = true;

        givenScenarioIsLoaded(TestData.CONFIG_PAGES_EXPANDED);

        expectAllPagesAreExpanded();
    });

    function givenScenarioIsLoaded(config) {
        if (angular.isUndefined(config)) {
            config = TestData.CONFIG;
        }

        $httpBackend.whenGET(HostNameAndPort.forLink() + 'rest/configuration').respond(config);
        $httpBackend.whenGET(HostNameAndPort.forLink() + 'rest/branch/trunk/build/current/usecase/SearchUseCase/scenario/NotFoundScenario').respond(TestData.SCENARIO);
        $httpBackend.whenGET(HostNameAndPort.forTest() + 'rest/labelconfigurations').respond({});

        ConfigService.load();
        $httpBackend.flush();
    }

    function expectAllPagesAreExpanded() {
        expect(ScenarioController.showAllStepsForPage(0)).toBeTruthy();
        expect(ScenarioController.showAllStepsForPage(1)).toBeTruthy();
        expect(ScenarioController.showAllStepsForPage(2)).toBeFalsy(); // Scenario has only 2 pages
    }
});
