'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');


scenarioo.describeUseCase('Use breadcrumbs', function () {

    scenarioo.describeScenario('Navigate to the Home Page, filter for one usecase select scenario click on breadcrumb',
        function () {
            var homePage = new pages.homePage();
            var useCasePage = new pages.usecasePage();
            var breadcrumpsPage = new pages.breadcrumpsPage();

            browser.get('#/');
            homePage.closeScenariooInfoDialogIfOpen();
            scenarioo.docuWriter.saveStep('Display the homePage');

            homePage.assertPageIsDisplayed();
            homePage.filterUseCases('User wants');
            scenarioo.docuWriter.saveStep('Enter filter criteria in use case overview');
            homePage.assertUseCasesShown(1);
            scenarioo.docuWriter.saveStep('One use case found');

            homePage.selectUseCase(0);
            scenarioo.docuWriter.saveStep('Selected found use case');

            useCasePage.selectScenario(3);
            scenarioo.docuWriter.saveStep('Selected scenario');

            breadcrumpsPage.assertUseCaseNameInBreadcrumb('breadcrumb_1', 'Find Page');
            breadcrumpsPage.clickOnBreadcrumb('breadcrumb_1');
            scenarioo.docuWriter.saveStep('Clicked on use case in breadcrumb');

            breadcrumpsPage.clickOnBreadcrumb('breadcrumb_0');
            scenarioo.docuWriter.saveStep('Clicked on home breadcrumb');
        }
    );

    scenarioo.describeScenario('Navigate to scenario and test for tooltip',
        function () {
            var breadcrumpsPage = new pages.breadcrumpsPage();

            browser.get('#/step/Find%20Page/find_page_with_text_on_page_from_multiple_results/searchResults.jsp/0/0');
            scenarioo.docuWriter.saveStep('Display steps and pages');

            breadcrumpsPage.assertThatTooltipIsShown('tooltip_2', 'Scenario: Find page with ' +
                'text on page from multiple results');
            scenarioo.docuWriter.saveStep('Test that tooltip explicit exists');

        }
    );

});