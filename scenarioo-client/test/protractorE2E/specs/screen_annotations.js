'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('View screen annotations', function () {

    var stepPage = new pages.stepPage();

    beforeEach(function () {
        new pages.homePage().initLocalStorage();
    });

    scenarioo.describeScenario('No screen annotations are shown if the step does not contain any annotations', function () {
        stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/0');
        stepPage.assertNoScreenAnnotationsArePresent();
        scenarioo.docuWriter.saveStep('No screen annotations are shown');
    });

    scenarioo.describeScenario('Screen annotations are shown if the step has some', function () {
        stepPage.goToPage('/step/Find Page/find_multiple_results/startSearch.jsp/0/1');
        stepPage.assertTwoScreenAnnotationsArePresent();
        scenarioo.docuWriter.saveStep('Two screen annotations are shown');
    });

});
