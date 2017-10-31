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
                var flow = protractor.promise.controlFlow();
                homePage.startScenariooFirstTimeVisit();
                flow.execute(function() {
                    console.log('Going to assert that page is displayed');
                });
                homePage.assertPageIsDisplayed();
                flow.execute(function() {
                    console.log('Going to record the first scenarioo step');
                });
                step('About dialog is displayed on first access of Scenarioo');
                flow.execute(function() {
                    console.log('Recorded the first scenarioo step');
                });
                homePage.assertScenariooInfoDialogShown();
                homePage.closeScenariooInfoDialogIfOpen();
                homePage.assertScenariooInfoDialogNotShown();
                step('About dialog is closed');
            });

        scenario('Later visits')
            .description('About dialog not open when previously visited.')
            .it(function () {
                step('Before startScenariooRevisited()');
                homePage.startScenariooRevisited();
                step('About dialog not visible for previous visitors');
                homePage.assertPageIsDisplayed();
                homePage.assertScenariooInfoDialogNotShown();
            });


    });
