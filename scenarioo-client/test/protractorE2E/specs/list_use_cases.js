'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var NUMBER_OF_USE_CASES = 4;

describeUseCaseE('List use cases', {
    description: 'As soon as a branch and a build are selected, a list of use cases is shown.'
}, function () {

    var homePage = new pages.homePage();

    beforeEach(function(){
        homePage.initLocalStorage();
    });

    describeScenarioE('Display and filter usecases', function () {
        homePage.goToPage();
        homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
        scenarioo.saveStep('display usecases on homepage');
        homePage.assertPageIsDisplayed();
        homePage.filterUseCases('notinlist');
        homePage.assertUseCasesShown(0);
        scenarioo.saveStep('filter applied: no use cases shown');
        homePage.filterUseCases('find page');
        homePage.assertUseCasesShown(1);
        scenarioo.saveStep('filter applied: one use case found');
        homePage.filterUseCases('user wants find page');
        homePage.assertUseCasesShown(1);
        scenarioo.saveStep('other filter applied: one use case found');
    });

    describeScenarioE('Show and hide metadata', function () {
        homePage.goToPage();
        scenarioo.saveStep('display the homePage, metadata shown');
        homePage.assertPageIsDisplayed();
        homePage.assertMetaDataShown();
        homePage.hideMetaData();
        homePage.assertMetaDataHidden();
        scenarioo.saveStep('metadata hidden');
        homePage.showMetaData();
        homePage.assertMetaDataShown();
        scenarioo.saveStep('metadata shown');
    });

});
