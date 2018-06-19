'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import StepPage from '../pages/stepPage';

useCase('Step - Screen Annotations')
    .description('Screens can have visual annotations that e.g. mark where a click was done or where data was entered that can be displayed in step view.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Screen without annotations')
            .description('No screen annotations are shown if the step does not contain any annotations')
            .it(async () => {
                await Utils.navigateToRoute('/step/Find Page/find_multiple_results/startSearch.jsp/0/0');
                await StepPage.assertScreenshotIsShown();
                await StepPage.assertNoScreenAnnotationsArePresent();
                await step('No screen annotations are shown');
            });

        scenario('Screen with annotations')
            .description('Screen annotations are shown if the step has some')
            .it(async () => {
                await Utils.navigateToRoute('/step/Find Page/find_multiple_results/startSearch.jsp/0/1');
                await StepPage.assertScreenshotIsShown();
                await StepPage.assertNumberOfVisibleScreenAnnotationsIs(2);
                await step('Two screen annotations are shown');
            });

        scenario('Show and hide annotations')
            .description('Screen annotations can be hidden by clicking a button')
            .it(async () => {
                await Utils.navigateToRoute('/step/Find Page/find_multiple_results/startSearch.jsp/0/1');
                await StepPage.assertScreenshotIsShown();
                await StepPage.assertNumberOfVisibleScreenAnnotationsIs(2);
                await step('Two screen annotations are shown');

                await StepPage.clickShowScreenAnnotationsButton();
                await StepPage.assertNoScreenAnnotationsAreVisible();
                await step('Screen annotations are hidden');

                await StepPage.clickShowScreenAnnotationsButton();
                await StepPage.assertNumberOfVisibleScreenAnnotationsIs(2);
                await step('Two screen annotations are shown');
            });

        scenario('Popup')
            .description('Further information about the annotation is displayed in a popup')
            .it(async () => {
                await Utils.navigateToRoute('/step/Technical%20Corner%20Cases/dummy_scenario_with_screen_annotations_of_all_types_on_one_page/specialPageWithOnlyOneVariant.jsp/0/1');
                await StepPage.assertScreenshotIsShown();
                await StepPage.assertNumberOfVisibleScreenAnnotationsIs(11);
                await step('Eleven screen annotations are shown');

                await StepPage.clickFirstScreenAnnotation();
                await StepPage.assertScreenAnnotationPopupIsDisplayed();
                await StepPage.assertTitleOfAnnotationPopupIs('  DEFAULT-Annotation \'Life Is Beautiful\'');
                await step('Popup is shown');
            });

    });
