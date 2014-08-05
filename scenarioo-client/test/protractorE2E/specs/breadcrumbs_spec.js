'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');


scenarioo.describeUseCase('Breadcrump', function () {
    scenarioo.describeScenario('Navigate to the Home Page, filter for one usecase select scenario click on first breadcrumb',
        function () {
            var homePage = new pages.homePage();
            var useCasePage = new pages.usecasePage();
            var scenarioPage = new pages.scenarioPage();
            var breadcrumpsPage = new pages.breadcrumpsPage();

            browser.get('#/');
            scenarioo.docuWriter.saveStep('display the homePage');
            homePage.assertPageIsDisplayed();
            homePage.filterUseCases('User wants');
            homePage.assertUseCasesShown(1);
            scenarioo.docuWriter.saveStep('one use case found');
            homePage.selectUseCase(0);
            scenarioo.docuWriter.saveStep('selected found use case');
            useCasePage.selectScenario(3);
            scenarioo.docuWriter.saveStep('selected scenario');
            breadcrumpsPage.clickOnBreadcrump('breadcrump.1');

            var usecaseName = breadcrumpsPage.getUsecaseName('breadcrump.1');

            // TODO: check something on usecase view (e.g. name)
    });
});