'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import HomePage from '../webPages/homePage';
import UsecasePage from '../webPages/usecasePage';
import BreadcrumbsPage from '../webPages/breadcrumbsPage';

useCase('Use breadcrumbs')
    .description('Breadcrumbs help navigating Scenarioo. They are used to navigate back to a use case or a scenario from a more detailed page.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Navigate upwards')
            .description('Navigate to the Home Page, filter for one use case, select scenario, click on breadcrumb')
            .it(async () => {
                await Utils.navigateToRoute('/');
                await step('Display the homePage');

                await HomePage.assertPageIsDisplayed();
                await HomePage.filterUseCases('User wants');
                await step('Enter filter criteria in use case overview');

                await HomePage.assertUseCasesShown(1);
                await step('One use case found');

                await HomePage.selectUseCase(0);
                await step('Selected found use case');

                await UsecasePage.selectScenario(3);
                await step('Selected scenario');

                await BreadcrumbsPage.assertBreadcrumbElementText('breadcrumb_1', 'Find Page');
                await BreadcrumbsPage.clickOnBreadcrumb('breadcrumb_1');
                await step('Clicked on use case in breadcrumb');

                await BreadcrumbsPage.clickOnBreadcrumb('breadcrumb_0');
                await step('Clicked on home breadcrumb');
            });

        scenario('Tooltip in breadcrumbs')
            .description('Navigate to scenario and test for tooltip')
            .it(async () => {
                await Utils.navigateToRoute('/step/Find%20Page/find_page_with_text_on_page_from_multiple_results/searchResults.jsp/0/0');
                await step('Display steps and pages');

                await BreadcrumbsPage.assertThatTooltipIsShown('tooltip_2', 'Scenario: Find page with ' +
                    'text on page from multiple results');
                await step('Test that tooltip explicit exists');
            });

    });
