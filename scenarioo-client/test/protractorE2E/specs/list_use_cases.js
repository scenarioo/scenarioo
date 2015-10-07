'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var NUMBER_OF_USE_CASES = 4;

scenarioo.describeUseCase('List use cases', 'As soon as a branch and a build are selected, a list of use cases is shown.', function () {

    var homePage = new pages.homePage();

    beforeEach(function(){
        homePage.initLocalStorage();
    });

    scenarioo.describeScenario('Display and filter usecases', function () {
        homePage.goToPage();
        homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
        scenarioo.docuWriter.saveStep('display usecases on homepage');
        homePage.assertPageIsDisplayed();
        homePage.filterUseCases('notinlist');
        homePage.assertUseCasesShown(0);
        scenarioo.docuWriter.saveStep('filter applied: no use cases shown');
        homePage.filterUseCases('find page');
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('filter applied: one use case found');
        homePage.filterUseCases('user wants find page');
        homePage.assertUseCasesShown(1);
        scenarioo.docuWriter.saveStep('other filter applied: one use case found');
    });

    scenarioo.describeScenario('Show and hide metadata', function () {
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
