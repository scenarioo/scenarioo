/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

describe('Controller :: Scenario', function() {

    var $scope, configFake, scenarioController, $httpBackend, $routeParams, Config, TestData, HostNameAndPort;

    beforeEach(module('scenarioo.controllers'));

    beforeEach(inject(function($rootScope, $controller, _$httpBackend_, _$routeParams_, _Config_, _TestData_, _HostnameAndPort_, localStorageService) {
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        $routeParams = _$routeParams_;
        Config = _Config_;
        TestData = _TestData_;
        HostNameAndPort = _HostnameAndPort_;

        $routeParams.useCaseName = 'SearchUseCase';
        $routeParams.scenarioName = 'NotFoundScenario';

        localStorageService.clearAll();

        scenarioController = $controller('ScenarioCtrl', {$scope: $scope, Config: configFake});
    }));

    it('clears search field when resetSearchField() is called', function() {
        $scope.searchFieldText = 'test';
        $scope.resetSearchField();
        expect($scope.searchFieldText).toBe('');
    });

    it('creates the correct link to a step', function() {
        var link = $scope.getLinkToStep('searchPage.html', 2, 0);
        expect(link).toBe('#/step/SearchUseCase/NotFoundScenario/searchPage.html/2/0');
    });

    it('creates empty image link, if branch and build selection is unknown', function() {
        var imageLink = $scope.getScreenShotUrl('img.jpg');
        expect(imageLink).toBeUndefined();
    });

    it('creates the correct image link, if selected branch and build is known', function() {
        $httpBackend.whenGET(HostNameAndPort.forLink() + 'rest/configuration').respond(TestData.CONFIG);
        $httpBackend.whenGET(HostNameAndPort.forLink() + 'rest/branches/trunk/builds/current/usecases/SearchUseCase/scenarios/NotFoundScenario').respond(TestData.SCENARIO);

        Config.load();
        $httpBackend.flush();

        var imageLink = $scope.getScreenShotUrl('img.jpg');
        expect(imageLink).toBe(HostNameAndPort.forLink() + 'rest/branches/trunk/builds/current/usecases/SearchUseCase/scenarios/NotFoundScenario/image/img.jpg');
    });

    it('does not show all steps of a page by default', function() {
        expect($scope.showAllStepsForPage(0)).toBeFalsy();
        expect($scope.showAllStepsForPage(1)).toBeFalsy();
        expect($scope.showAllStepsForPage(2)).toBeFalsy();
    });

    it('can toggle the showPageForAllSteps property', function() {
        $scope.toggleShowAllStepsForPage(5);
        expect($scope.showAllStepsForPage(5)).toBeTruthy();
        $scope.toggleShowAllStepsForPage(5);
        expect($scope.showAllStepsForPage(5)).toBeFalsy();
    });

    // TODO implement the remaining tests

});