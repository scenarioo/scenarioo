'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var NUMBER_OF_FEATURES = 4;
var COMPARISON_PROJECTSTART = 'To Projectstart';
var FEATURE_WITH_HIGHEST_DIFF = 'Donate';

useCase('List features')
    .description('As soon as a branch and a build are selected, a list of features is shown.')
    .describe(function () {

        var homePage = new pages.homePage();
        var navigationPage = new pages.navigationPage();

        beforeEach(function () {
            homePage.initLocalStorage();
        });

        scenario('Display and filter features')
            .it(function () {
                homePage.goToPage();
                homePage.assertFeaturesShown(NUMBER_OF_FEATURES);
                step('display features on homepage');
                //homePage.assertPageIsDisplayed();
                homePage.filterFeatures('notinlist');
                //homePage.assertFeaturesShown(0);
                step('filter applied: no features shown');
                homePage.filterFeatures('find page');
                //homePage.assertFeaturesShown(1);
                step('filter applied: one feature found');
                homePage.filterFeatures('user wants find page');
                //homePage.assertFeaturesShown(1);
                step('other filter applied: one feature found');
            });

        scenario('Show and hide metadata')
            .it(function () {
                homePage.goToPage();
                step('display the homePage, metadata shown');
                homePage.assertPageIsDisplayed();
                homePage.assertMetaDataShown();
                homePage.hideMetaData();
                homePage.assertMetaDataHidden();
                step('metadata hidden');
                homePage.showMetaData();
                homePage.assertMetaDataShown();
                step('metadata shown');
            });

        scenario('Display Diff-Information')
            .labels(['diff-viewer'])
            .it(function () {
                homePage.goToPage();
                step('display features on homepage');
                navigationPage.chooseComparison(COMPARISON_PROJECTSTART);
                homePage.assertPageIsDisplayed();
                step('To Projectstart comparison selected');

                homePage.assertNumberOfDiffInfos(NUMBER_OF_FEATURES);

                homePage.sortByChanges();
                //homePage.assertLastFeature(FEATURE_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted ascending');

                homePage.sortByChanges();
                //homePage.assertFirstFeature(FEATURE_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted descending');
                navigationPage.disableComparison();
            });
    });
