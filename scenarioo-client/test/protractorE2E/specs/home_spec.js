'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Home', function () {

    scenarioo.describeScenario('Navigate to the Home Page, display popup without cookie', function () {
        var homePage = new pages.homePage();
        browser.get('#/');
        scenarioo.docuWriter.saveStep('display the homePage with popup');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogShown();
        homePage.closeScenariooInfoDialog();
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('display the homePage');
    });

    scenarioo.describeScenario('Navigate to the Home Page, do not display popup when cookie set', function () {
        var homePage = new pages.homePage();
        browser.get('#/');
        scenarioo.docuWriter.saveStep('display the homePage without popup');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogNotShown();
        homePage.assertUseCasesShown(1);
    });

    scenarioo.describeScenario('Navigate to the Home Page, filter usecases', function () {
        var homePage = new pages.homePage();
        browser.get('#/');
        scenarioo.docuWriter.saveStep('display the homePage');
        homePage.assertPageIsDisplayed();
        homePage.filterUseCases('notinlist');
        homePage.assertUseCasesShown(0);
        scenarioo.docuWriter.saveStep('no use cases shown');
        homePage.filterUseCases('find page');
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('one use case found');
        homePage.filterUseCases('user wants find page');
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('one use case found');
    });

});

scenarioo.describeUseCase('Step', function () {
    scenarioo.describeScenario('Assert previous-button is disabled on first page', function () {
        browser.get('#/');
        var homePage = new pages.homePage();
        homePage.selectUseCase(0);

        var usecasePage = new pages.usecasePage();
        usecasePage.selectScenario(0);

        var scenarioPage = new pages.scenarioPage();
        scenarioPage.openStepByName('Step 1: Wikipedia Suche');

        var stepPage = new pages.stepPage();
        stepPage.assertPreviousStepIsDisabled();
        stepPage.assertNextStepIsEnabled();
    });
});