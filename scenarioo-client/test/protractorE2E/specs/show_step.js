'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Show step', 'Show a single step of a scenario. Includes the screenshot, metadata nad navigation buttons', function () {

    var homePage = new pages.homePage();
    var usecasePage = new pages.usecasePage();
    var scenarioPage = new pages.scenarioPage();
    var stepPage = new pages.stepPage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    scenarioo.describeScenario('Navigation', 'Navigate back and forth through the scenario steps.', function () {
        var ROUTE_OF_FIRST_STEP = '/step/Find%20Page/find_no_results/startSearch.jsp/0/0';
        var ROUTE_OF_SECOND_STEP = '/step/Find%20Page/find_no_results/startSearch.jsp/0/1';
        var ROUTE_OF_THIRD_STEP = '/step/Find%20Page/find_no_results/searchResults.jsp/0/0';

        homePage.goToPage();
        scenarioo.docuWriter.saveStep('Display home page with list of use cases');

        homePage.selectUseCase(1);
        scenarioo.docuWriter.saveStep('Display list of scenarios');

        usecasePage.selectScenario(1);
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

    scenarioo.describeScenario('Step does not exist', 'If the requested step does not exist, an error message is shown.', function () {
        stepPage.goToPage('/step/Find Page/find_no_results/inexistent_page.jsp/0/42');
        stepPage.assertErrorMessageIsShown();

        scenarioo.docuWriter.saveStep('Error message.');
    });

    scenarioo.describeScenario('Fallback step exists', 'A fallback message is shown in case the page does not exist but a fallback is found.', function () {
        stepPage.goToPage('/step/Find%20Page/renamed_scenario/searchResults.jsp/0/0');

        stepPage.assertFallbackMessageIsShown();
        stepPage.assertFallbackMessageContainsText('Scenario: find_multiple_results');

        scenarioo.docuWriter.saveStep('Fallback message.');
    });

    scenarioo.describeScenario('Fallback to best match', 'If the fallback mechanism finds multiple candidates, the one with the most matching labels is used.', function() {
        stepPage.goToPage('/step/RenamedUseCase/DeletedScenario/contentPage.jsp/111/222?labels=exact%20match,i18n,step-label-2,public,page-label1,page-label2');

        stepPage.assertFallbackMessageIsShown();
        stepPage.assertFallbackMessageContainsText('Usecase: Switch Language');
        stepPage.assertFallbackMessageContainsText('Scenario: search_article_in_german_and_switch_to_spanish');
        stepPage.assertScenarioLabelsContain('i18n');
        scenarioo.docuWriter.saveStep('Of the 10 page variants, a fallback step with an i18n label is returned.');
    });

    scenarioo.describeScenario('Share step', 'The step link popup shows the link to the step and to the image.', function () {
        stepPage.goToPage('/step/Find Page/find_no_results/startSearch.jsp/0/0');

        scenarioo.docuWriter.saveStep('A step.');

        stepPage.clickShareThisPageLink();
        stepPage.assertStepLinksDialogVisible();
        scenarioo.docuWriter.saveStep('Step links dialog.');
    });

    scenarioo.describeScenario('Metadata with link to object', 'Click on a object link in Call tree and jump to object example.action.StartInitAction', function () {
        stepPage.goToPage('/step/Find%20Page/find_no_results/startSearch.jsp/0/0');

        stepPage.openMetadataTabIfClosed(0);
        scenarioo.docuWriter.saveStep('Expand Call tree panel');

        stepPage.clickOnLink('uiAction_example.action.StartInitAction');
        stepPage.assertToolTipInBreadcrumb('uiAction: example.action.StartInitAction');
    });

});
