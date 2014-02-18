'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Home', function () {

    scenarioo.describeScenario('Navigate to the Home Page', function () {
        var homePage = new pages.homePage();
        browser.get('#/');
        scenarioo.docuWriter.saveStep('display the homePage');
        homePage.assertPageIsDisplayed();
    });

});