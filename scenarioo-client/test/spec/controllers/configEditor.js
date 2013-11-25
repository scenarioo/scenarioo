'use strict';

describe('Controller :: ConfigEditorCtrl', function () {

    var $rootScope, $controller, BranchesResource, Config, $httpBackend, $scope, ConfigCtrl, HostnameAndPort, TestData;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$rootScope_, _$controller_, _BranchesResource_, _Config_, _$httpBackend_, _HostnameAndPort_, _TestData_) {
        $rootScope = _$rootScope_;
        $controller = _$controller_;
        BranchesResource = _BranchesResource_;
        Config = _Config_;
        $httpBackend = _$httpBackend_;
        HostnameAndPort = _HostnameAndPort_;
        TestData = _TestData_;

        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/branches').respond(TestData.BRANCHES);
        $httpBackend.whenGET(HostnameAndPort.forTest() + '/scenarioo/rest/configuration').respond(TestData.CONFIG);

        $scope = $rootScope.$new();
        ConfigCtrl = $controller('ConfigEditorCtrl', {$scope: $scope, BranchesResource: BranchesResource, Config: Config});
    }));

    describe('when page is loaded', function () {
        it('loads and displays the config from the server', function () {
            expect(ConfigCtrl).toBeDefined();
            expect($scope.configuration).toBeUndefined();

            $httpBackend.flush();

            expect($scope.configuration).toEqualData(TestData.CONFIG);
        });

        it('loads all branches and builds', function () {
            expect($scope.branches).toBeUndefined();
            expect($scope.configuredBranch).toBeUndefined();

            $httpBackend.flush();

            expect($scope.branches).toBeDefined();
            expect($scope.configuredBranch).toBeDefined();
        });
    });

    describe('when reset button is clicked', function () {
        it('resets the config to the loaded values', function () {
            $httpBackend.flush();

            changeAllValues();

            $scope.resetConfiguration();

            expect($scope.configuration).toEqualData(TestData.CONFIG);
        });
    });

    describe('when the save button is clicked', function () {
        it('saves the edited config', function () {
            spyOn(Config, 'updateConfiguration');

            $httpBackend.flush();

            changeAllValues();

            $scope.updateConfiguration();

            expect(Config.updateConfiguration).toHaveBeenCalled();
        });
    });

    function changeAllValues() {
        $scope.configuration.defaultBuildName = 'new build';
        $scope.configuration.defaultBranchName = 'new branch';
        $scope.configuration.scenarioPropertiesInOverview = 'abc';
        $scope.configuration.applicationInformation = 'new information';
        $scope.configuration.testDocumentationDirPath = 'new path';
    }

});