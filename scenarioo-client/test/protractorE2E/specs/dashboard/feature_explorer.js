'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../../webPages');

useCase('dashboard_feature_explorer')
    .description('The user can use the explorer to navigate the tree.')
    .describe(function () {

        var featureExplorer = new pages.featureExplorer();

        scenario('Reset')
            .description('Reset to first visit')
            .it(function () {
                featureExplorer.startScenariooFirstTimeVisit();
                featureExplorer.goToPage();
                step('Show Features of root Feature');
            });


        scenario('Expand-Collapse')
            .description('Expand tree Collapse Tree')
            .it(function () {
                featureExplorer.goToPage();
                featureExplorer.expandable.click();
                step('collapsed');
                featureExplorer.expandable.click();
                step('expanded');
            });

        scenario('Select Features in tree')
            .description('Select Features in tree')
            .it(function () {
                featureExplorer.goToPage();
                featureExplorer.clickFeatureByFeatureId('Donate');
                step('Donate Selected');
                featureExplorer.clickFeatureByFeatureId('Home');
                step('Back to home');
            });

        scenario('Navigation on Sub')
            .description('Test Visit first time')
            .it(function () {
                featureExplorer.goToPage();
                featureExplorer.docuViewTab.click();
                featureExplorer.clickFeatureByFeatureId('Donate');
                step('Show Documentation of root Feature and subs');
                featureExplorer.mapViewTab.click();
                step('Show Feature-Map View of root Feature and subs');
                featureExplorer.scenariosViewTab.click();
                step('Show Scenarios View of root Feature');
                featureExplorer.featureViewTab.click();
                step('Back to Feature View');
            });
    });
