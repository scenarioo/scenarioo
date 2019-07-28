import {ScenariosComponent} from '../../../../app/build/scenarios-overview/scenarios-overview.component';

describe('ScenariosComponent', () => {
    let component: ScenariosComponent;

    const BRANCH = 'branch_123',
         BUILD = 'build_123',
         USE_CASE = 'LogIn';

    beforeEach(() => {
        // component = new ScenariosComponent(BRANCH, BUILD, USE_CASE);
    });

    it('should load all scenarios and and the selected use case', () => {

    });
});

// import {Observable, of} from 'rxjs';
//
// describe('ScenariosComponent', () => {
//
//     const BRANCH = 'branch_123',
//         BUILD = 'build_123',
//         USE_CASE = 'LogIn';
//
//     let $scope, routeParams, controller, UseCaseDiffInfoResource, ScenarioDiffInfosResource,
//         SelectedBranchAndBuildService, $location, RelatedIssueResource, TestData;
//     let labelConfigurationService: any;
//     const ConfigResourceMock = {
//         get: () => of({})
//     };
//     const ScenarioResourceMock = {
//         get: () => of({}),
//         getUseCaseScenarios: () => getFindAllScenariosFake()
//     };
//     const ConfigurationServiceMock = {
//         scenarioPropertiesInOverview: () => {
//             return of()
//         }
//     };
//     const SelectedBranchAndBuildServiceMock = {
//         callback: undefined,
//         selectedStep: {
//             branch: undefined,
//             build: undefined
//         },
//         selected: () => {
//             return {
//                 branch: SelectedBranchAndBuildServiceMock.selectedStep['branchName'],
//                 build: SelectedBranchAndBuildServiceMock.selectedStep['buildName'],
//             };
//         },
//         callOnSelectionChange: (callback) => {
//             SelectedBranchAndBuildServiceMock.callback = callback
//         },
//         update: (newStep) => {
//             SelectedBranchAndBuildServiceMock.selectedStep = newStep;
//             SelectedBranchAndBuildServiceMock.callback(newStep);
//         },
//     };
//     const DiffInfoServiceMock = {};
//     beforeEach(angular.mock.module('scenarioo.controllers'));
//
//     beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
//         // TODO: Remove after AngularJS Migration.
//         $provide.value('ConfigResource', ConfigResourceMock);
//         $provide.value('ScenarioResource', ScenarioResourceMock);
//         $provide.value('ConfigurationService', ConfigurationServiceMock);
//         $provide.value('SelectedBranchAndBuildService', SelectedBranchAndBuildServiceMock);
//         $provide.value('DiffInfoService', DiffInfoServiceMock);
//         $provide.value('ScenarioDiffInfosResource', {
//             get() {
//             }
//         });
//         $provide.value('UseCaseDiffInfoResource', {
//             get() {
//             }
//         });
//         $provide.value('RelatedIssueResource', {
//             get() {
//             },
//             getForStepsOverview() {
//             },
//             getForScenariosOverview() {
//             }
//         });
//         $provide.value('SketchIdsResource', {});
//     }));
//
//     beforeEach(inject(($rootScope, $routeParams, $controller, _RelatedIssueResource_, _UseCaseDiffInfoResource_, _ScenarioDiffInfosResource_,
//                        _ConfigurationService_, _SelectedBranchAndBuildService_, _$location_, LocalStorageService, _TestData_) => {
//             $scope = $rootScope.$new();
//             routeParams = $routeParams;
//             routeParams.useCaseName = USE_CASE;
//
//             RelatedIssueResource = _RelatedIssueResource_;
//             UseCaseDiffInfoResource = _UseCaseDiffInfoResource_;
//             ScenarioDiffInfosResource = _ScenarioDiffInfosResource_;
//             SelectedBranchAndBuildService = _SelectedBranchAndBuildService_;
//             labelConfigurationService = {
//                 get(): Observable<any> {
//                     return of({});
//                 }
//             };
//             TestData = _TestData_;
//
//             $location = _$location_;
//
//             LocalStorageService.clearAll();
//
//             controller = $controller('UseCaseController', {
//                 $scope: $scope,
//                 $routeParams: routeParams,
//                 ConfigurationService: _ConfigurationService_,
//                 RelatedIssueResource: RelatedIssueResource,
//                 UseCaseDiffInfoResource: UseCaseDiffInfoResource,
//                 ScenarioDiffInfosResource: ScenarioDiffInfosResource,
//                 SelectedBranchAndBuildService: SelectedBranchAndBuildService,
//                 labelConfigurationService
//             });
//         }
//     ));
//
//     it('should load all scenarios and and the selected use case', () => {
//         spyOn(ScenarioResourceMock, 'getUseCaseScenarios').and.returnValue(getFindAllScenariosFake());
//         spyOn(RelatedIssueResource, 'get').and.callFake(getRelatedIssuesFake());
//         spyOn(UseCaseDiffInfoResource, 'get').and.callFake(getEmptyData());
//         spyOn(ScenarioDiffInfosResource, 'get').and.callFake(getEmptyData());
//
//         expect(SelectedBranchAndBuildService.selected().branch).toBeUndefined();
//         expect(SelectedBranchAndBuildService.selected().build).toBeUndefined();
//
//         $location.url('/new/path/?branch=' + BRANCH + '&build=' + BUILD);
//         $scope.$apply();
//
//         // TODO remove after AngularJS Migration and after removing SelectedBranchAndBuildServiceMock.
//         SelectedBranchAndBuildServiceMock.update({
//             branch: BRANCH,
//             build: BUILD
//         });
//
//         // TODO reactivate after AngularJS Migration and after removing SelectedBranchAndBuildServiceMock.
//         // expect(SelectedBranchAndBuildService.selected().branch).toBe(BRANCH);
//         // expect(SelectedBranchAndBuildService.selected().build).toBe(BUILD);
//         // $scope.$apply();
//
//         expect(ScenarioResourceMock.getUseCaseScenarios).toHaveBeenCalledWith({
//             'branchName': BRANCH,
//             'buildName': BUILD,
//         }, USE_CASE);
//
//         expect(controller.useCase).toBeDefined();
//         expect(controller.scenarios).toBeDefined();
//         expect(controller.propertiesToShow).toBeDefined();
//     });
//
//     function getFindAllScenariosFake() {
//         const DATA = {
//             useCase: 'useCase',
//             scenarios: getFakeScenarios()
//         };
//
//         return of(DATA);
//
//     }
//
//     function getRelatedIssuesFake() {
//         const DATA = {
//             0:
//                 {
//                     id: '1',
//                     name: 'fakeTestingIssue',
//                     firstScenarioSketchId: '1'
//                 }
//         };
//
//         return (params, onSuccess) => {
//             onSuccess(DATA);
//         };
//     }
//
//     function getFakeScenarios() {
//         const scenarios = [];
//         scenarios.push({
//             name: 'scenario'
//         });
//         return scenarios;
//     }
//
//     function getEmptyData() {
//         const DATA = {};
//
//         return (params, onSuccess) => {
//             onSuccess(DATA);
//         };
//     }
//
// });
