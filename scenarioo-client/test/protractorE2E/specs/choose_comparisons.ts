'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import NavigationPage from '../webPages/navigationPage';
import HomePage from '../webPages/homePage';
import UsecasePage from '../webPages/usecasePage';
import ScenarioPage from '../webPages/scenarioPage';
import StepPage from '../webPages/stepPage';

const NUMBER_OF_AVAILABLE_COMPARISON = 6;
const BRANCH_WIKI = 'Production';
const BRANCH_WIKI_DEV = 'Development';
const BUILD_LAST_SUCCESSFUL = 'last successful';
const BUILD_JANUARY = '2014-01-20';
const SECOND_USE_CASE = 1;
const SECOND_SCENARIO = 1;

useCase('Diff viewer - Choose comparisons')
    .labels(['diff-viewer'])
    .description('Select Build and Comparison from navigation bar')
	.describe(function () {

		beforeEach(async function () {
            await Utils.startScenariooRevisited();
		});

		afterEach(async function () {
			// Reset Selection
            await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
            await NavigationPage.chooseBranch(BRANCH_WIKI);
		});

		scenario('Check selectable comparisons')
			.description('Selects a build from wikipedia-docu-example-dev and checks if six comparisons are available')
			.it(async function () {
                await Utils.navigateToRoute('/');
                await NavigationPage.chooseBranch(BRANCH_WIKI_DEV);
				step('wikipedia-docu-example-dev branch selected');
                await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL);
                step('last successful build selected');
                await NavigationPage.disableComparison();
                await NavigationPage.assertSelectedComparison('Disabled (Available ' + NUMBER_OF_AVAILABLE_COMPARISON + ')');
			});

		scenario('select comparison')
			.description('Selects a build from wikipedia-example-dev and selects to last successful (dev) as comparison')
			.it(async function () {
                await Utils.navigateToRoute('/');
                await NavigationPage.chooseBranch(BRANCH_WIKI_DEV);
				step('wikipedia-docu-example-dev branch selected');
                await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
				step('last successful build selected');
                await NavigationPage.chooseComparison('To last successful (dev)');
				step('last successful (dev) comparison selected');
                await NavigationPage.assertSelectedComparison('last successful');
                await NavigationPage.disableComparison();
			});

		scenario('disable comparison')
			.description('Disables the diff viewer feature by selecting "Disable" in the comparison menu')
			.it(async function () {
                await Utils.navigateToRoute('/');
                await NavigationPage.chooseBranch(BRANCH_WIKI_DEV);
				step('wikipedia-docu-example-dev branch selected');

                await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
				step('last successful build selected');

                await NavigationPage.chooseComparison('To last successful (dev)');
				step('last successful (dev) comparison selected');

                await NavigationPage.disableComparison();
                await HomePage.assertNoDiffInfoDisplayed();
                step('comparison Disabled');

                await HomePage.selectUseCase(SECOND_USE_CASE);
                await UsecasePage.assertNoDiffInfoDisplayed();
                step('Check for diff elements in list of scenarios');

                await UsecasePage.selectScenario(SECOND_SCENARIO);
                await ScenarioPage.assertNoDiffInfoDisplayed();
				step('Check for diff elements in scenario');

                await ScenarioPage.openStepByName('Step 1: Wikipedia Suche');
                await StepPage.assertNoDiffInfoDisplayed();
                step('Check for diff elements in step');
			});

		scenario('hide comparison menu')
			.description('if no comparison is available the comparison menu should be hidden')
			.it(async function () {
                await Utils.navigateToRoute('/');
                await NavigationPage.chooseBranch(BRANCH_WIKI);
				step('wikipedia-docu-example branch selected');
                await NavigationPage.chooseBuild(BUILD_JANUARY);
				step('January build selected');

                await HomePage.assertComparisonMenuNotShown();
			});

	});
