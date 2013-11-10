'use strict';

describe('Controller :: useCase', function () {

    var BRANCH = 'branch_123',
        BUILD = 'build_123',
        USE_CASE = 'LogIn';

    var scope, routeParams, configMock, controller, scenarioService;

    beforeEach(module('ngUSDClientApp.controllers'));

    beforeEach(inject(function ($rootScope, $routeParams, $controller, ScenarioService, ConfigMock) {
            scope = $rootScope.$new();
            routeParams = $routeParams;
            routeParams.useCaseName = USE_CASE;
            configMock = ConfigMock;
            scenarioService = ScenarioService;

            controller = $controller("UseCaseCtrl", {$scope: scope, $routeParams: routeParams, Config: ConfigMock, ScenarioService: scenarioService});
        }
    ));

    it('should load all scenarios and and the selected use case', function() {
        spyOn(scenarioService, 'findAllScenarios').andCallFake(getFindAllScenariosFake());

        expect(configMock.selectedBranch()).toBe(undefined);
        expect(configMock.selectedBuild()).toBe(undefined);
        expect(scope.useCase).toBeUndefined();
        expect(scope.scenarios).toBeUndefined();
        expect(scope.propertiesToShow).toBeUndefined();

        configMock.setSelectedBranch(BRANCH);
        configMock.setSelectedBuild(BUILD);
        expect(configMock.selectedBranch()).toBe(BRANCH);
        expect(configMock.selectedBuild()).toBe(BUILD);

        scope.$apply();

        expect(scenarioService.findAllScenarios).toHaveBeenCalledWith({'branchName': BRANCH, 'buildName': BUILD, 'usecaseName': USE_CASE});
        expect(scope.useCase).toBeDefined();
        expect(scope.scenarios).toBeDefined();
        expect(scope.propertiesToShow).toBeDefined();
    });

    function getFindAllScenariosFake() {
        return function() {
            return {
                then: function(callback) {
                    callback({
                        useCase: 'useCase',
                        scenarios: 'scenarios'
                    });
                }
            };
        }
    }

});
