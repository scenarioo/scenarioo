'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Scenario overview', function () {

    scenarioo.describeScenario('Expand all, collapse all on scenario page', function () {
        var homePage = new pages.homePage();
        var useCasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();

        browser.get('#/');
        scenarioo.docuWriter.saveStep('select a use case on in the use case list');
        homePage.assertPageIsDisplayed();
        homePage.assertUseCasesShown(2);
        homePage.selectUseCase(0);
        scenarioo.docuWriter.saveStep('select a scenario in the scenario list');
        useCasePage.selectScenario(1);
        scenarioo.docuWriter.saveStep('all pages are collapsed by default, "expand all" button is visible');
        scenarioPage.expectOnlyExpandAllButtonIsDisplayed();
        scenarioPage.toggleShowAllStepsOfPage(0);
        scenarioo.docuWriter.saveStep('"expand all" button and "collapse all" button are both visible');
        scenarioPage.expectExpandAllAndCollapseAllButtonBothDisplayed();
        scenarioPage.toggleShowAllStepsOfPage(1);
        scenarioo.docuWriter.saveStep('Only "collapse all" visible');
        scenarioPage.expectOnlyCollapseAllButtonIsDisplayed();
    });

});