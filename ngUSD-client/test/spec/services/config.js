'use strict';

describe('Service :: config', function () {

    var DUMMY_CONFIG_RESPONSE = {'testDocumentationDirPath': 'webtestDocuContentExample', 'defaultBuildName': 'current', 'scenarioPropertiesInOverview': 'userProfile, configuration', 'applicationInformation': 'This is my personal copy of ngUSD :-)', 'buildstates': {'failed': 'label-important', 'success': 'label-success', 'warning': 'label-warning'}, 'defaultBranchName': 'trunk'};

    beforeEach(module('ngUSDClientApp.services'));

    it('should inject Config', inject(function (Config) {
        expect(Config).not.toBeUndefined();
    }));

    it('should be able to load config from server', inject(function (Config, $rootScope, $httpBackend) {
        spyOn($rootScope, '$broadcast');
        $httpBackend.when('GET', '/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);

        Config.load();

        $httpBackend.flush();

        expect($rootScope.$broadcast).toHaveBeenCalledWith('configLoaded');

        expect(Config.selectedBuild()).not.toBeUndefined();
        expect(Config.selectedBranch()).not.toBeUndefined();
        expect(Config.scenarioPropertiesInOverview()).not.toBeUndefined();
        expect(Config.applicationInformation()).not.toBeUndefined();
    }));

});
