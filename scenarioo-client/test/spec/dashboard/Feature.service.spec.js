'use strict';

describe('FeatureService', function () {

    var service;

    beforeEach(module('scenarioo.services'));


    var FeaturesResourceFake = {
        query: function (object, functionCallback) {
            functionCallback([
                {
                    name: 'feature1',
                    id: 'feature1',
                    features: []
                }
            ]);
        }
    };

    beforeEach(function () {
        module(function ($provide) {
            $provide.value('FeaturesResource', FeaturesResourceFake);
        });
    });

    beforeEach(inject(function (FeatureService) {
            service = FeatureService;
        }
    ));

    it('test beginning', function () {
        var rootFeature = service.getRootFeature();
        expect(rootFeature.name).toBe('Home');
        expect(rootFeature.features.length).toBe(1);
    });

    /*
     it('has no features and builds set in the beginning', function () {
     expect(dashboardController.features.length).toBe(0);
     expect(dashboardController.branchesAndBuilds.length).toBe(0);
     });

     it('navigates to feature when link is clicked', function () {
     expect($location.path()).toBe('');

     var dummyFeature = { name: 'DisplayWeather', diffInfo: {isRemoved: false} };
     dashboardController.clickFeature(dummyFeature, '/dashboard');

     expect($location.path()).toBe('/dashboard/DisplayWeather');
     });

     it('opens treenavigation and navigates to root feature', function(){
     expect($location.path()).toBe('/dashboard/DisplayWeather');
     element(by.id('pageslideButton')).click();
     element(by.id('root')).click();
     expect($location.path()).toBe('');
     });

     */
});
