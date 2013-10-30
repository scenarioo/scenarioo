'use strict';

describe('Service :: config', function () {

    var BUILD_STATE_FAILED = 'failed',
        BUILD_STATE_SUCCESS = 'success',
        BUILD_STATE_WARNING = 'warning',
        DUMMY_CONFIG_RESPONSE = {
        'testDocumentationDirPath': 'webtestDocuContentExample',
        'defaultBuildName': 'current',
        'scenarioPropertiesInOverview': 'userProfile, configuration',
        'applicationInformation': 'This is my personal copy of ngUSD :-)',
        'buildstates': {
            BUILD_STATE_FAILED: 'label-important',
            BUILD_STATE_SUCCESS: 'label-success',
            BUILD_STATE_WARNING: 'label-warning'
        },
        'defaultBranchName': 'trunk'
    };

    beforeEach(angular.mock.module('ngUSDClientApp.services'));

    it('should inject Config', inject(function (Config) {
        expect(Config).not.toBeUndefined();
    }));

    it('has undefined branch and build cookies by default', inject(function(Config, $cookieStore) {
        expect($cookieStore.get(Config.BRANCH_URL_PARAMETER)).toBeUndefined();
        expect($cookieStore.get(Config.BUILD_URL_PARAMETER)).toBeUndefined();
    }));

    it('should be able to load config from server', inject(function (CONFIG_LOADED_EVENT, Config, $rootScope, $httpBackend, $location, $cookieStore) {
        spyOn($rootScope, '$broadcast').andCallThrough();

        loadConfigFromService(Config, $httpBackend);

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);

        expect(Config.selectedBranch()).toBe(DUMMY_CONFIG_RESPONSE.defaultBranchName);
        expect(Config.selectedBuild()).toBe(DUMMY_CONFIG_RESPONSE.defaultBuildName);
        expect(Config.scenarioPropertiesInOverview()).toBeDefined();
        expect(Config.applicationInformation()).toBe(DUMMY_CONFIG_RESPONSE.applicationInformation);
        expect($cookieStore.get(Config.BRANCH_URL_PARAMETER)).toBe(DUMMY_CONFIG_RESPONSE.defaultBranchName);
        expect($cookieStore.get(Config.BUILD_URL_PARAMETER)).toBe(DUMMY_CONFIG_RESPONSE.defaultBuildName);
    }));

    it('should be able to load config from server, with branch and build from URL', inject(function (CONFIG_LOADED_EVENT, Config, $rootScope, $httpBackend, $location, $cookieStore) {
        var BRANCH = 'branch_bugfix',
            BUILD = '2013-08-12_07:43';

        $location.url('/new/path/?branch=' + BRANCH + '&build=' + BUILD);

        spyOn($rootScope, '$broadcast').andCallThrough();

        loadConfigFromService(Config, $httpBackend);

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);

        expect(Config.selectedBranch()).toBe(BRANCH);
        expect(Config.selectedBuild()).toBe(BUILD);
        expect($cookieStore.get(Config.BRANCH_URL_PARAMETER)).toBe(BRANCH);
        expect($cookieStore.get(Config.BUILD_URL_PARAMETER)).toBe(BUILD);
    }));

    it('should react to $location changes', inject(function (CONFIG_LOADED_EVENT,Config, $rootScope, $httpBackend, $location, $cookieStore) {
        var BRANCH_CHANGED = 'branch_changed',
            BUILD_CHANGED = '2013-08-12_07:43_changed';

        spyOn($rootScope, '$broadcast').andCallThrough();

        $location.url('/new/path/?a=b');

        loadConfigFromService(Config, $httpBackend);

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);
        expect(Config.selectedBuild()).toBe(DUMMY_CONFIG_RESPONSE.defaultBuildName);
        expect(Config.selectedBranch()).toBe(DUMMY_CONFIG_RESPONSE.defaultBranchName);

        $location.url('/new/path/?build=' + BUILD_CHANGED + '&branch=' + BRANCH_CHANGED);
        $rootScope.$digest();

        expect(Config.selectedBuild()).toBe(BUILD_CHANGED);
        expect(Config.selectedBranch()).toBe(BRANCH_CHANGED);
        expect($cookieStore.get(Config.BRANCH_URL_PARAMETER)).toBe(BRANCH_CHANGED);
        expect($cookieStore.get(Config.BUILD_URL_PARAMETER)).toBe(BUILD_CHANGED);
    }));

    it('should be able to load config from server, with values from cookie', inject(function (CONFIG_LOADED_EVENT,Config, $rootScope, $httpBackend, $cookieStore) {
        var BRANCH_COOKIE = 'branch_cookie',
            BUILD_COOKIE = '2013-08-12_07:43_cookie';

        $cookieStore.put('branch', BRANCH_COOKIE);
        $cookieStore.put('build', BUILD_COOKIE);

        spyOn($rootScope, '$broadcast').andCallThrough();

        loadConfigFromService(Config, $httpBackend);

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);

        expect(Config.selectedBranch()).toBe(BRANCH_COOKIE);
        expect(Config.selectedBuild()).toBe(BUILD_COOKIE);
        expect($cookieStore.get(Config.BRANCH_URL_PARAMETER)).toBe(BRANCH_COOKIE);
        expect($cookieStore.get(Config.BUILD_URL_PARAMETER)).toBe(BUILD_COOKIE);
    }));

    it('contains build state to css class mapping as a map', inject(function(Config, $httpBackend) {
        loadConfigFromService(Config, $httpBackend);

        var buildStateToClassMapping = Config.buildStateToClassMapping();

        expect(buildStateToClassMapping).toBeDefined();
        expect(getSize(buildStateToClassMapping)).toBe(3);
        expect(buildStateToClassMapping[BUILD_STATE_FAILED]).toBe(DUMMY_CONFIG_RESPONSE.buildstates[BUILD_STATE_FAILED]);
        expect(buildStateToClassMapping[BUILD_STATE_WARNING]).toBe(DUMMY_CONFIG_RESPONSE.buildstates[BUILD_STATE_WARNING]);
        expect(buildStateToClassMapping[BUILD_STATE_SUCCESS]).toBe(DUMMY_CONFIG_RESPONSE.buildstates[BUILD_STATE_SUCCESS]);
    }));

    it('contains additional columns for scenario overview', inject(function(Config, $httpBackend) {
        loadConfigFromService(Config, $httpBackend);

        var columns = Config.scenarioPropertiesInOverview();

        expect(columns).toBeDefined();
        expect(getSize(columns)).toBe(2);
        expect(columns[0]).toBe('userProfile');
        expect(columns[1]).toBe('configuration');
    }));

    function loadConfigFromService(Config, $httpBackend) {
        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);
        Config.load();
        $httpBackend.flush();
    }

    function getSize(object) {
        var size = 0, key;
        for (key in object) {
            if (object.hasOwnProperty(key)) size++;
        }
        return size;
    };

});
