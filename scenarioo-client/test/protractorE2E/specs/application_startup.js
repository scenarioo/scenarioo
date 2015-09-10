'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Application startup', 'The user is presented with the about dialog on his first Scenarioo visit.', function () {

    var homePage = new pages.homePage();

    scenarioo.describeScenario('First visit', 'About dialog open on first access to Scenarioo to welcome new user.', function () {
        homePage.startScenariooFirstTimeVisit();
        scenarioo.docuWriter.saveStep('About dialog is displayed on first access of Scenarioo');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogShown();
        homePage.closeScenariooInfoDialogIfOpen();
        homePage.assertScenariooInfoDialogNotShown();
        scenarioo.docuWriter.saveStep('About dialog is closed');
    });

    scenarioo.describeScenario('Later visits', 'About dialog not open when previously visited.', function () {
        homePage.startScenariooRevisited();
        scenarioo.docuWriter.saveStep('About dialog not visible for previous visitors');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogNotShown();
    });

});
