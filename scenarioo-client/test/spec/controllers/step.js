'use strict';

describe('StepCtrl', function () {

    var $scope, $routeParams, $location, $q, $window, Config, ScenarioService, PageVariantService, StepService, HostnameAndPort, SelectedBranchAndBuild;
    var StepCtrl;

    var METADATA_TYPE = 'some_type';

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$rootScope_, _$routeParams_, _$location_, _$q_, _$window_, _Config_, _ScenarioService_, _PageVariantService_, _StepService_, _HostnameAndPort_, _SelectedBranchAndBuild_, $controller) {
        $scope = _$rootScope_.$new();
        $routeParams = _$routeParams_;
        $location = _$location_;
        $q = _$q_;
        $window = _$window_;
        Config = _Config_;
        ScenarioService = _ScenarioService_;
        PageVariantService = _PageVariantService_;
        StepService = _StepService_;
        HostnameAndPort = _HostnameAndPort_;
        SelectedBranchAndBuild = _SelectedBranchAndBuild_;

        StepCtrl = $controller('StepCtrl', {$scope: $scope, $routeParams: $routeParams, $location: $location,
            $q: $q, $window: $window, Config: Config, ScenarioService: ScenarioService,
            PageVariantService: PageVariantService, StepService: StepService, HostnameAndPort: HostnameAndPort,
            SelectedBranchAndBuild: SelectedBranchAndBuild});
    }));

    it('shows all metadata sections collapsed first', function () {
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeTruthy();
    });

    it('collapses metadata sections on click', function() {
        $scope.toggleMetadataCollapsed(METADATA_TYPE);
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeFalsy();


        $scope.toggleMetadataCollapsed(METADATA_TYPE);
        expect($scope.isMetadataCollapsed(METADATA_TYPE)).toBeTruthy();
    });

});

