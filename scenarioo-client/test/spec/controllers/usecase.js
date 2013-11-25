'use strict';

describe('Controller :: useCase', function () {

    var BRANCH = 'branch_123',
        BUILD = 'build_123',
        USE_CASE = 'LogIn';

    var $scope, routeParams, configMock, controller, ScenarioResource, SelectedBranchAndBuild, $location;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function ($rootScope, $routeParams, $controller, _ScenarioResource_, ConfigMock, _SelectedBranchAndBuild_, _$location_) {
            $scope = $rootScope.$new();
            routeParams = $routeParams;
            routeParams.useCaseName = USE_CASE;
            configMock = ConfigMock;
            ScenarioResource = _ScenarioResource_;
            SelectedBranchAndBuild = _SelectedBranchAndBuild_;
            $location = _$location_;

            controller = $controller('UseCaseCtrl', {$scope: $scope, $routeParams: routeParams, Config: ConfigMock, ScenarioResource: ScenarioResource, SelectedBranchAndBuild: SelectedBranchAndBuild});
        }
    ));

    it('should load all scenarios and and the selected use case', function() {
        spyOn(ScenarioResource, 'get').andCallFake(getFindAllScenariosFake());

        expect(SelectedBranchAndBuild.selected().branch).toBeUndefined();
        expect(SelectedBranchAndBuild.selected().build).toBeUndefined();
        expect($scope.useCase).toBeUndefined();
        expect($scope.scenarios).toBeUndefined();
        expect($scope.propertiesToShow).toBeUndefined();

        $location.url('/new/path/?branch=' + BRANCH + '&build=' + BUILD);
        $scope.$apply();

        expect(SelectedBranchAndBuild.selected().branch).toBe(BRANCH);
        expect(SelectedBranchAndBuild.selected().build).toBe(BUILD);

        $scope.$apply();

        expect(ScenarioResource.get).toHaveBeenCalledWith({'branchName': BRANCH, 'buildName': BUILD, 'usecaseName': USE_CASE}, jasmine.any(Function));
        expect($scope.useCase).toBeDefined();
        expect($scope.scenarios).toBeDefined();
        expect($scope.propertiesToShow).toBeDefined();
    });

    function getFindAllScenariosFake() {
        var DATA = {
            useCase: 'useCase',
            scenarios: 'scenarios'
        };

        return function(params, onSuccess) {
            onSuccess(DATA);
        };
    }

});
