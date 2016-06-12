'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var NUMBER_OF_AVAILABLE_COMPARISON = 6;

useCase('Navigation')
	.description('Select Build and Comparison from navigation bar')
	.describe(function () {

		var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
		var scenarioPage = new pages.scenarioPage();
		var stepPage = new pages.stepPage();

		beforeEach(function () {
			new pages.homePage().initLocalStorage();
		});

		afterEach(function () {
			// Reset Selection
			homePage.chooseBuild('last successful:');
			homePage.chooseBranch('wikipedia-docu-example');
		});

		scenario('6 comparison should be selectable')
			.description('Selects a build from wikipedia-docu-example-dev and checks available')
			.it(function () {
				homePage.goToPage();
				homePage.chooseBranch('wikipedia-docu-example-dev');
				step('wikipedia-docu-example-dev branch selected');
				homePage.chooseBuild('last successful');
				step('last successful build selected');
				step('6 Available Comparisons');

				homePage.assertSelectedComparison('Disabled (Available ' + NUMBER_OF_AVAILABLE_COMPARISON + ')');
			});

		scenario('select comparison')
			.description('Selects a build from wikipedia-example-dev and selects to last successful (dev) as comparison')
			.it(function () {
				homePage.goToPage();
				homePage.chooseBranch('wikipedia-docu-example-dev');
				step('wikipedia-docu-example-dev branch selected');
				homePage.chooseBuild('last successful:');
				step('last successful build selected');
				homePage.chooseComparison('To last successful (dev)');
				step('last successful (dev) comparison selected');

				homePage.assertSelectedComparison('last successful');
			});

		scenario('disable comparison')
			.description('Disables the diff viewer feature by selecting "Disable" in the comparison menu')
			.it(function () {
				homePage.goToPage();
				homePage.chooseBranch('wikipedia-docu-example-dev');
				step('wikipedia-docu-example-dev branch selected');
				homePage.chooseBuild('last successful:');
				step('last successful build selected');
				homePage.chooseComparison('To last successful (dev)');
				step('last successful (dev) comparison selected');
                homePage.chooseComparison('Disable');
                homePage.assertNoDiffInfoDisplayed();
                step('comparison Disabled');
                homePage.selectUseCase(1);
				usecasePage.assertNoDiffInfoDisplayed();
                step('Check for diff elements in list of scenarios');
				usecasePage.selectScenario(1);
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
				homePage.chooseBranch('wikipedia-docu-example');
				step('wikipedia-docu-example branch selected');
				homePage.chooseBuild('Revision 1290FE2');
				step('Revision 1290FE2 build selected');

				homePage.assertComparisonMenuNotShown();
			});

	});
