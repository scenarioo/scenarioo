'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');
var NUMBER_OF_USE_CASES = 4;

useCase('List scenarios')
    .description('After clicking on a use case, the user is presented with a list of all scenarios in this use case.')
    .describe(function () {

        var homePage = new pages.homePage();
        var useCasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Expand all, collapse all on scenario page')
            .it(function () {
                homePage.goToPage();
                step('select a use case from the use case list');
                homePage.assertPageIsDisplayed();
                homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
                homePage.selectUseCase(1);
                step('select a scenario in the scenario list');
                useCasePage.selectScenario(0);
                step('all pages are collapsed by default, "expand all" button is visible');
                scenarioPage.expectOnlyExpandAllButtonIsDisplayed();
                scenarioPage.toggleShowAllStepsOfPage(0);
                step('"expand all" button and "collapse all" button are both visible');
                scenarioPage.expectExpandAllAndCollapseAllButtonBothDisplayed();
                scenarioPage.toggleShowAllStepsOfPage(1);
                step('Only "collapse all" visible');
                scenarioPage.expectOnlyCollapseAllButtonIsDisplayed();
            });

    });
