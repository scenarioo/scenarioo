'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');


scenarioo.describeUseCase('Breadcrump', function () {
    scenarioo.describeScenario('Navigate to the Home Page, filter usecases', function () {
        var homePage = new pages.homePage();
        var useCasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();
        var breadcrumpsPage = new pages.breadcrumpsPage();

        scenarioo.docuWriter.saveStep('display the homePage');
        homePage.assertPageIsDisplayed();
        homePage.filterUseCases('User wants');
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('one use case found');
        homePage.selectUseCase(0);
        scenarioo.docuWriter.saveStep('selected found use case');
    });
});