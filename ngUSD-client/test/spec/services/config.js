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

    it('should be able to load config from server, with defaults', inject(function (Config, $rootScope, $httpBackend, $location) {

        // this is needed, that injection of $location in service and flushing of $httpBackend works as expected
        $rootScope.$apply();

        spyOn($rootScope, '$broadcast');

        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        Config.load();

        $httpBackend.flush();

        expect($rootScope.$broadcast).toHaveBeenCalledWith('configLoaded');

        expect(Config.selectedBuild()).toBeDefined();
        expect(Config.selectedBranch()).toBeDefined();
        expect(Config.selectedBuild()).toBe('current');
        expect(Config.selectedBranch()).toBe('trunk');

        expect(Config.scenarioPropertiesInOverview()).toBeDefined();
        expect(Config.applicationInformation()).toBeDefined();
    }));



    it('should be able to load config from server, with values from URL', inject(function (Config, $rootScope, $httpBackend,$location) {

        $location.url('/new/path/?build=2013-08&branch=someotherbranch');
        // this is needed, that injection of $location in service and flushing of $httpBackend works as expected
        $rootScope.$apply();

        spyOn($rootScope, '$broadcast');

        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        Config.load();

        $httpBackend.flush();

        expect($rootScope.$broadcast).toHaveBeenCalledWith('configLoaded');

        expect(Config.selectedBuild()).toBeDefined();
        expect(Config.selectedBranch()).toBeDefined();
        expect(Config.selectedBranch()).toBe('someotherbranch');
        expect(Config.selectedBuild()).toBe('2013-08');

        expect(Config.scenarioPropertiesInOverview()).toBeDefined();
        expect(Config.applicationInformation()).toBeDefined();
    }));

});
