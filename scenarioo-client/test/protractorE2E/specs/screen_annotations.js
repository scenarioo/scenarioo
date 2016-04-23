'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('View screen annotations')
    .description('Screens can have visual annotations that e.g. mark where a click was done or where data was entered.')
    .describe(function () {

        var stepPage = new pages.stepPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Screen without annotations')
            .description('No screen annotations are shown if the step does not contain any annotations')
            .it(function () {
                stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/0');
                stepPage.assertScreenshotIsShown();
                stepPage.assertNoScreenAnnotationsArePresent();
                step('No screen annotations are shown');
            });

        scenario('Screen with annotations')
            .description('Screen annotations are shown if the step has some')
            .it(function () {
                stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/1');
                stepPage.assertScreenshotIsShown();
                stepPage.assertNumberOfVisibleScreenAnnotationsIs(2);
                step('Two screen annotations are shown');
            });

        scenario('Show and hide annotations')
            .description('Screen annotations can be hidden by clicking a button')
            .it(function () {
                stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/1');
                stepPage.assertScreenshotIsShown();
                stepPage.assertNumberOfVisibleScreenAnnotationsIs(2);
                step('Two screen annotations are shown');

                stepPage.clickShowScreenAnnotationsButton();
                stepPage.assertNoScreenAnnotationsAreVisible();
                step('Screen annotations are hidden');

                stepPage.clickShowScreenAnnotationsButton();
                stepPage.assertNumberOfVisibleScreenAnnotationsIs(2);
                step('Two screen annotations are shown');
            });

        scenario('Popup')
            .description('Further information about the annotation is displayed in a popup')
            .it(function () {
                stepPage.goToPage('/step/Technical%20Corner%20Cases/dummy_scenario_with_screen_annotations_of_all_types_on_one_page/specialPageWithOnlyOneVariant.jsp/0/1');
                stepPage.assertScreenshotIsShown();
                stepPage.assertNumberOfVisibleScreenAnnotationsIs(11);
                step('Eleven screen annotations are shown');

                // This part of the test always fails on the CI but runns locally
                // TODO #473 Fix this
                /*
                 stepPage.clickFirstScreenAnnotation();
                 stepPage.assertScreenAnnotationPopupIsDisplayed();
                 stepPage.assertTitleOfAnnotationPopupIs('DEFAULT-Annotation \'Life Is Beautiful\'');
                 scenarioo.docuWriter.saveStep('Popup is shown');
                 */
            });

    });
