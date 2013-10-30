'use strict';

describe('Controller :: useCase', function () {

    var BRANCH = 'branch_123',
        BUILD = 'build_123',
        USE_CASE = 'LogIn';

    var scope, routeParams, configFake, controller, scenarioService;

    beforeEach(
        angular.mock.module('ngUSDClientApp.controllers')
    );

    beforeEach(inject(function ($rootScope, $routeParams, $controller, ScenarioService) {
            scope = $rootScope.$new();
            routeParams = $routeParams;
            routeParams.useCaseName = USE_CASE;
            configFake = createConfigFake();
            scenarioService = ScenarioService;

            controller = $controller("UseCaseCtrl", {$scope: scope, $routeParams: routeParams, Config: configFake, ScenarioService: scenarioService});
        }
    ));

    it('should load all scenarios and and the selected use case', function() {
        spyOn(scenarioService, 'findAllScenarios').andCallFake(getFindAllScenariosFake());

        expect(configFake.selectedBranch()).toBe(undefined);
        expect(configFake.selectedBuild()).toBe(undefined);
        expect(scope.useCase).toBeUndefined();
        expect(scope.scenarios).toBeUndefined();
        expect(scope.propertiesToShow).toBeUndefined();

        configFake.setSelectedBranch(BRANCH);
        configFake.setSelectedBuild(BUILD);
        expect(configFake.selectedBranch()).toBe(BRANCH);
        expect(configFake.selectedBuild()).toBe(BUILD);

        scope.$apply();

        expect(scenarioService.findAllScenarios).toHaveBeenCalledWith({'branchName': BRANCH, 'buildName': BUILD, 'usecaseName': USE_CASE});
        expect(scope.useCase).toBeDefined();
        expect(scope.scenarios).toBeDefined();
        expect(scope.propertiesToShow).toBeDefined();
    });

    function createConfigFake() {
        return {
            branch : undefined,
            build : undefined,

            selectedBranch : function() {
                return this.branch;
            },

            selectedBuild : function() {
                return this.build;
            },

            setSelectedBranch : function(branch) {
                this.branch = branch;
            },

            setSelectedBuild : function(build) {
                this.build = build;
            },

            scenarioPropertiesInOverview : function() {
                return [
                    {
                        text: 'User Profile',
                        property: 'details.properties.userProfile',
                        attr: 'userProfile'
                    }
                ];
            }
        };
    }

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
