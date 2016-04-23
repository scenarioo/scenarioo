'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Create sketch')
    .description('Create a sketch based on the screenshot of a step.')
    .describe(function () {

        var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();
        var stepPage = new pages.stepPage();
        var editorPage = new pages.editorPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('New issue success')
            .description('Create a new issue successfully')
            .it(function () {

                var sketchName = 'Created by automated test';

                homePage.goToPage();

                step('Select a use case from the list');
                homePage.selectUseCase(1);

                step('Select a scenario from the list');
                usecasePage.selectScenario(1);

                step('Select a step from the scenario');
                scenarioPage.openStepByName('Step 1: Wikipedia Suche');

                step('Click "Create Sketch" button');
                stepPage.clickCreateSketchButton();

                // Drawing in the sketching editor is omitted here.
                // If somebody wants to try to implement this, go ahead please :-)

                step('Enter information about the step');
                editorPage.enterSketchInformation(sketchName);
                editorPage.assertSaveSketchSuccessfulMessageIsNotPresent();

                step('Save issue');
                editorPage.clickSaveButton();
                editorPage.assertSaveSketchSuccessfulMessageIsDisplayed();

                homePage.goToPage();
                homePage.selectSketchesTab();
                homePage.assertSketchesListContainsEntryWithSketchName(sketchName);
            });

        scenario('New issue fail')
            .description('Fail to create an issue because insufficient information was entered')
            .it(function () {
                stepPage.goToPage('/step/Find%20Page/find_no_results/startSearch.jsp/0/0');

                step('click "Create Sketch" button');
                stepPage.clickCreateSketchButton();
                editorPage.assertPageIsDisplayed();

                step('Sketch can not be saved. Save button is inactive because not all required fields are filled in.');
                editorPage.assertSaveButtonIsDisabled();
            });

        scenario('Remember author')
            .description('The author is remembered and automatically filled in for future issues')
            .it(function () {

                stepPage.goToPage('/step/Donate/find_donate_page/donate.jsp/0/0');

                step('Click "Create Sketch" button');
                stepPage.clickCreateSketchButton();

                step('Enter information about the step');
                editorPage.assertAuthorFieldIsEmpty();
                editorPage.enterSketchInformation('Also created by automated test');

                step('Save issue');
                editorPage.clickSaveButton();

                stepPage.goToPage('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');

                step('Go to a different step and create a sketch');
                stepPage.clickCreateSketchButton();

                step('Author information is now already filled in');
                editorPage.assertAuthorFieldIsFilledAlready();
            });

    });
