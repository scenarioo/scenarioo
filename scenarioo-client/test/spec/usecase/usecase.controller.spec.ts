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
import {IConfiguration} from '../../../app/generated-types/backend-types';

declare var angular: angular.IAngularStatic;
import {Observable, of, ReplaySubject} from 'rxjs';



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
        configuration : new ReplaySubject<IConfiguration>(1),

        getConfiguration: () => {
            ConfigurationServiceMock.configuration.next(TestData.CONFIG);
            return ConfigurationServiceMock.configuration.asObservable();
        },
        updateConfiguration: (newConfig: IConfiguration) => {
            ConfigurationServiceMock.configuration.next(newConfig);
        }
    };
    beforeEach(angular.mock.module('scenarioo.controllers'));

    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.
        $provide.value("ConfigResource", ConfigResourceMock);
        $provide.value("ScenarioResource", ScenarioResourceMock);
        $provide.value('ConfigurationService', ConfigurationServiceMock);
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

    // TODO reactivate after AngularJS Migration. Automatically injecting SelectedBranchAndBuildService into the test does not work at the moment and there are too many dependencies to mock this class away.
    xit('should load all scenarios and and the selected use case', () => {
        spyOn(ScenarioResourceMock, 'getUseCaseScenarios').and.returnValue(getFindAllScenariosFake());
        spyOn(RelatedIssueResource, 'query').and.callFake(queryRelatedIssuesFake());
        spyOn(UseCaseDiffInfoResource, 'get').and.callFake(getEmptyData());
        spyOn(ScenarioDiffInfosResource, 'get').and.callFake(getEmptyData());

        expect(SelectedBranchAndBuildService.selected().branch).toBeUndefined();
        expect(SelectedBranchAndBuildService.selected().build).toBeUndefined();

        $location.url('/new/path/?branch=' + BRANCH + '&build=' + BUILD);
        $scope.$apply();

        expect(SelectedBranchAndBuildService.selected().branch).toBe(BRANCH);
        expect(SelectedBranchAndBuildService.selected().build).toBe(BUILD);

        $scope.$apply();

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
