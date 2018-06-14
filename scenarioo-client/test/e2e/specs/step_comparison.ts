'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import StepPage from '../pages/stepPage';

const COMPARISON_SCREENSHOT_SRC = 'rest/branch/wikipedia-docu-example/build/2014-01-20/usecase/Find%20Page/scenario/find_page_title_unique_directly/image/002.png';
const BASE_SCREENSHOT_SRC = 'rest/branch/wikipedia-docu-example/build/last%20successful/usecase/Find%20Page/scenario/find_page_title_unique_directly/image/002.png';

useCase('Step - Comparison')
    .description('Compare the screen of a step with same step in another run of same test in another build')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Compare Screens')
            .description('Show screenshot comparison')
            .labels(['diff-viewer'])
            .it(async () => {
                await Utils.navigateToRoute('/step/Find%20Page/find_page_title_unique_directly/contentPage.jsp/0/0?branch=wikipedia-docu-example&build=last%20successful&comparison=To%20Projectstart');
                await step('Changed Step is displayed');

                await StepPage.openComparisonTab();
                await StepPage.assertStepComparisonSideBySideViewIsActive();
                await StepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                await StepPage.expectStepComparisonOtherScreenTitle('To Projectstart: 2014-01-20', 'January 20, 2014, 12:00 AM 1290FE2');
                await StepPage.expectHighlightsDisplayed();
                await StepPage.assertStepComparisonScreenshotSrcEquals(COMPARISON_SCREENSHOT_SRC);
                await StepPage.expectStepComparisonLegendText('Highlighted Changes in Screen');
                await step('Screen Comparison Side by Side is displayed');

                await StepPage.hideHighlights();
                await StepPage.expectHighlightsHidden();
                await StepPage.assertStepBaseScreenshotSrcEquals(BASE_SCREENSHOT_SRC);
                await step('Highlights hidden');

                await StepPage.showHighlights();
                await StepPage.expectHighlightsDisplayed();
                await step('Highlights displayed again');

                await StepPage.showComparisonCurrentScreenView();
                await StepPage.assertStepComparisonCurrentScreenViewIsActive();
                await StepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                await StepPage.expectSwitchComparisonSingleScreensButtonEnabled();
                await StepPage.assertStepBaseScreenshotSrcEquals(BASE_SCREENSHOT_SRC);
                await step('Current Screen displayed with highlighted changes');

                await StepPage.switchComparisonSingleScreens();
                await StepPage.assertStepComparisonOtherScreenViewIsActive();
                await StepPage.expectStepComparisonOtherScreenTitle('To Projectstart: 2014-01-20', 'January 20, 2014, 12:00 AM 1290FE2');
                await StepPage.expectSwitchComparisonSingleScreensButtonEnabled();
                await StepPage.assertStepComparisonScreenshotSrcEquals(COMPARISON_SCREENSHOT_SRC);
                await step('Other Screen displayed with highlighted changes');

                await StepPage.clickScreenshotTabButton();
                await step('Step Tab selected again');
            });

        scenario('Compare Added Step')
            .description('For added steps no comparison screenshot is available.')
            .labels(['diff-viewer'])
            .it(async () => {
                const SCREENSHOT_SRC = 'rest/branch/wikipedia-docu-example/build/last%20successful/usecase/Donate/scenario/find_donate_page/image/001.png';
                await Utils.navigateToRoute('/step/Donate/find_donate_page/startSearch.jsp/0/1?branch=wikipedia-docu-example&build=last%20successful&comparison=To%20Projectstart');
                await step('Added Step is displayed');

                await StepPage.openComparisonTab();
                await StepPage.showSideBySideView();
                await StepPage.assertStepComparisonSideBySideViewIsActive();
                await StepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                await StepPage.expectStepComparisonOtherScreenTitle('To Projectstart: 2014-01-20', 'January 20, 2014, 12:00 AM 1290FE2');
                await StepPage.expectHighlightsButtonHidden();
                await StepPage.assertStepNoComparisonScreenshot();
                await StepPage.assertStepBaseScreenshotSrcEquals(SCREENSHOT_SRC);
                await StepPage.expectSwitchComparisonSingleScreensButtonDisabled();
                await StepPage.expectStepComparisonOtherScreenViewIsDisabled();
                await StepPage.expectStepComparisonLegendText('Added Step: No Comparison');
                await step('Screen Comparison Side by Side for added step is displayed');

                await StepPage.showComparisonCurrentScreenView();
                await StepPage.assertStepComparisonCurrentScreenViewIsActive();
                await StepPage.expectStepComparisonCurrentScreenTitle('Current: last successful: 2014-03-19', 'March 19, 2014, 12:00 AM F398DA3');
                await StepPage.expectSwitchComparisonSingleScreensButtonDisabled();
                await StepPage.expectHighlightsButtonHidden();
                await StepPage.assertStepNoComparisonScreenshot();
                await StepPage.assertStepBaseScreenshotSrcEquals(SCREENSHOT_SRC);
                await StepPage.expectStepComparisonOtherScreenViewIsDisabled();
                await step('Current Screen displayed only for added step');
            });

    });
