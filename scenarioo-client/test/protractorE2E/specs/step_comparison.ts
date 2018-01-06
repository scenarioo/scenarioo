'use strict';

import {scenario, step, useCase} from "scenarioo-js";

const pages = require('./../webPages');

const COMPARISON_SCREENSHOT_SRC = 'rest/branch/wikipedia-docu-example/build/2014-01-20/usecase/Find%20Page/scenario/find_page_title_unique_directly/image/002.png';
const BASE_SCREENSHOT_SRC = 'rest/branch/wikipedia-docu-example/build/last%20successful/usecase/Find%20Page/scenario/find_page_title_unique_directly/image/002.png';

useCase('Step - Comparison')
    .description('Compare the screen of a step with same step in another run of same test in another build')
    .describe(function () {

        const homePage = new pages.homePage();
        const scenarioPage = new pages.scenarioPage();
        const stepPage = new pages.stepPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Compare Screens')
            .description('Show screenshot comparison')
            .labels(['diff-viewer'])
            .it(function () {

                stepPage.goToPage('/step/Find%20Page/find_page_title_unique_directly/contentPage.jsp/0/0?branch=wikipedia-docu-example&build=last%20successful&comparison=To%20Projectstart');
                step('Changed Step is displayed');

                stepPage.openComparisonTab();
                stepPage.assertStepComparisonSideBySideViewIsActive();
                stepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                stepPage.expectStepComparisonOtherScreenTitle('To Projectstart: 2014-01-20', 'January 20, 2014, 12:00 AM 1290FE2');
                stepPage.expectHighlightsDisplayed();
                stepPage.assertStepComparisonScreenshotSrcEquals(COMPARISON_SCREENSHOT_SRC);
                stepPage.expectStepComparisonLegendText('Highlighted Changes in Screen');
                step('Screen Comparison Side by Side is displayed');

                stepPage.hideHighlights();
                stepPage.expectHighlightsHidden();
                stepPage.assertStepBaseScreenshotSrcEquals(BASE_SCREENSHOT_SRC);
                step('Highlights hidden');

                stepPage.showHighlights();
                stepPage.expectHighlightsDisplayed();
                step('Highlights displayed again');

                stepPage.showComparisonCurrentScreenView();
                stepPage.assertStepComparisonCurrentScreenViewIsActive();
                stepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                stepPage.expectSwitchComparisonSingleScreensButtonEnabled();
                stepPage.assertStepBaseScreenshotSrcEquals(BASE_SCREENSHOT_SRC);
                step('Current Screen displayed with highlighted changes');

                stepPage.switchComparisonSingleScreens();
                stepPage.assertStepComparisonOtherScreenViewIsActive();
                stepPage.expectStepComparisonOtherScreenTitle('To Projectstart: 2014-01-20', 'January 20, 2014, 12:00 AM 1290FE2');
                stepPage.expectSwitchComparisonSingleScreensButtonEnabled();
                stepPage.assertStepComparisonScreenshotSrcEquals(COMPARISON_SCREENSHOT_SRC);
                step('Other Screen displayed with highlighted changes');

                stepPage.clickScreenshotTabButton();
                step('Step Tab selected again');
            });

        scenario('Compare Added Step')
            .description('For added steps no comparison screenshot is available.')
            .labels(['diff-viewer'])
            .it(function () {

                const SCREENSHOT_SRC = 'rest/branch/wikipedia-docu-example/build/last%20successful/usecase/Donate/scenario/find_donate_page/image/001.png';
                stepPage.goToPage('/step/Donate/find_donate_page/startSearch.jsp/0/1?branch=wikipedia-docu-example&build=last%20successful&comparison=To%20Projectstart');
                step('Added Step is displayed');

                stepPage.openComparisonTab();
                stepPage.showSideBySideView();
                stepPage.assertStepComparisonSideBySideViewIsActive();
                stepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                stepPage.expectStepComparisonOtherScreenTitle('To Projectstart: 2014-01-20', 'January 20, 2014, 12:00 AM 1290FE2');
                stepPage.expectHighlightsButtonHidden();
                stepPage.assertStepNoComparisonScreenshot();
                stepPage.assertStepBaseScreenshotSrcEquals(SCREENSHOT_SRC);
                stepPage.expectSwitchComparisonSingleScreensButtonDisabled();
                stepPage.expectStepComparisonOtherScreenViewIsDisabled();
                stepPage.expectStepComparisonLegendText('Added Step: No Comparison');
                step('Screen Comparison Side by Side for added step is displayed');

                stepPage.showComparisonCurrentScreenView();
                stepPage.assertStepComparisonCurrentScreenViewIsActive();
                stepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                stepPage.expectSwitchComparisonSingleScreensButtonDisabled();
                stepPage.expectHighlightsButtonHidden();
                stepPage.assertStepNoComparisonScreenshot();
                stepPage.assertStepBaseScreenshotSrcEquals(SCREENSHOT_SRC);
                stepPage.expectStepComparisonOtherScreenViewIsDisabled();
                step('Current Screen displayed only for added step');

            });

    });
