'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Show step')
    .description('Show a single step of a scenario. Includes the screenshot, metadata nad navigation buttons')
    .describe(function () {

        var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();
        var stepPage = new pages.stepPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Navigation')
            .description('Navigate back and forth through the scenario steps.')
            .it(function () {

                var ROUTE_OF_FIRST_STEP = '/step/Find%20Page/find_no_results/startSearch.jsp/0/0';
                var ROUTE_OF_SECOND_STEP = '/step/Find%20Page/find_no_results/startSearch.jsp/0/1';
                var ROUTE_OF_THIRD_STEP = '/step/Find%20Page/find_no_results/searchResults.jsp/0/0';

                homePage.goToPage();
                step('Display home page with list of use cases');

                homePage.selectUseCase(1);
                step('Display list of scenarios');

                usecasePage.selectScenario(1);
                step('Display one scenario');

                scenarioPage.openStepByName('Step 1: Wikipedia Suche');
                stepPage.assertRoute(ROUTE_OF_FIRST_STEP);
                stepPage.assertPreviousStepIsDisabled();
                stepPage.assertPreviousPageIsDisabled();
                stepPage.assertNextStepIsEnabled();
                stepPage.assertNextPageIsEnabled();
                step('First step of scenario. Back buttons are disabled.');

                stepPage.goToNextStep();
                stepPage.assertRoute(ROUTE_OF_SECOND_STEP);
                stepPage.assertPreviousStepIsEnabled();
                stepPage.assertPreviousPageIsDisabled();
                stepPage.assertNextStepIsEnabled();
                stepPage.assertNextPageIsEnabled();
                step('Second step of scenario. Previous step button is now active.');

                stepPage.goToNextPage();
                stepPage.assertRoute(ROUTE_OF_THIRD_STEP);
                stepPage.assertPreviousStepIsEnabled();
                stepPage.assertPreviousPageIsEnabled();
                stepPage.assertNextStepIsDisabled();
                stepPage.assertNextPageIsDisabled();
                step('Second step of scenario. Previous step button is now active.');

                stepPage.goToPreviousStep();
                stepPage.assertRoute(ROUTE_OF_SECOND_STEP);
                stepPage.goToPreviousStep();
                stepPage.assertRoute(ROUTE_OF_FIRST_STEP);
                stepPage.assertPreviousStepIsDisabled();
                stepPage.assertPreviousPageIsDisabled();
                stepPage.assertNextStepIsEnabled();
                stepPage.assertNextPageIsEnabled();
                step('Back on the first step.');
            });

        scenario('Step does not exist')
            .description('If the requested step does not exist, an error message is shown.')
            .it(function () {

                stepPage.goToPage('/step/Find Page/find_no_results/inexistent_page.jsp/0/42');
                stepPage.assertErrorMessageIsShown();

                step('Error message.');
            });

        scenario('Fallback step exists')
            .description('A fallback message is shown in case the page does not exist but a fallback is found.')
            .it(function () {

                stepPage.goToPage('/step/Find%20Page/renamed_scenario/searchResults.jsp/0/0');

                stepPage.assertFallbackMessageIsShown();
                stepPage.assertFallbackMessageContainsText('Scenario: find_multiple_results');

                step('Fallback message.');
            });

        scenario('Fallback to best match')
            .description('If the fallback mechanism finds multiple candidates, the one with the most matching labels is used.')
            .it(function () {

                stepPage.goToPage('/step/RenamedUseCase/DeletedScenario/contentPage.jsp/111/222?labels=exact%20match,i18n,step-label-2,public,page-label1,page-label2');

                stepPage.assertFallbackMessageIsShown();
                stepPage.assertFallbackMessageContainsText('Usecase: Switch Language');
                stepPage.assertFallbackMessageContainsText('Scenario: search_article_in_german_and_switch_to_spanish');
                stepPage.assertScenarioLabelsContain('i18n');
                step('Of the 10 page variants, a fallback step with an i18n label is returned.');
            });

        scenario('Share step')
            .description('The step link popup shows the link to the step and to the image.')
            .it(function () {

                stepPage.goToPage('/step/Find Page/find_no_results/startSearch.jsp/0/0');
                step('A step.');

                stepPage.clickShareThisPageLink();
                stepPage.assertStepLinksDialogVisible();
                step('Step links dialog.');
            });

        scenario('Metadata with link to object')
            .description('Click on a object link in Call tree and jump to object example.action.StartInitAction')
            .it(function () {

                stepPage.goToPage('/step/Find%20Page/find_no_results/startSearch.jsp/0/0');
                stepPage.openMetadataTabIfClosed(1);
                step('Expand Call tree panel');

                stepPage.clickOnLink('uiAction_example.action.StartInitAction');
                stepPage.assertToolTipInBreadcrumb('uiAction: example.action.StartInitAction');
            });

        scenario('HTML view of current step')
            .description('If the step data contains html source data, it should be displayed in the HTML tab')
            .it(function () {

                stepPage.goToPage('/step/Find%20Page/find_no_results/startSearch.jsp/0/0');
                step('A step');

                stepPage.clickHtmlTabButton();
                step('Switch to HTML tab');

                stepPage.assertHtmlSourceEquals('<html>\n<head>\n</head>\n<body>\n   <p>just some dummy html code</p>\n</body>\n</html>');

                stepPage.clickScreenshotTabButton();
                step('Switch back to Screenshot tab');
            });

        scenario('Step without HTML source attached')
            .description('If the step data contains no html source data, the HTML tab should not be displayed at all')
            .it(function () {

                stepPage.goToPage('/step/Donate/find_donate_page/startSearch.jsp/0/0');
                stepPage.assertHtmlTabIsHidden();
                step('A step with no HTML source attached');
            });

    });
