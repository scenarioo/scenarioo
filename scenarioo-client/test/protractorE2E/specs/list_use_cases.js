'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var NUMBER_OF_USE_CASES = 4;
var COMPARISON_PROJECTSTART = 'To Projectstart';
var COMPARISON_DISABLE = 'Disable';
var USE_CASE_WITH_HIGHEST_DIFF = 'Donate';

useCase('List use cases')
    .description('As soon as a branch and a build are selected, a list of use cases is shown.')
    .describe(function () {

        var homePage = new pages.homePage();

        beforeEach(function () {
            homePage.initLocalStorage();
        });

        scenario('Display and filter usecases')
            .it(function () {
                homePage.goToPage();
                homePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
                step('display usecases on homepage');
                homePage.assertPageIsDisplayed();
                homePage.filterUseCases('notinlist');
                homePage.assertUseCasesShown(0);
                step('filter applied: no use cases shown');
                homePage.filterUseCases('find page');
                homePage.assertUseCasesShown(1);
                step('filter applied: one use case found');
                homePage.filterUseCases('user wants find page');
                homePage.assertUseCasesShown(1);
                step('other filter applied: one use case found');
            });

        scenario('Show and hide metadata')
            .it(function () {
                homePage.goToPage();
                step('display the homePage, metadata shown');
                homePage.assertPageIsDisplayed();
                homePage.assertMetaDataShown();
                homePage.hideMetaData();
                homePage.assertMetaDataHidden();
                step('metadata hidden');
                homePage.showMetaData();
                homePage.assertMetaDataShown();
                step('metadata shown');
            });

        scenario('Display Diff-Information')
            .it(function () {
                homePage.goToPage();
                step('display usecases on homepage');
                homePage.chooseComparison(COMPARISON_PROJECTSTART);
                homePage.assertPageIsDisplayed();
                step('To Projectstart comparison selected');

                homePage.assertNumberOfDiffInfos(NUMBER_OF_USE_CASES);

                // Reset
                homePage.chooseComparison(COMPARISON_DISABLE);
            });

        scenario('Sort by Diff-Information')
            .it(function () {
                homePage.goToPage();
                step('display usecases on homepage');
                homePage.chooseComparison(COMPARISON_PROJECTSTART);
                homePage.assertPageIsDisplayed();
                step('To Projectstart comparison selected');

                homePage.sortByChanges();
                homePage.assertLastUseCase(USE_CASE_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted ascending');

                homePage.sortByChanges();
                homePage.assertFirstUseCase(USE_CASE_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted descending');

                // Reset
                homePage.chooseComparison(COMPARISON_DISABLE);
            });
    });
