'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');


describeUseCaseE('Use breadcrumbs', {
    description: 'Breadcrumbs help navigating Scenarioo. They are used to navigate back to a use case or a scenario from a more detailed page.'
}, function () {

    var homePage = new pages.homePage();
    var useCasePage = new pages.usecasePage();
    var breadcrumbsPage = new pages.breadcrumbsPage();
    var stepPage = new pages.stepPage();

    beforeEach(function () {
        new pages.homePage().initLocalStorage();
    });

    describeScenarioE('Navigate upwards', {
        description: 'Navigate to the Home Page, filter for one use case, select scenario, click on breadcrumb'
        }, function () {
            homePage.goToPage();
            scenarioo.saveStep('Display the homePage');

            homePage.assertPageIsDisplayed();
            homePage.filterUseCases('User wants');
            scenarioo.saveStep('Enter filter criteria in use case overview');
            homePage.assertUseCasesShown(1);
            scenarioo.saveStep('One use case found');

            homePage.selectUseCase(0);
            scenarioo.saveStep('Selected found use case');

            useCasePage.selectScenario(3);
            scenarioo.saveStep('Selected scenario');

            breadcrumbsPage.assertUseCaseNameInBreadcrumb('breadcrumb_1', 'Find Page');
            breadcrumbsPage.clickOnBreadcrumb('breadcrumb_1');
            scenarioo.saveStep('Clicked on use case in breadcrumb');

            breadcrumbsPage.clickOnBreadcrumb('breadcrumb_0');
            scenarioo.saveStep('Clicked on home breadcrumb');
        }
    );

    describeScenarioE('Tooltip in breadcrumbs', {
        description: 'Navigate to scenario and test for tooltip'
        }, function () {
            stepPage.goToPage('/step/Find%20Page/find_page_with_text_on_page_from_multiple_results/searchResults.jsp/0/0');
            scenarioo.saveStep('Display steps and pages');

            breadcrumbsPage.assertThatTooltipIsShown('tooltip_2', 'Scenario: Find page with ' +
            'text on page from multiple results');
            scenarioo.saveStep('Test that tooltip explicit exists');
        }
    );

});
