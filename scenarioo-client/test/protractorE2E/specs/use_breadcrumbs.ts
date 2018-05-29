'use strict';

import { scenario, step, useCase } from "scenarioo-js";
import * as Utils from "../util/util";

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Use breadcrumbs')
    .description('Breadcrumbs help navigating Scenarioo. They are used to navigate back to a use case or a scenario from a more detailed page.')
    .describe(function () {

        var homePage = new pages.homePage();
        var useCasePage = new pages.usecasePage();
        var breadcrumbsPage = new pages.breadcrumbsPage();
        var stepPage = new pages.stepPage();

        beforeEach(async function () {
            await Utils.startScenariooRevisited();
        });

        scenario('Navigate upwards')
            .description('Navigate to the Home Page, filter for one use case, select scenario, click on breadcrumb')
            .it(async function () {

                Utils.navigateToRoute();
                step('Display the homePage');

                homePage.assertPageIsDisplayed();
                homePage.filterUseCases('User wants');
                step('Enter filter criteria in use case overview');
                homePage.assertUseCasesShown(1);
                step('One use case found');

                homePage.selectUseCase(0);
                step('Selected found use case');

                useCasePage.selectScenario(3);
                step('Selected scenario');

                breadcrumbsPage.assertBreadcrumbElementText('breadcrumb_1', 'Find Page');
                breadcrumbsPage.clickOnBreadcrumb('breadcrumb_1');
                step('Clicked on use case in breadcrumb');

                breadcrumbsPage.clickOnBreadcrumb('breadcrumb_0');
                step('Clicked on home breadcrumb');
            });

        scenario('Tooltip in breadcrumbs')
            .description('Navigate to scenario and test for tooltip')
            .it(async function () {

                Utils.navigateToRoute('/step/Find%20Page/find_page_with_text_on_page_from_multiple_results/searchResults.jsp/0/0');
                step('Display steps and pages');

                breadcrumbsPage.assertThatTooltipIsShown('tooltip_2', 'Scenario: Find page with ' +
                    'text on page from multiple results');
                step('Test that tooltip explicit exists');
            });

    });
