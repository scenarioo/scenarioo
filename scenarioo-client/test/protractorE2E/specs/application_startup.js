'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Application Startup')
    .description('The user is presented with the about dialog on his first Scenarioo visit.')
    .describe(function () {

        var homePage = new pages.homePage();

        scenario('First visit')
            .description('About dialog open on first access to Scenarioo to welcome new user.')
            .it(function () {
                homePage.startScenariooFirstTimeVisit();
                step('About dialog is displayed on first access of Scenarioo');
                homePage.assertPageIsDisplayed();
                homePage.assertScenariooInfoDialogShown();
                homePage.closeScenariooInfoDialogIfOpen();
                homePage.assertScenariooInfoDialogNotShown();
                step('About dialog is closed');
            });

        scenario('Later visits')
            .description('About dialog not open when previously visited.')
            .it(function () {
                homePage.startScenariooRevisited();
                step('About dialog not visible for previous visitors');
                homePage.assertPageIsDisplayed();
                homePage.assertScenariooInfoDialogNotShown();
            });


    });
