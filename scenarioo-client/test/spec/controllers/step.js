'use strict';

describe('StepCtrl', function () {

    var $scope, $routeParams, $location, $q, $window, Config, ScenarioResource, PageVariantService, StepService,
        HostnameAndPort, SelectedBranchAndBuild, $httpBackend, TestData;
    var StepCtrl;

    var METADATA_TYPE = 'some_type';

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$rootScope_, _$routeParams_, _$location_, _$q_, _$window_, _Config_, _ScenarioResource_,
                                _PageVariantService_, _StepService_, _HostnameAndPort_, _SelectedBranchAndBuild_,
                                $controller, _$httpBackend_, _TestData_) {
        $scope = _$rootScope_.$new();
        $routeParams = _$routeParams_;
        $location = _$location_;
        $q = _$q_;
        $window = _$window_;
        Config = _Config_;
        ScenarioResource = _ScenarioResource_;
        PageVariantService = _PageVariantService_;
        StepService = _StepService_;
        HostnameAndPort = _HostnameAndPort_;
        SelectedBranchAndBuild = _SelectedBranchAndBuild_;
        $httpBackend = _$httpBackend_;
        TestData = _TestData_;

        $routeParams.useCaseName = 'uc';
        $routeParams.scenarioName = 'sc';
        $routeParams.pageName = 'pn';
        $routeParams.pageIndex = 0;
        $routeParams.stepIndex = 0;

        StepCtrl = $controller('StepCtrl', {$scope: $scope, $routeParams: $routeParams, $location: $location,
            $q: $q, $window: $window, Config: Config, ScenarioResource: ScenarioResource,
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

    it('loads the step data', function() {
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/configuration').respond(TestData.CONFIG);
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/branches/trunk/builds/current/search/pagevariants').respond(TestData.PAGE_VARIANTS);
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/branches/trunk/builds/current/usecases/uc/scenarios/sc').respond(TestData.SCENARIO);
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/branches/trunk/builds/current/usecases/uc/scenarios/sc/steps/0').respond(TestData.STEP);

        Config.load();
        $httpBackend.flush();

        expect($scope.step).toEqualData(TestData.STEP);
    });

});

