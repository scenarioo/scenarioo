'use strict';

describe('Controller :: Config', function () {

    var $rootScope, $controller, BranchService, Config, $httpBackend, $scope, ConfigCtrl;

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

    beforeEach(module('ngUSDClientApp.controllers'));

    beforeEach(inject(function (_$rootScope_, _$controller_, _BranchService_, _Config_, _$httpBackend_) {
        $rootScope = _$rootScope_;
        $controller = _$controller_;
        BranchService = _BranchService_;
        Config = _Config_;
        $httpBackend = _$httpBackend_;

        $httpBackend.whenGET('http://localhost:8080/ngusd/rest/branches').respond(DUMMY_BRANCHES_RESPONSE);
        $httpBackend.whenGET('http://localhost:8080/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        $scope = $rootScope.$new();
        ConfigCtrl = $controller('ConfigCtrl', {$scope: $scope, BranchService: BranchService, Config: Config});
        $httpBackend.flush();
    }));

    describe('when page is loaded', function () {
        it('loads and displays the config from the server', function () {

            expect(ConfigCtrl).toBeDefined();
            expect($scope.configuration.defaultBuildName).toBeUndefined();
            expect($scope.configuration.defaultBranchName).toBeUndefined();
            expect($scope.configuration.scenarioPropertiesInOverview).toBeUndefined();
            expect($scope.configuration.applicationInformation).toBeUndefined();


            loadConfig();

            expect($scope.configuration.defaultBuildName).toEqual(DUMMY_CONFIG_RESPONSE.defaultBuildName);
            expect($scope.configuration.defaultBranchName).toEqual(DUMMY_CONFIG_RESPONSE.defaultBranchName);
            expect($scope.configuration.scenarioPropertiesInOverview).toEqual(DUMMY_CONFIG_RESPONSE.scenarioPropertiesInOverview);
            expect($scope.configuration.applicationInformation).toEqual(DUMMY_CONFIG_RESPONSE.applicationInformation);
        });

        it('loads all branches and builds', function () {
            expect($scope.configuredBranch).toBeUndefined();

            loadConfig();

            expect($scope.configurableBranches).toBeDefined();
            expect($scope.configuredBranch).toBeDefined();
        })
    });

    describe('when reset button is clicked', function () {
        it('resets the config to the loaded values', function () {
            loadConfig();

            changeAllValues();

            $scope.resetConfiguration();

            expect($scope.configuration.defaultBuildName).toEqual(DUMMY_CONFIG_RESPONSE.defaultBuildName);
            expect($scope.configuration.defaultBranchName).toEqual(DUMMY_CONFIG_RESPONSE.defaultBranchName);
            expect($scope.configuration.scenarioPropertiesInOverview).toEqual(DUMMY_CONFIG_RESPONSE.scenarioPropertiesInOverview);
            expect($scope.configuration.applicationInformation).toEqual(DUMMY_CONFIG_RESPONSE.applicationInformation);
        });
    });

    describe('when the save button is clicked', function () {
        it('saves the edited config', function () {
            spyOn(Config, 'updateConfiguration');

            loadConfig();

            changeAllValues();

            $scope.updateConfiguration();

            expect(Config.updateConfiguration).toHaveBeenCalled();
        });
    });

    function loadConfig() {
        Config.load();
        $httpBackend.flush();
    }

    function changeAllValues() {
        $scope.configuration.defaultBuildName = 'new build';
        $scope.configuration.defaultBranchName = 'new branch';
        $scope.configuration.scenarioPropertiesInOverview = 'abc';
        $scope.configuration.applicationInformation = 'new information';
    }

});