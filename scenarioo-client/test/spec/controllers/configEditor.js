'use strict';

describe('Controller :: ConfigEditorCtrl', function () {

    var $rootScope, $controller, BranchesResource, Config, $httpBackend, $scope, ConfigCtrl, HostnameAndPort;

    var DUMMY_CONFIG_RESPONSE = {
        'testDocumentationDirPath': 'webtestDocuContentExample',
        'defaultBuildName': 'current',
        'scenarioPropertiesInOverview': 'userProfile, configuration',
        'applicationInformation': 'This is my personal copy of Scenarioo :-)',
        'buildstates': {
            BUILD_STATE_FAILED: 'label-important',
            BUILD_STATE_SUCCESS: 'label-success',
            BUILD_STATE_WARNING: 'label-warning'
        },
        'defaultBranchName': 'trunk'
    };

    var DUMMY_BRANCHES_RESPONSE = '[{"branch":{"description":"","name":"2013-05"},"builds":[{"linkName":"2013-06-16_001226-497549","build":{"revision":"497549","details":{"properties":{"softwareVersion":"13.0.5"}},"date":1371334346000,"name":"2013-06-16_001226-497549","state":"success"}},{"linkName":"current","build":{"revision":"497549","details":{"properties":{"softwareVersion":"13.0.5"}},"date":1371334346000,"name":"2013-06-16_001226-497549","state":"success"}}]},{"branch":{"description":"","name":"trunk"},"builds":[{"linkName":"2013-06-05_001546-497000","build":{"revision":"497000","details":{"properties":{"softwareVersion":"13.1.1"}},"date":1370384146000,"name":"2013-06-05_001546-497000","state":"failed"}},{"linkName":"2013-06-14_001548-497444","build":{"revision":"497444","details":{"properties":{"softwareVersion":"13.1.1c"}},"date":1371161748000,"name":"2013-06-14_001548-497444","state":"success"}},{"linkName":"current","build":{"revision":"497444","details":{"properties":{"softwareVersion":"13.1.1c"}},"date":1371161748000,"name":"2013-06-14_001548-497444","state":"success"}}]}]';

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function (_$rootScope_, _$controller_, _BranchesResource_, _Config_, _$httpBackend_, _HostnameAndPort_) {
        $rootScope = _$rootScope_;
        $controller = _$controller_;
        BranchesResource = _BranchesResource_;
        Config = _Config_;
        $httpBackend = _$httpBackend_;
        HostnameAndPort = _HostnameAndPort_;

        $httpBackend.whenGET(HostnameAndPort.forNgResource() + '/scenarioo/rest/branches').respond(DUMMY_BRANCHES_RESPONSE);
        $httpBackend.whenGET(HostnameAndPort.forNgResource() + '/scenarioo/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        $scope = $rootScope.$new();
        ConfigCtrl = $controller('ConfigEditorCtrl', {$scope: $scope, BranchesResource: BranchesResource, Config: Config});
    }));

    describe('when page is loaded', function () {
        it('loads and displays the config from the server', function () {
            expect(ConfigCtrl).toBeDefined();
            expect($scope.configuration).toBeUndefined();

            $httpBackend.flush();

            expect($scope.configuration).toEqualData(DUMMY_CONFIG_RESPONSE);
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

            expect($scope.configuration.defaultBuildName).toEqual(DUMMY_CONFIG_RESPONSE.defaultBuildName);
            expect($scope.configuration.defaultBranchName).toEqual(DUMMY_CONFIG_RESPONSE.defaultBranchName);
            expect($scope.configuration.scenarioPropertiesInOverview).toEqual(DUMMY_CONFIG_RESPONSE.scenarioPropertiesInOverview);
            expect($scope.configuration.applicationInformation).toEqual(DUMMY_CONFIG_RESPONSE.applicationInformation);
            expect($scope.configuration.testDocumentationDirPath).toEqual(DUMMY_CONFIG_RESPONSE.testDocumentationDirPath);
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