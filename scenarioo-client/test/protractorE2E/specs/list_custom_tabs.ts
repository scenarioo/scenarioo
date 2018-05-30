'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import HomePage from '../webPages/homePage';

useCase('List custom tabs')
    .description('Custom tabs can be defined to show aggregated documentation data..')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Display and filter pages')
            .it(async () => {
                await Utils.navigateToRoute('/');
                step('display the homePage');
                await HomePage.selectPagesTab();
                await HomePage.assertPagesTabContainsPage('startSearch.jsp');
                step('select the custom tab for pages');
                await HomePage.filterPages('startSearch');
                await HomePage.assertCustomTabEntriesShown(1);
                step('filter by name of the page');
            });

    });
