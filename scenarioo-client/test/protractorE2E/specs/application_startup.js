'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

describeUseCaseE('Application startup', {
    description: 'The user is presented with the about dialog on his first Scenarioo visit.'
    }, function() {
    var homePage = new pages.homePage();

    describeScenarioE('First visit', {
        description: 'About dialog open on first access to Scenarioo to welcome new user.'
    }, function() {
        homePage.startScenariooFirstTimeVisit();
        scenarioo.saveStep('About dialog is displayed on first access of Scenarioo');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogShown();
        homePage.closeScenariooInfoDialogIfOpen();
        homePage.assertScenariooInfoDialogNotShown();
        scenarioo.saveStep('About dialog is closed');
    });

    describeScenarioE('Later visits', {
        description: 'About dialog not open when previously visited.'
    }, function() {
        homePage.startScenariooRevisited();
        scenarioo.saveStep('About dialog not visible for previous visitors');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogNotShown();
    });

});
