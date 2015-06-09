'use strict';

// TODO [#389] This file is prefixed with a_ so that the test method that tests the "first time popup" is always executed first.
// This should not be required anymore, see linked issue.

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');


var NUMBER_OF_USE_CASES = 4;


scenarioo.describeUseCase('List use cases', function () {
    browser.driver.manage().window().maximize();

    scenarioo.describeScenario('Navigate to the Home Page, display popup without cookie', function () {
        var homePage = new pages.homePage();
        homePage.goToPage();
        scenarioo.docuWriter.saveStep('display the homePage with popup');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogShown();
        homePage.closeScenariooInfoDialogIfOpen();
        homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
        scenarioo.docuWriter.saveStep('display the homePage');
    });

    scenarioo.describeScenario('Navigate to the Home Page, do not display popup when cookie set', function () {
        var homePage = new pages.homePage();
        homePage.goToPage();
        scenarioo.docuWriter.saveStep('display the homePage without popup');
        homePage.assertPageIsDisplayed();
        homePage.assertScenariooInfoDialogNotShown();
        homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
    });

    scenarioo.describeScenario('Navigate to the Home Page, filter usecases', function () {
        var homePage = new pages.homePage();
        homePage.goToPage();
        scenarioo.docuWriter.saveStep('display the homePage');
        homePage.assertPageIsDisplayed();
        homePage.filterUseCases('notinlist');
        homePage.assertUseCasesShown(0);
        scenarioo.docuWriter.saveStep('no use cases shown');
        homePage.filterUseCases('find page');
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('one use case found');
        homePage.filterUseCases('user wants find page');
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('one use case found');
    });

    scenarioo.describeScenario('Navigate to the Home Page, show and hide metadata', function () {
        var homePage = new pages.homePage();
        homePage.goToPage();
        scenarioo.docuWriter.saveStep('display the homePage, metadata shown');
        homePage.assertPageIsDisplayed();
        homePage.assertMetaDataShown();
        homePage.hideMetaData();
        homePage.assertMetaDataHidden();
        scenarioo.docuWriter.saveStep('metadata hidden');
        homePage.showMetaData();
        homePage.assertMetaDataShown();
        scenarioo.docuWriter.saveStep('metadata shown');
    });

});
