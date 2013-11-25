'use strict';

describe('Controller: MainCtrl', function () {

    var $controller, $rootScope, $location, $httpBackend, SelectedBranchAndBuild, HostnameAndPort,
        UseCaseService, BranchesAndBuilds, TestData;
    var $scope;
    var MainCtrl;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$controller_, _$rootScope_, _$location_, _$httpBackend_, _SelectedBranchAndBuild_,
                                _UseCaseService_, _BranchesAndBuilds_, _HostnameAndPort_, _TestData_) {
        $controller = _$controller_;
        $rootScope = _$rootScope_;
        $location = _$location_;
        $httpBackend = _$httpBackend_;
        SelectedBranchAndBuild = _SelectedBranchAndBuild_;
        UseCaseService = _UseCaseService_;
        BranchesAndBuilds = _BranchesAndBuilds_;
        HostnameAndPort = _HostnameAndPort_;
        TestData = _TestData_;

        $scope = $rootScope.$new();
        MainCtrl = $controller('MainCtrl', {$scope: $scope, $location: $location,
            SelectedBranchAndBuild: SelectedBranchAndBuild, UseCaseService: UseCaseService,
            BranchesAndBuilds: BranchesAndBuilds});
    }));

    it('navigates to use case page when use case row is clicked', function () {
        expect($location.path()).toBe('');

        $scope.goToUseCase('SelectPhoneNumber');

        expect($location.path()).toBe('/usecase/SelectPhoneNumber');
    });

    it('loads usecases when branch and build selection changes', function () {
        var USECASES_URL = HostnameAndPort.forTest() + '/scenarioo/rest/branches/release-branch-2014-01-16/builds/example-build/usecases';
        var BRANCHES_URL = HostnameAndPort.forTest() + '/scenarioo/rest/branches';

        expect($scope.useCases).toBeUndefined();
        expect($scope.branchesAndBuilds).toBeUndefined();
        $httpBackend.whenGET(USECASES_URL).respond(TestData.USECASES);
        $httpBackend.whenGET(BRANCHES_URL).respond(TestData.BRANCHES);

        $location.url('/?branch=release-branch-2014-01-16&build=example-build');
        $scope.$apply();

        $httpBackend.flush();

        expect($scope.useCases).toEqualData(TestData.USECASES);
        expect($scope.branchesAndBuilds.branches).toEqualData(TestData.BRANCHES);
        expect($scope.branchesAndBuilds.selectedBranch).toEqualData(TestData.BRANCHES[1]);
        expect($scope.branchesAndBuilds.selectedBuild).toEqualData(TestData.BRANCHES[1].builds[0]);
    });

});
