'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import HomePage from '../pages/homePage';
import UsecasePage from '../pages/usecasePage';
import ScenarioPage from '../pages/scenarioPage';
import StepPage from '../pages/stepPage';
import EditorPage from '../pages/editorPage';

useCase('Create sketch')
    .description('Create a sketch based on the screenshot of a step.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('New issue success')
            .description('Create a new issue successfully')
            .it(async () => {
                const sketchName = 'automated test sketch 1';

                await HomePage.goToPage();

                await step('Select a use case from the list');
                await HomePage.selectUseCase(1);

                await step('Select a scenario from the list');
                await UsecasePage.selectScenario(1);

                await step('Select a step from the scenario');
                await ScenarioPage.openStepByName('Step 1: Wikipedia Suche');

                await step('Click "Create Sketch" button');
                await StepPage.clickCreateSketchButton();

                // Drawing in the sketching editor is omitted here.
                // If somebody wants to try to implement this, go ahead please :-)

                await step('Enter information about the step');
                await EditorPage.enterSketchInformation(sketchName, 'protractor e2e');
                await EditorPage.assertSaveSketchSuccessfulMessageIsNotPresent();

                await step('Save issue');
                await EditorPage.clickSaveButton();
                await EditorPage.assertSaveSketchSuccessfulMessageIsDisplayed();

                await HomePage.goToPage();
                await HomePage.selectSketchesTab();
                return HomePage.assertSketchesListContainsEntryWithSketchName(sketchName);
            });

        scenario('New issue fail')
            .description('Fail to create an issue because insufficient information was entered')
            .it(async () => {
                await Utils.navigateToRoute('/step/Find%20Page/find_no_results/startSearch.jsp/0/0');

                await step('click "Create Sketch" button');
                await StepPage.clickCreateSketchButton();
                await EditorPage.assertPageIsDisplayed();

                await step('Sketch can not be saved. Save button is inactive because not all required fields are filled in.');
                return EditorPage.assertSaveButtonIsDisabled();
            });

        scenario('Remember author')
            .description('The author is remembered and automatically filled in for future issues')
            .it(async () => {

                await Utils.navigateToRoute('/step/Donate/find_donate_page/donate.jsp/0/0');

                await step('Click "Create Sketch" button');
                await StepPage.clickCreateSketchButton();

                await step('Enter required sketch information');
                await EditorPage.assertAuthorFieldIsEmpty();
                await EditorPage.enterSketchInformation('automated test sketch 2', 'protractor e2e');

                await step('Save sketch');
                await EditorPage.clickSaveButton();
                await EditorPage.assertSaveSketchSuccessfulMessageIsDisplayed();

                await Utils.navigateToRoute('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');

                await step('Go to a different step and create a sketch');
                await StepPage.clickCreateSketchButton();

                await step('Author information is now already filled in');
                return EditorPage.assertAuthorFieldIsSetTo('protractor e2e');
            });

    });
