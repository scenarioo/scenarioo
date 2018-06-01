'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import HomePage from '../webPages/homePage';
import StepPage from '../webPages/stepPage';
import BreadcrumbsPage from '../webPages/breadcrumbsPage';
import SearchResultsPage from '../webPages/searchResultsPage';
import NavigationPage from '../webPages/navigationPage';
import GeneralSettingsPage from '../webPages/generalSettingsPage';

useCase('Full Text Search')
    .description('Search within all information of the selected build.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Configuration and status')
            .description('The admin page shows the configured elaasticsearch endpoint and whether it\'s reachable.')
            .it(async () => {
                await GeneralSettingsPage.goToPage();
                await GeneralSettingsPage.assertSearchEndpointConfiguredAndReachable();
                await step('Search endpoint is configured and reachable');
            });

        scenario('Search with result')
            .description('Search for a term that yields some results.')
            .it(async () => {
                await HomePage.goToPage();
                await NavigationPage.enterSearchTerm('donate.jsp');
                await step('Search term entered');

                await NavigationPage.clickSearchButton();
                await BreadcrumbsPage.assertBreadcrumbElementText('breadcrumb_0', 'Home');
                await BreadcrumbsPage.assertBreadcrumbElementText('breadcrumb_last_1', 'Search Results for donate');
                await SearchResultsPage.assertResultTableTitle('Search Results (showing 1 of 1 hits)');
                await SearchResultsPage.assertNumberOfResultRows(3);
                await step('Search results');

                await SearchResultsPage.openFirstScenarioAndClickStep();
                await Utils.assertRoute('/step/Donate/find_donate_page/donate.jsp/0/0');
                await StepPage.assertScreenshotIsShown();
                await step('After navigating to a search result step');
            });

        scenario('Search with and without HTML source')
            .description('By default the HTML source is not searched, but the user can select to also search it.')
            .it(async () => {
                await HomePage.goToPage();
                await NavigationPage.enterSearchTerm('body');
                await step('Search term body entered, which mainly appears in the html source code');

                await NavigationPage.clickSearchButton();
                await BreadcrumbsPage.assertBreadcrumbElementText('breadcrumb_0', 'Home');
                await BreadcrumbsPage.assertBreadcrumbElementText('breadcrumb_last_1', 'Search Results for body');
                await SearchResultsPage.assertNoResultsShown();
                await step('No results, as not searching in html source');

                await SearchResultsPage.clickIncludeHtml();
                await BreadcrumbsPage.assertBreadcrumbElementText('breadcrumb_0', 'Home');
                await BreadcrumbsPage.assertBreadcrumbElementText('breadcrumb_last_1', 'Search Results for body');
                await SearchResultsPage.assertResultTableTitle('Search Results (showing 32 of 32 hits)');
                await SearchResultsPage.assertNumberOfResultRows(43);
                await step('Many results, as now searching in html source as well');
            });
    });
