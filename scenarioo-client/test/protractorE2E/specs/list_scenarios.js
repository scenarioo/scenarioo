'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');
var NUMBER_OF_FEATURES = 4;
var NUMBER_OF_SCENARIOS = 4;
var COMPARISON_PROJECTSTART = 'To Projectstart';
var COMPARISON_DISABLE = 'Disable';
var SECOND_FEATURE = 1;
var SCENARIO_WITH_HIGHEST_DIFF = 'Find page title unique directly';

useCase('List scenarios')
    .description('After clicking on a feature, the user is presented with a list of all scenarios in this feature.')
    .describe(function () {

        var homePage = new pages.homePage();
        var featurePage = new pages.featurePage();
        var scenarioPage = new pages.scenarioPage();
        var navigationPage = new pages.navigationPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Expand all, collapse all on scenario page')
            .it(function () {
                homePage.goToPage();
                step('select a feature from the feature list');
                homePage.assertPageIsDisplayed();
                homePage.assertFeaturesShown(NUMBER_OF_FEATURES);
                homePage.selectFeature(SECOND_FEATURE);
                step('select a scenario in the scenario list');
                featurePage.selectScenario(0);
                step('all pages are collapsed by default, "expand all" button is visible');
                scenarioPage.expectOnlyExpandAllButtonIsDisplayed();
                scenarioPage.toggleShowAllStepsOfPage(0);
                step('"expand all" button and "collapse all" button are both visible');
                scenarioPage.expectExpandAllAndCollapseAllButtonBothDisplayed();
                scenarioPage.toggleShowAllStepsOfPage(1);
                step('Only "collapse all" visible');
                scenarioPage.expectOnlyCollapseAllButtonIsDisplayed();
            });

        scenario('Display Diff-Information')
            .labels(['diff-viewer'])
            .it(function () {
                homePage.goToPage();
                step('display features on homepage');
                homePage.assertPageIsDisplayed();
                navigationPage.chooseComparison(COMPARISON_PROJECTSTART);
                step('To Projectstart comparison selected');
                homePage.selectFeature(SECOND_FEATURE);
                step('Use Case selected');

                featurePage.assertNumberOfDiffInfos(NUMBER_OF_SCENARIOS);
                navigationPage.disableComparison();
            });

        scenario('Sort by Diff-Information')
            .labels(['diff-viewer'])
            .it(function () {
                homePage.goToPage();
                step('display features on homepage');
                homePage.assertPageIsDisplayed();
                navigationPage.chooseComparison('To Projectstart');
                step('To Projectstart comparison selected');
                homePage.selectFeature(SECOND_FEATURE);
                step('Use Case selected');

                featurePage.sortByChanges();
                featurePage.assertLastFeature(SCENARIO_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted ascending');

                featurePage.sortByChanges();
                featurePage.assertFirstFeature(SCENARIO_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted descending');
                navigationPage.disableComparison();
            });
    });
