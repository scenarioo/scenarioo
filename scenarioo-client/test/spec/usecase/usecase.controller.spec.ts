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
import {Observable, of} from 'rxjs';

declare var angular: angular.IAngularStatic;


describe('UseCaseController', () => {

    const BRANCH = 'branch_123',
        BUILD = 'build_123',
        USE_CASE = 'LogIn';

    let $scope, routeParams, controller, UseCaseDiffInfoResource, ScenarioDiffInfosResource,
        SelectedBranchAndBuildService, $location, RelatedIssueResource, TestData;
    let labelConfigurationService: any;
    const ConfigResourceMock = {
        get: () => of({})
    };
    const ScenarioResourceMock = {
        get: () => of({}),
        getUseCaseScenarios: () => getFindAllScenariosFake()
    };
    const ConfigurationServiceMock = {
        scenarioPropertiesInOverview: () => {
            return of()
        }
    };
    const SelectedBranchAndBuildServiceMock = {
        callback: undefined,
        selectedStep: {
            branch: undefined,
            build: undefined
        },
        selected: () => {
            return {
                branch: SelectedBranchAndBuildServiceMock.selectedStep['branchName'],
                build: SelectedBranchAndBuildServiceMock.selectedStep['buildName'],
            };
        },
        callOnSelectionChange: (callback) => {
            SelectedBranchAndBuildServiceMock.callback = callback
        },
        update: (newStep) => {
            SelectedBranchAndBuildServiceMock.selectedStep = newStep;
            SelectedBranchAndBuildServiceMock.callback(newStep);
        },
    };
    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.
        $provide.value('ConfigResource', ConfigResourceMock);
        $provide.value('ScenarioResource', ScenarioResourceMock);
        $provide.value('ConfigurationService', ConfigurationServiceMock);
        $provide.value('SelectedBranchAndBuildService', SelectedBranchAndBuildServiceMock);
        $provide.value('ScenarioDiffInfosResource', {
            get() {
            }
        });
        $provide.value('UseCaseDiffInfoResource', {
            get() {
            }
        });
    }));

    beforeEach(inject(($rootScope, $routeParams, $controller, _RelatedIssueResource_, _UseCaseDiffInfoResource_, _ScenarioDiffInfosResource_,
                       _ConfigurationService_, _SelectedBranchAndBuildService_, _$location_, LocalStorageService, _TestData_) => {
            $scope = $rootScope.$new();
            routeParams = $routeParams;
            routeParams.useCaseName = USE_CASE;

            RelatedIssueResource = _RelatedIssueResource_;
            UseCaseDiffInfoResource = _UseCaseDiffInfoResource_;
            ScenarioDiffInfosResource = _ScenarioDiffInfosResource_;
            SelectedBranchAndBuildService = _SelectedBranchAndBuildService_;
            labelConfigurationService = {
                get(): Observable<any> {
                    return of({});
                }
            };
            TestData = _TestData_;

            $location = _$location_;

            LocalStorageService.clearAll();

            controller = $controller('UseCaseController', {
                $scope: $scope,
                $routeParams: routeParams,
                ConfigurationService: _ConfigurationService_,
                RelatedIssueResource: RelatedIssueResource,
                UseCaseDiffInfoResource: UseCaseDiffInfoResource,
                ScenarioDiffInfosResource: ScenarioDiffInfosResource,
                SelectedBranchAndBuildService: SelectedBranchAndBuildService,
                labelConfigurationService
            });
        }
    ));

    it('should load all scenarios and and the selected use case', () => {
        spyOn(ScenarioResourceMock, 'getUseCaseScenarios').and.returnValue(getFindAllScenariosFake());
        spyOn(RelatedIssueResource, 'query').and.callFake(queryRelatedIssuesFake());
        spyOn(UseCaseDiffInfoResource, 'get').and.callFake(getEmptyData());
        spyOn(ScenarioDiffInfosResource, 'get').and.callFake(getEmptyData());

        expect(SelectedBranchAndBuildService.selected().branch).toBeUndefined();
        expect(SelectedBranchAndBuildService.selected().build).toBeUndefined();

        $location.url('/new/path/?branch=' + BRANCH + '&build=' + BUILD);
        $scope.$apply();

        // TODO remove after AngularJS Migration and after removing SelectedBranchAndBuildServiceMock.
        SelectedBranchAndBuildServiceMock.update({
            branch: BRANCH,
            build: BUILD
        });

        // TODO reactivate after AngularJS Migration and after removing SelectedBranchAndBuildServiceMock.
        // expect(SelectedBranchAndBuildService.selected().branch).toBe(BRANCH);
        // expect(SelectedBranchAndBuildService.selected().build).toBe(BUILD);
        // $scope.$apply();

        expect(ScenarioResourceMock.getUseCaseScenarios).toHaveBeenCalledWith({
            'branchName': BRANCH,
            'buildName': BUILD,
        }, USE_CASE);

        expect(controller.useCase).toBeDefined();
        expect(controller.scenarios).toBeDefined();
        expect(controller.propertiesToShow).toBeDefined();
    });

    function getFindAllScenariosFake() {
        const DATA = {
            useCase: 'useCase',
            scenarios: getFakeScenarios()
        };

        return of(DATA);

    }

    function queryRelatedIssuesFake() {
        const DATA = {
            0:
                {
                    id: '1',
                    name: 'fakeTestingIssue',
                    firstScenarioSketchId: '1'
                }
        };

        return (params, onSuccess) => {
            onSuccess(DATA);
        };
    }

    function getFakeScenarios() {
        const scenarios = [];
        scenarios.push({
            name: 'scenario'
        });
        return scenarios;
    }

    function getEmptyData() {
        const DATA = {};

        return (params, onSuccess) => {
            onSuccess(DATA);
        };
    }

});
