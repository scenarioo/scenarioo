'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var NUMBER_OF_AVAILABLE_COMPARISON = 6;
var BRANCH_WIKI = 'Production';
var BRANCH_WIKI_DEV = 'Development';
var BUILD_LAST_SUCCESSFUL = 'last successful';
var BUILD_JANUARY = '2014-01-20';
var SECOND_USE_CASE = 1;
var SECOND_SCENARIO = 1;

useCase('Diff viewer - Choose comparisons')
    .labels(['diff-viewer'])
    .description('Select Build and Comparison from navigation bar')
	.describe(function () {

		var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
		var scenarioPage = new pages.scenarioPage();
		var stepPage = new pages.stepPage();
        var navigationPage = new pages.navigationPage();

		beforeEach(function () {
			new pages.homePage().initLocalStorage();
		});

		afterEach(function () {
			// Reset Selection
            navigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
            navigationPage.chooseBranch(BRANCH_WIKI);
		});

		scenario('Check selectable comparisons')
			.description('Selects a build from wikipedia-docu-example-dev and checks if six comparisons are available')
			.it(function () {
                homePage.goToPage();
                navigationPage.chooseBranch(BRANCH_WIKI_DEV);
				step('wikipedia-docu-example-dev branch selected');
                navigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL);
                step('last successful build selected');
                navigationPage.disableComparison();
                navigationPage.assertSelectedComparison('Disabled (Available ' + NUMBER_OF_AVAILABLE_COMPARISON + ')');
			});

		scenario('select comparison')
			.description('Selects a build from wikipedia-example-dev and selects to last successful (dev) as comparison')
			.it(function () {
				homePage.goToPage();
                navigationPage.chooseBranch(BRANCH_WIKI_DEV);
				step('wikipedia-docu-example-dev branch selected');
                navigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
				step('last successful build selected');
                navigationPage.chooseComparison('To last successful (dev)');
				step('last successful (dev) comparison selected');
                navigationPage.assertSelectedComparison('last successful');
                navigationPage.disableComparison();
			});

		scenario('disable comparison')
			.description('Disables the diff viewer feature by selecting "Disable" in the comparison menu')
			.it(function () {
				homePage.goToPage();
                navigationPage.chooseBranch(BRANCH_WIKI_DEV);
				step('wikipedia-docu-example-dev branch selected');

                navigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
				step('last successful build selected');

                navigationPage.chooseComparison('To last successful (dev)');
				step('last successful (dev) comparison selected');

                navigationPage.disableComparison();
                homePage.assertNoDiffInfoDisplayed();
                step('comparison Disabled');

                homePage.selectUseCase(SECOND_USE_CASE);
				usecasePage.assertNoDiffInfoDisplayed();
                step('Check for diff elements in list of scenarios');

				usecasePage.selectScenario(SECOND_SCENARIO);
				scenarioPage.assertNoDiffInfoDisplayed();
				step('Check for diff elements in scenario');

				scenarioPage.openStepByName('Step 1: Wikipedia Suche');
				stepPage.assertNoDiffInfoDisplayed();
                step('Check for diff elements in step');
			});

		scenario('hide comparison menu')
			.description('if no comparison is available the comparison menu should be hidden')
			.it(function () {
				homePage.goToPage();
                navigationPage.chooseBranch(BRANCH_WIKI);
				step('wikipedia-docu-example branch selected');
                navigationPage.chooseBuild(BUILD_JANUARY);
				step('January build selected');

				homePage.assertComparisonMenuNotShown();
			});

	});
