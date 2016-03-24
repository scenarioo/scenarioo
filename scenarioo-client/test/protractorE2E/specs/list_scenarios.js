'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');
var NUMBER_OF_USE_CASES = 4;

describeUseCaseE('List scenarios', {
    description: 'After clicking on a use case, the user is presented with a list of all scenarios in this use case.'
}, function () {

    var homePage = new pages.homePage();
    var useCasePage = new pages.usecasePage();
    var scenarioPage = new pages.scenarioPage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    describeScenarioE('Expand all, collapse all on scenario page', function () {
        homePage.goToPage();
        scenarioo.saveStep('select a use case from the use case list');
        homePage.assertPageIsDisplayed();
        homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
        homePage.selectUseCase(1);
        scenarioo.saveStep('select a scenario in the scenario list');
        useCasePage.selectScenario(0);
        scenarioo.saveStep('all pages are collapsed by default, "expand all" button is visible');
        scenarioPage.expectOnlyExpandAllButtonIsDisplayed();
        scenarioPage.toggleShowAllStepsOfPage(0);
        scenarioo.saveStep('"expand all" button and "collapse all" button are both visible');
        scenarioPage.expectExpandAllAndCollapseAllButtonBothDisplayed();
        scenarioPage.toggleShowAllStepsOfPage(1);
        scenarioo.saveStep('Only "collapse all" visible');
        scenarioPage.expectOnlyCollapseAllButtonIsDisplayed();
    });

});
