'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import HomePage from '../pages/homePage';
import DetailAreaPage from '../pages/detailAreaPage';

// Just to make TypeScript happy
declare var angular: any;

const SECOND_USE_CASE = 1;

useCase('Browse Detailarea')
    .description('The user is presented the detailarea (collapsed/expanded) as it is saved in the LocalStorage')
    .describe(() => {

        beforeEach(async () => {
            await Utils.clearLocalStorageAndSetPreviouslyVisited();
            await HomePage.goToPage();
            await HomePage.assertPageIsDisplayed();
        });

        scenario('Remembers collapsed state of details area and sections on usecases overview')
            .description('States of detailarea and sections are correctly saved in LocalStorage.')
            .it(async () => {
                await DetailAreaPage.assertDetailsAreaExpanded();
                await DetailAreaPage.assertSectionExpanded('Build');
                await DetailAreaPage.assertSectionExpanded('Branch');
                await step('Detailarea and important sections are expanded on first visit');

                await DetailAreaPage.collapseDetailsSection('Build');
                await DetailAreaPage.assertSectionCollapsed('Build');
                await step('Build section has been collapsed');

                await DetailAreaPage.collapseDetailsArea();
                await DetailAreaPage.assertDetailsAreaCollapsed();
                await step('Detailarea has been collapsed');

                await Utils.refreshBrowser();
                await DetailAreaPage.assertDetailsAreaCollapsed();
                await step('Collapsed detailarea is remembered on revisit.');

                await DetailAreaPage.expandDetailsArea();
                await DetailAreaPage.assertDetailsAreaExpanded();
                await step('Detailarea has been expanded');

                await DetailAreaPage.assertSectionExpanded('Branch');
                await DetailAreaPage.assertSectionCollapsed('Build');
                await step('Collapsed or expanded areas are remembered on revisit');

                await DetailAreaPage.expandDetailsSection('Build');
                await DetailAreaPage.assertSectionExpanded('Build');
                await step('Build section has been expanded');
            });

        scenario('Remembers collapsed state of details area and sections on scenarios overview')
            .description('States of detailarea and sections are correctly saved in LocalStorage.')
            .it(async () => {
                await HomePage.selectUseCase(SECOND_USE_CASE);
                await step('select a scenario in the scenario list');

                await DetailAreaPage.assertDetailsAreaExpanded();
                await DetailAreaPage.assertSectionExpanded('Use Case');
                await DetailAreaPage.assertSectionCollapsed('Labels');
                await DetailAreaPage.assertSectionCollapsed('Webtest Class');
                await step('Detailarea and important sections are expanded on first visit');

                await DetailAreaPage.collapseDetailsSection('Use Case');
                await DetailAreaPage.assertSectionCollapsed('Use Case');
                await DetailAreaPage.expandDetailsSection('Labels');
                await DetailAreaPage.assertSectionExpanded('Labels');
                await step('Use Case section has been collapsed and Labels section expanded');

                await DetailAreaPage.collapseDetailsArea();
                await DetailAreaPage.assertDetailsAreaCollapsed();
                await step('Detailarea has been collapsed');

                await Utils.refreshBrowser();
                await DetailAreaPage.assertDetailsAreaCollapsed();
                await step('Collapsed detailarea is remembered on revisit.');

                await DetailAreaPage.expandDetailsArea();
                await DetailAreaPage.assertDetailsAreaExpanded();
                await step('Detailarea has been expanded');

                await DetailAreaPage.assertSectionCollapsed('Use Case');
                await DetailAreaPage.assertSectionExpanded('Labels');
                await step('Collapsed or expanded areas are remembered on revisit');
            });
    });