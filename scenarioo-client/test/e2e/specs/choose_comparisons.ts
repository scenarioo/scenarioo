'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import NavigationPage from '../pages/navigationPage';
import HomePage from '../pages/homePage';
import UsecasePage from '../pages/usecasePage';
import ScenarioPage from '../pages/scenarioPage';
import StepPage from '../pages/stepPage';

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
	.describe(() => {

		beforeEach(async () => {
            await Utils.startScenariooRevisited();
		});

		afterEach(async () => {
			// Reset Selection
            await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
            await NavigationPage.chooseBranch(BRANCH_WIKI);
		});

		scenario('Check selectable comparisons')
			.description('Selects a build from wikipedia-docu-example-dev and checks if six comparisons are available')
			.it(async () => {
                await HomePage.goToPage();
                await NavigationPage.chooseBranch(BRANCH_WIKI_DEV);
				await step('wikipedia-docu-example-dev branch selected');
                await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL);
                await step('last successful build selected');
                await NavigationPage.disableComparison();
                await NavigationPage.assertSelectedComparison('Disabled (Available ' + NUMBER_OF_AVAILABLE_COMPARISON + ')');
			});

		scenario('select comparison')
			.description('Selects a build from wikipedia-example-dev and selects to last successful (dev) as comparison')
			.it(async () => {
                await HomePage.goToPage();
                await NavigationPage.chooseBranch(BRANCH_WIKI_DEV);
				await step('wikipedia-docu-example-dev branch selected');
                await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
				await step('last successful build selected');
                await NavigationPage.chooseComparison('To last successful (dev)');
				await step('last successful (dev) comparison selected');
                await NavigationPage.assertSelectedComparison('last successful');
                await NavigationPage.disableComparison();
			});

		scenario('disable comparison')
			.description('Disables the diff viewer feature by selecting "Disable" in the comparison menu')
			.it(async () => {
                await HomePage.goToPage();
                await NavigationPage.chooseBranch(BRANCH_WIKI_DEV);
				await step('wikipedia-docu-example-dev branch selected');

                await NavigationPage.chooseBuild(BUILD_LAST_SUCCESSFUL + ':');
				await step('last successful build selected');

                await NavigationPage.chooseComparison('To last successful (dev)');
				await step('last successful (dev) comparison selected');

                await NavigationPage.disableComparison();
                await HomePage.assertNoDiffInfoDisplayed();
                await step('comparison Disabled');

                await HomePage.selectUseCase(SECOND_USE_CASE);
                await UsecasePage.assertNoDiffInfoDisplayed();
                await step('Check for diff elements in list of scenarios');

                await UsecasePage.selectScenario(SECOND_SCENARIO);
                await ScenarioPage.assertNoDiffInfoDisplayed();
				await step('Check for diff elements in scenario');

                await ScenarioPage.openStepByName('Step 1: Wikipedia Suche');
                await StepPage.assertNoDiffInfoDisplayed();
                await step('Check for diff elements in step');
			});

		scenario('hide comparison menu')
			.description('if no comparison is available the comparison menu should be hidden')
			.it(async () => {
                await HomePage.goToPage();
                await NavigationPage.chooseBranch(BRANCH_WIKI);
				await step('wikipedia-docu-example branch selected');
                await NavigationPage.chooseBuild(BUILD_JANUARY);
				await step('January build selected');

                await HomePage.assertComparisonMenuNotShown();
			});

	});
