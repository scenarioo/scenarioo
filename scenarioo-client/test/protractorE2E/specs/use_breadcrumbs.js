'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');


scenarioo.describeUseCase('Use breadcrumbs', 'Breadcrumbs help navigating Scenarioo. They are used to navigate back to a use case or a scenario from a more detailed page.', function () {

    var homePage = new pages.homePage();
    var useCasePage = new pages.usecasePage();
    var breadcrumbsPage = new pages.breadcrumbsPage();
    var stepPage = new pages.stepPage();

    beforeEach(function () {
        new pages.homePage().initLocalStorage();
    });

    scenarioo.describeScenario('Navigate upwards', 'Navigate to the Home Page, filter for one use case, select scenario, click on breadcrumb', function () {
            homePage.goToPage();
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

            breadcrumbsPage.assertUseCaseNameInBreadcrumb('breadcrumb_1', 'Find Page');
            breadcrumbsPage.clickOnBreadcrumb('breadcrumb_1');
            scenarioo.docuWriter.saveStep('Clicked on use case in breadcrumb');

            breadcrumbsPage.clickOnBreadcrumb('breadcrumb_0');
            scenarioo.docuWriter.saveStep('Clicked on home breadcrumb');
        }
    );

    scenarioo.describeScenario('Tooltip in breadcrumbs', 'Navigate to scenario and test for tooltip', function () {
            stepPage.goToPage('/step/Find%20Page/find_page_with_text_on_page_from_multiple_results/searchResults.jsp/0/0');
            scenarioo.docuWriter.saveStep('Display steps and pages');

            breadcrumbsPage.assertThatTooltipIsShown('tooltip_2', 'Scenario: Find page with ' +
            'text on page from multiple results');
            scenarioo.docuWriter.saveStep('Test that tooltip explicit exists');
        }
    );

});
