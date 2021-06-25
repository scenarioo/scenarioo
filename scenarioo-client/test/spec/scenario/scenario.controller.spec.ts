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

import {of, ReplaySubject} from 'rxjs';
import {IConfiguration, IUseCaseScenarios} from '../../../app/generated-types/backend-types';

declare var angular: angular.IAngularStatic;

// Migration - do not invest much time in old tests
xdescribe('ScenarioController', () => {

    let $scope, $httpBackend, $routeParams, ConfigurationService, TestData, ScenarioController, SelectedBranchAndBuildService;

    const LabelConfigurationsResourceMock = {
        query: () => of({}),
    };
    const ScenarioResourceMock = {
        get: () => of(TestData.SCENARIO),
        getUseCaseScenarios: () => of<IUseCaseScenarios>({
            useCase: TestData.SCENARIO.useCase,
            scenarios: [TestData.SCENARIO.scenario]
        })
    };
    const ConfigurationServiceMock = {
        configuration : new ReplaySubject<IConfiguration>(1),
        expandPagesInScenarioOverviewValue : undefined,

        getConfiguration: () => {
            ConfigurationServiceMock.configuration.next(TestData.CONFIG);
            return ConfigurationServiceMock.configuration.asObservable();
        },
        updateConfiguration: (newConfig: IConfiguration) => {
            ConfigurationServiceMock.configuration.next(newConfig);
            ConfigurationServiceMock.expandPagesInScenarioOverviewValue = newConfig['expandPagesInScenarioOverview'];
            SelectedBranchAndBuildServiceMock.update(newConfig);
        },
        expandPagesInScenarioOverview: () => {
            return ConfigurationServiceMock.expandPagesInScenarioOverviewValue;
        },
    };
    const SelectedBranchAndBuildServiceMock = {
        callback: undefined,
        selected: () => {
            return {
                branch: TestData.CONFIG['defaultBranchName'],
                build: TestData.CONFIG['defaultBuildName'],
            };
        },
        callOnSelectionChange: (callback) => {
            SelectedBranchAndBuildServiceMock.callback = callback},
        update: (newConfig) => {
            SelectedBranchAndBuildServiceMock.callback({
                branch: newConfig['defaultBranchName'],
                build: newConfig['defaultBuildName'],
            });
        },
    };

    const RelatedIssueResourceMock = {
        getForStepsOverview: () => of( {
            0:
                {
                    id: '1',
                    name: 'fakeTestingIssue',
                    firstScenarioSketchId: '1'
                }
        })
    };
    const DiffInfoServiceMock = {};
    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        $provide.value('ScenarioResource', ScenarioResourceMock);
        $provide.value('SelectedBranchAndBuildService', SelectedBranchAndBuildServiceMock);
        $provide.value('DiffInfoService', DiffInfoServiceMock);
        $provide.value('LabelConfigurationsResource', LabelConfigurationsResourceMock);
        $provide.value('ConfigurationService', ConfigurationServiceMock);
        $provide.value('BuildDiffInfoResource', {});
        $provide.value('UseCaseDiffInfoResource', {});
        $provide.value('ScenarioDiffInfoResource', {});
        $provide.value('StepDiffInfosResource', {});
        $provide.value('RelatedIssueResource', RelatedIssueResourceMock);
        $provide.value('SketchIdsResource', {});
    }));

    beforeEach(inject(($controller, $rootScope, _$httpBackend_, _$routeParams_, _TestData_, localStorageService,
                       _ConfigurationService_, _SelectedBranchAndBuildService_
        ) => {
            $scope = $rootScope.$new();
            $httpBackend = _$httpBackend_;
            $routeParams = _$routeParams_;
            TestData = _TestData_;
            ConfigurationService = _ConfigurationService_;

            SelectedBranchAndBuildService = _SelectedBranchAndBuildService_;

            $routeParams.useCaseName = 'SearchUseCase';
            $routeParams.scenarioName = 'NotFoundScenario';

            localStorageService.clearAll();

            ScenarioController = $controller('ScenarioController', {$scope: $scope});
        }
    ));

    it('clears search field when resetSearchField() is called', () => {
        ScenarioController.searchFieldText = 'test';
        ScenarioController.resetSearchField();
        expect(ScenarioController.searchFieldText).toBe('');
    });

    it('creates the correct link to a step', () => {
        const link = ScenarioController.getLinkToStep('searchPage.html', 2, 0);
        expect(link).toBe('#/step/SearchUseCase/NotFoundScenario/searchPage.html/2/0');
    });

    it('creates empty image link, if branch and build selection is unknown', () => {
        const imageLink = ScenarioController.getScreenShotUrl('img.jpg');
        expect(imageLink).toBeUndefined();
    });

    it('creates the correct image link, if selected branch and build is known', () => {
        givenScenarioIsLoaded();

        const imageLink = ScenarioController.getScreenShotUrl('img.jpg');
        expect(imageLink).toBe('rest/branch/trunk/build/current/usecase/SearchUseCase/scenario/NotFoundScenario/image/img.jpg');
    });

    it('does not show all steps of a page by default', () => {
        expect(ScenarioController.showAllStepsForPage(0)).toBeFalsy();
        expect(ScenarioController.showAllStepsForPage(1)).toBeFalsy();
        expect(ScenarioController.showAllStepsForPage(2)).toBeFalsy();
    });

    it('can toggle the showPageForAllSteps property', () => {
        ScenarioController.toggleShowAllStepsForPage(5);
        expect(ScenarioController.showAllStepsForPage(5)).toBeTruthy();
        ScenarioController.toggleShowAllStepsForPage(5);
        expect(ScenarioController.showAllStepsForPage(5)).toBeFalsy();
    });

    it('hides the expand all button, if all expandable pages are already expanded', () => {
        givenScenarioIsLoaded();

        ScenarioController.toggleShowAllStepsForPage(0);
        ScenarioController.toggleShowAllStepsForPage(1);

        expect(ScenarioController.isExpandAllPossible()).toBeFalsy();
    });

    it('shows the expand all button, if at least one expandable page is collapsed', () => {
        givenScenarioIsLoaded();

        expect(ScenarioController.isExpandAllPossible()).toBeTruthy();
    });


    it('hides the collapse all button, if all pages are collapsed already', () => {
        givenScenarioIsLoaded();

        // all pages are collapsed by default

        expect(ScenarioController.isCollapseAllPossible()).toBeFalsy();
    });

    it('shows the collapse all button, if at least one collapsable page is expanded', () => {
        givenScenarioIsLoaded();

        ScenarioController.toggleShowAllStepsForPage(1);

        expect(ScenarioController.isCollapseAllPossible()).toBeTruthy();
    });

    it('collapses all pages if the user clicks collapse all', () => {
        ScenarioController.toggleShowAllStepsForPage(2);
        ScenarioController.toggleShowAllStepsForPage(5);
        ScenarioController.collapseAll();
        expect(ScenarioController.showAllStepsForPage(2)).toBeFalsy();
        expect(ScenarioController.showAllStepsForPage(5)).toBeFalsy();
    });


    it('expands all pages if the user clicks expand all', () => {
        givenScenarioIsLoaded();

        ScenarioController.expandAll();
        expectAllPagesAreExpanded();
    });

    it('expands all pages, if this is the default set in the config', () => {
        ConfigurationService.getRaw = true;

        givenScenarioIsLoaded(TestData.CONFIG_PAGES_EXPANDED);

        expectAllPagesAreExpanded();
    });

    function givenScenarioIsLoaded(config?: IConfiguration) {
        if (angular.isUndefined(config)) {
            config = TestData.CONFIG;
        }
        spyOn(ScenarioResourceMock, 'getUseCaseScenarios').and.returnValue(of(TestData.SCENARIO));
        ConfigurationService.updateConfiguration(config);
        $scope.$apply();

    }

    function expectAllPagesAreExpanded() {
        expect(ScenarioController.showAllStepsForPage(0)).toBeTruthy();
        expect(ScenarioController.showAllStepsForPage(1)).toBeTruthy();
        expect(ScenarioController.showAllStepsForPage(2)).toBeFalsy(); // Scenario has only 2 pages
    }
});
