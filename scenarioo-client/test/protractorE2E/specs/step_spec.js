'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Step', function () {

    scenarioo.describeScenario('Assert previous-button is disabled on first page', function () {
        var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();
        var stepPage = new pages.stepPage();

        browser.get('#/');
        homePage.closeScenariooInfoDialogIfOpen();
        scenarioo.docuWriter.saveStep('Display home page with list of use cases');

        homePage.selectUseCase(0);
        scenarioo.docuWriter.saveStep('Display list of scenarios');

        usecasePage.selectScenario(0);
        scenarioo.docuWriter.saveStep('Display one scenario');

        scenarioPage.openStepByName('Step 1: Wikipedia Suche');
        stepPage.assertPreviousStepIsDisabled();
        stepPage.assertPreviousPageIsDisabled();
        stepPage.assertNextStepIsEnabled();
        stepPage.assertNextPageIsEnabled();
        scenarioo.docuWriter.saveStep('First step of scenario. Back buttons are disabled.');

        stepPage.goToNextStep();
        stepPage.assertPreviousStepIsEnabled();
        stepPage.assertPreviousPageIsDisabled();
        stepPage.assertNextStepIsEnabled();
        stepPage.assertNextPageIsEnabled();
        scenarioo.docuWriter.saveStep('Second step of scenario. Previous step button is now active.');

        stepPage.goToNextPage();
        stepPage.assertPreviousStepIsEnabled();
        stepPage.assertPreviousPageIsEnabled();
        stepPage.assertNextStepIsDisabled();
        stepPage.assertNextPageIsDisabled();
        scenarioo.docuWriter.saveStep('Second step of scenario. Previous step button is now active.');

        stepPage.goToPreviousStep();
        stepPage.goToPreviousStep();
        stepPage.assertPreviousStepIsDisabled();
        stepPage.assertPreviousPageIsDisabled();
        stepPage.assertNextStepIsEnabled();
        stepPage.assertNextPageIsEnabled();
        scenarioo.docuWriter.saveStep('Back on the first step.');
    });

});