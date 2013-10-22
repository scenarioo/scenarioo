'use strict';

describe('Service :: config', function () {

    var DUMMY_CONFIG_RESPONSE = {
        'testDocumentationDirPath': 'webtestDocuContentExample',
        'defaultBuildName': 'current',
        'scenarioPropertiesInOverview': 'userProfile, configuration',
        'applicationInformation': 'This is my personal copy of ngUSD :-)',
        'buildstates': {
            'failed': 'label-important',
            'success': 'label-success',
            'warning': 'label-warning'
        },
        'defaultBranchName': 'trunk'
    };

    beforeEach(angular.mock.module('ngUSDClientApp.services'));

    it('should inject Config', inject(function (Config) {
        expect(Config).not.toBeUndefined();
    }));

    it('should be able to load config from server', inject(function (CONFIG_LOADED_EVENT, Config, $rootScope, $httpBackend, $location) {
        spyOn($rootScope, '$broadcast').andCallThrough();

        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        Config.load();

        $httpBackend.flush();

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);

        expect(Config.selectedBuild()).toBe(DUMMY_CONFIG_RESPONSE.defaultBuildName);
        expect(Config.selectedBranch()).toBe(DUMMY_CONFIG_RESPONSE.defaultBranchName);
        expect(Config.scenarioPropertiesInOverview()).toBe(DUMMY_CONFIG_RESPONSE.scenarioPropertiesInOverview);
        expect(Config.applicationInformation()).toBe(DUMMY_CONFIG_RESPONSE.applicationInformation);
    }));

    it('should be able to load config from server, with values from URL', inject(function (CONFIG_LOADED_EVENT, Config, $rootScope, $httpBackend, $location) {
        $location.url('/new/path/?build=2013-08&branch=someotherbranch');

        spyOn($rootScope, '$broadcast').andCallThrough();

        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        Config.load();

        $httpBackend.flush();

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);

        expect(Config.selectedBuild()).toBe('2013-08');
        expect(Config.selectedBranch()).toBe('someotherbranch');
    }));

    it('should react to $location changes', inject(function (CONFIG_LOADED_EVENT,Config, $rootScope, $httpBackend, $location) {
        spyOn($rootScope, '$broadcast').andCallThrough();

        $location.url('/new/path/?a=b');

        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        Config.load();

        $httpBackend.flush();

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);
        expect(Config.selectedBuild()).toBe(DUMMY_CONFIG_RESPONSE.defaultBuildName);
        expect(Config.selectedBranch()).toBe(DUMMY_CONFIG_RESPONSE.defaultBranchName);

        $location.url('/new/path/?build=2013-08_changed&branch=someotherbranch_changed');
        $rootScope.$digest();


        expect(Config.selectedBuild()).toBe('2013-08_changed');
        expect(Config.selectedBranch()).toBe('someotherbranch_changed');
    }));

    it('should be able to load config from server, with values from cookie', inject(function (CONFIG_LOADED_EVENT,Config, $rootScope, $httpBackend, $cookieStore) {
        $cookieStore.put('branch', 'cookieBranch');
        $cookieStore.put('build', '2013-08-cookie');

        spyOn($rootScope, '$broadcast').andCallThrough();

        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        Config.load();

        $httpBackend.flush();

        expect($rootScope.$broadcast).toHaveBeenCalledWith(CONFIG_LOADED_EVENT);

        expect(Config.selectedBuild()).toBe('2013-08-cookie');
        expect(Config.selectedBranch()).toBe('cookieBranch');
    }));

});
