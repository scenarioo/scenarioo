'use strict'

describe('Controller :: Scenario', function() {

    var scope, configFake, scenarioController, $httpBackend;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function($rootScope, $controller, _$httpBackend_) {
        scope = $rootScope.$new();
        configFake = createConfigFake();
        $httpBackend = _$httpBackend_;

        scenarioController = $controller('ScenarioCtrl', {$scope: scope, Config: configFake});
    }));

    //it('should know the config', inject(function(Config) {
    //    expect(Config).toBeDefined();
    //}));

});