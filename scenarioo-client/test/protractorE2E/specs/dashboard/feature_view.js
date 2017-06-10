'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../../webPages');

useCase('dashboard_feature_view')
    .description('The user is presented with feature view.')
    .describe(function () {

        var featurePage = new pages.featureView();

        scenario('First visit')
            .description('Test Visit first time')
            .it(function () {
                featurePage.startScenariooFirstTimeVisit();
                featurePage.goToPage();
                step('Show Features of root Feature');
            });


        scenario('Navigation')
            .description('Test Visit first time')
            .it(function () {
                featurePage.goToPage();
                featurePage.docuViewTab.click();
                step('Show Documentation of root Feature and subs');
                featurePage.mapViewTab.click();
                step('Show Feature-Map View of root Feature and subs');
                featurePage.scenariosViewTab.click();
                step('Show Scenarios View of root Feature');
                featurePage.featureViewTab.click();
                step('Back to Feature View');
            });
    });
