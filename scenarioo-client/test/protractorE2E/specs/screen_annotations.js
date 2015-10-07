'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('View screen annotations', 'Screens can have visual annotations that e.g. mark where a click was done or where data was entered.', function () {

    var stepPage = new pages.stepPage();

    beforeEach(function () {
        new pages.homePage().initLocalStorage();
    });

    scenarioo.describeScenario('Screen without annotations', 'No screen annotations are shown if the step does not contain any annotations', function () {
        stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/0');
        stepPage.assertScreenshotIsShown();
        stepPage.assertNoScreenAnnotationsArePresent();
        scenarioo.docuWriter.saveStep('No screen annotations are shown');
    });

    scenarioo.describeScenario('Screen with annotations', 'Screen annotations are shown if the step has some', function () {
        stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/1');
        stepPage.assertScreenshotIsShown();
        stepPage.assertTwoScreenAnnotationsAreVisible();
        scenarioo.docuWriter.saveStep('Two screen annotations are shown');
    });

    scenarioo.describeScenario('Show and hide annotations', 'Screen annotations can be hidden by clicking a button', function () {
        stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/1');
        stepPage.assertScreenshotIsShown();
        stepPage.assertTwoScreenAnnotationsAreVisible();
        scenarioo.docuWriter.saveStep('Two screen annotations are shown');

        stepPage.clickShowScreenAnnotationsButton();
        stepPage.assertNoScreenAnnotationsAreVisible();
        scenarioo.docuWriter.saveStep('Screen annotations are hidden');

        stepPage.clickShowScreenAnnotationsButton();
        stepPage.assertTwoScreenAnnotationsAreVisible();
        scenarioo.docuWriter.saveStep('Two screen annotations are shown');
    });

});
