'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Step page', function () {

    scenarioo.describeScenario('Assert previous-button is disabled on first page', function () {
        browser.get('#/');
        var homePage = new pages.homePage();
        homePage.selectUseCase(0);

        var usecasePage = new pages.usecasePage();
        usecasePage.selectScenario(0);

        var scenarioPage = new pages.scenarioPage();
        scenarioPage.openStepByName('Step 1: Wikipedia Suche');

        var stepPage = new pages.stepPage();
        stepPage.assertPreviousStepIsDisabled();
        stepPage.assertNextStepIsEnabled();
    });

});