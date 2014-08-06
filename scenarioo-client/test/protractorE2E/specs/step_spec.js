'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Step', function () {

    scenarioo.describeScenario('Navigate back and forth through the scenario steps.', function () {
        var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();
        var stepPage = new pages.stepPage();

        var ROUTE_OF_FIRST_STEP = '/step/Find%20Page/find_page_no_result/startSearch.jsp/0/0';
        var ROUTE_OF_SECOND_STEP = '/step/Find%20Page/find_page_no_result/startSearch.jsp/0/1';
        var ROUTE_OF_THIRD_STEP = '/step/Find%20Page/find_page_no_result/searchResults.jsp/0/0';

        browser.get('#/');
        homePage.closeScenariooInfoDialogIfOpen();
        scenarioo.docuWriter.saveStep('Display home page with list of use cases');

        homePage.selectUseCase(0);
        scenarioo.docuWriter.saveStep('Display list of scenarios');

        usecasePage.selectScenario(0);
        scenarioo.docuWriter.saveStep('Display one scenario');

        scenarioPage.openStepByName('Step 1: Wikipedia Suche');
        stepPage.assertRoute(ROUTE_OF_FIRST_STEP);
        stepPage.assertPreviousStepIsDisabled();
        stepPage.assertPreviousPageIsDisabled();
        stepPage.assertNextStepIsEnabled();
        stepPage.assertNextPageIsEnabled();
        scenarioo.docuWriter.saveStep('First step of scenario. Back buttons are disabled.');

        stepPage.goToNextStep();
        stepPage.assertRoute(ROUTE_OF_SECOND_STEP);
        stepPage.assertPreviousStepIsEnabled();
        stepPage.assertPreviousPageIsDisabled();
        stepPage.assertNextStepIsEnabled();
        stepPage.assertNextPageIsEnabled();
        scenarioo.docuWriter.saveStep('Second step of scenario. Previous step button is now active.');

        stepPage.goToNextPage();
        stepPage.assertRoute(ROUTE_OF_THIRD_STEP);
        stepPage.assertPreviousStepIsEnabled();
        stepPage.assertPreviousPageIsEnabled();
        stepPage.assertNextStepIsDisabled();
        stepPage.assertNextPageIsDisabled();
        scenarioo.docuWriter.saveStep('Second step of scenario. Previous step button is now active.');

        stepPage.goToPreviousStep();
        stepPage.assertRoute(ROUTE_OF_SECOND_STEP);
        stepPage.goToPreviousStep();
        stepPage.assertRoute(ROUTE_OF_FIRST_STEP);
        stepPage.assertPreviousStepIsDisabled();
        stepPage.assertPreviousPageIsDisabled();
        stepPage.assertNextStepIsEnabled();
        stepPage.assertNextPageIsEnabled();
        scenarioo.docuWriter.saveStep('Back on the first step.');
    });

    scenarioo.describeScenario('If the requested step does not exist, an error message is shown.', function () {
        var stepPage = new pages.stepPage();

        browser.get('#/step/Find Page/find_page_no_result/inexistent_page.jsp/0/42');

        stepPage.assertErrorMessageIsShown();

        scenarioo.docuWriter.saveStep('Error message.');
    });

    scenarioo.describeScenario('The step link popup shows the link to the step and to the image.', function () {
        var stepPage = new pages.stepPage();

        browser.get('#/step/Find Page/find_page_no_result/startSearch.jsp/0/0');
        scenarioo.docuWriter.saveStep('A step.');

        stepPage.clickShowStepLinksButton();
        stepPage.assertStepLinksDialogVisible();
        scenarioo.docuWriter.saveStep('Step links dialog.');
    });

    scenarioo.describeScenario('The step link dialog can also be opened using the "l" shortcut.', function () {
        var stepPage = new pages.stepPage();

        browser.get('#/step/Find Page/find_page_no_result/startSearch.jsp/0/0');
        scenarioo.docuWriter.saveStep('A step.');

        stepPage.type('l');
        stepPage.assertStepLinksDialogVisible();
        scenarioo.docuWriter.saveStep('Step links dialog.');
    });

});