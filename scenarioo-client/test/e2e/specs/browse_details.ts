'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import HomePage from '../pages/homePage';
import DetailAreaPage from '../pages/detailAreaPage';

const SECOND_USE_CASE = 1;

useCase('Browse Details')
    .description('The user can browse details sections and expanded/collapsed state is remembered on revisit')
    .describe(() => {

        beforeEach(async () => {
            await Utils.clearLocalStorageAndSetPreviouslyVisited();
            await HomePage.goToPage();
            await HomePage.assertPageIsDisplayed();
        });

        scenario('on usecases overview')
            .description('Remembers collapsed state of details area and sections on usecases overview')
            .it(async () => {
                await DetailAreaPage.assertDetailsExpanded();
                await DetailAreaPage.assertSectionExpanded('Build');
                await DetailAreaPage.assertSectionExpanded('Branch');
                await step('Detailarea and important sections are expanded on first visit');

                await DetailAreaPage.collapseSection('Build');
                await DetailAreaPage.assertSectionCollapsed('Build');
                await step('Build section has been collapsed');

                await DetailAreaPage.collapseDetails();
                await DetailAreaPage.assertDetailsCollapsed();
                await step('Detailarea has been collapsed');

                await Utils.refreshBrowser();
                await DetailAreaPage.assertDetailsCollapsed();
                await step('Collapsed detailarea is remembered on revisit.');

                await DetailAreaPage.expandDetails();
                await DetailAreaPage.assertDetailsExpanded();
                await step('Detailarea has been expanded');

                await DetailAreaPage.assertSectionExpanded('Branch');
                await DetailAreaPage.assertSectionCollapsed('Build');
                await step('Collapsed or expanded areas are remembered on revisit');

                await DetailAreaPage.expandSection('Build');
                await DetailAreaPage.assertSectionExpanded('Build');
                await step('Build section has been expanded');
            });

        scenario('on scenarios overview')
            .description('Remembers collapsed state of details area and sections on scenarios overview')
            .it(async () => {
                await HomePage.selectUseCase(SECOND_USE_CASE);
                await step('select a scenario in the scenario list');

                await DetailAreaPage.assertDetailsExpanded();
                await DetailAreaPage.assertSectionExpanded('Use Case');
                await DetailAreaPage.assertSectionCollapsed('Labels');
                await DetailAreaPage.assertSectionCollapsed('Webtest Class');
                await step('Detailarea and important sections are expanded on first visit');

                await DetailAreaPage.collapseSection('Use Case');
                await DetailAreaPage.assertSectionCollapsed('Use Case');
                await DetailAreaPage.expandSection('Labels');
                await DetailAreaPage.assertSectionExpanded('Labels');
                await step('Use Case section has been collapsed and Labels section expanded');

                await DetailAreaPage.collapseDetails();
                await DetailAreaPage.assertDetailsCollapsed();
                await step('Detailarea has been collapsed');

                await Utils.refreshBrowser();
                await DetailAreaPage.assertDetailsCollapsed();
                await step('Collapsed detailarea is remembered on revisit.');

                await DetailAreaPage.expandDetails();
                await DetailAreaPage.assertDetailsExpanded();
                await step('Detailarea has been expanded');

                await DetailAreaPage.assertSectionCollapsed('Use Case');
                await DetailAreaPage.assertSectionExpanded('Labels');
                await step('Collapsed or expanded areas are remembered on revisit');
            });
    });
