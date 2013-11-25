'use strict';

describe('Controller :: Scenario', function() {

    var scope, configFake, scenarioController, $httpBackend;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function($rootScope, $controller, _$httpBackend_) {
        scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;

        scenarioController = $controller('ScenarioCtrl', {$scope: scope, Config: configFake});
    }));

    // TODO implement tests
});