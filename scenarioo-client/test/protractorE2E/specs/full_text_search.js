'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Full Text Search')
    .description('Search within all information of the selected build.')
    .describe(function () {

        var generalSettingsPage = new pages.generalSettingsPage();
        var homePage = new pages.homePage();
        var navigationPage = new pages.navigationPage();
        var searchResultsPage = new pages.searchResultsPage();
        var breadcrumbsPage = new pages.breadcrumbsPage();
        var stepPage = new pages.stepPage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Configuration and status')
            .description('The admin page shows the configured elaasticsearch endpoint and whether it\'s reachable.')
            .it(function () {
                generalSettingsPage.goToPage();
                generalSettingsPage.assertSearchEndpointConfiguredAndReachable();
                step('Search endpoint is configured and reachable');
            });

        scenario('Search with result')
            .description('Search for a term that yields some results.')
            .it(function() {
                homePage.goToPage();
                navigationPage.enterSearchTerm('donate.jsp');
                step('Search term entered');

                navigationPage.clickSearchButton();
                breadcrumbsPage.assertBreadcrumbElementText('breadcrumb_0', 'Home');
                breadcrumbsPage.assertBreadcrumbElementText('breadcrumb_last_1', 'Search Results for donate');
                searchResultsPage.assertResultTableTitle('Search Results (showing 1 of 1 hits)');
                searchResultsPage.assertNumberOfResultRows(3);
                step('Search results');

                searchResultsPage.openFirstScenarioAndClickStep();
                stepPage.assertRoute('/step/Donate/find_donate_page/donate.jsp/0/0');
                stepPage.assertScreenshotIsShown();
                step('After navigating to a search result step');
            });

        scenario('Search with and without HTML source')
            .description('By default the HTML source is not searched, but the user can select to also search it.')
            .it(function() {
                homePage.goToPage();
                navigationPage.enterSearchTerm('body');
                step('Search term body entered, which mainly appears in the html source code');

                navigationPage.clickSearchButton();
                breadcrumbsPage.assertBreadcrumbElementText('breadcrumb_0', 'Home');
                breadcrumbsPage.assertBreadcrumbElementText('breadcrumb_last_1', 'Search Results for body');
                searchResultsPage.assertNoResultsShown();
                step('No results, as not searching in html source');
                
                searchResultsPage.clickIncludeHtml();
                breadcrumbsPage.assertBreadcrumbElementText('breadcrumb_0', 'Home');
                breadcrumbsPage.assertBreadcrumbElementText('breadcrumb_last_1', 'Search Results for body');
                searchResultsPage.assertResultTableTitle('Search Results (showing 32 of 32 hits)');
                searchResultsPage.assertNumberOfResultRows(43);
                step('Many results, as now searching in html source as well');
            });
    });
