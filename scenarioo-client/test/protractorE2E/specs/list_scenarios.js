'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');
var NUMBER_OF_USE_CASES = 4;

scenarioo.describeUseCase('list_scenarios', function () {

    var homePage = new pages.homePage();
    var useCasePage = new pages.usecasePage();
    var scenarioPage = new pages.scenarioPage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    scenarioo.describeScenario('Expand all, collapse all on scenario page', function () {


        homePage.goToPage();
        scenarioo.docuWriter.saveStep('select a use case from the use case list');
        homePage.assertPageIsDisplayed();
        homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
        homePage.selectUseCase(1);
        scenarioo.docuWriter.saveStep('select a scenario in the scenario list');
        useCasePage.selectScenario(0);
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
