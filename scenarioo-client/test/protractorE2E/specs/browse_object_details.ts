'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import ObjectDetailsPage from '../webPages/objectDetailsPage'

useCase('Browse object details')
    .description('The object details view includes a reference tree with all Use Cases, '
        + 'Scenarios, Steps and other objects that reference this object.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Default expand and collapse')
            .description('Only the first level of nodes (use cases) is expanded initially. All other tree levels are collapsed.')
            .it(async () => {
                await Utils.navigateToRoute('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');
                await ObjectDetailsPage.assertNumberOfRows(29);
                await ObjectDetailsPage.assertTreeNodeStatus('0', 'collapsed');
                await ObjectDetailsPage.assertTreeNodeStatus('1', 'expanded');
                await ObjectDetailsPage.assertTreeNodeStatus('4', 'expanded');
                await ObjectDetailsPage.assertTreeNodeStatus('7', 'expanded');
                await ObjectDetailsPage.assertTreeNodeStatus('10', 'expanded');
            });

        scenario('Nodes have links')
            .description('Nodes in the object reference tree are linked to their respective Scenario pages.')
            .it(async () => {
                await Utils.navigateToRoute('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');

                await ObjectDetailsPage.clickNthTreeTableRow(0);
                await Utils.assertRoute('/usecase/Find%20Page');
                await Utils.clickBrowserBackButton();

                await ObjectDetailsPage.clickNthTreeTableRow(1);
                await Utils.assertRoute('/scenario/Find%20Page/find_multiple_results');
                await Utils.clickBrowserBackButton();

                await ObjectDetailsPage.clickToExpand('1');
                await ObjectDetailsPage.clickNthTreeTableRow(2);
                await Utils.assertRoute('/step/Find%20Page/find_multiple_results/startSearch.jsp/0/0');
                await Utils.clickBrowserBackButton();
            });

        scenario('Search collapsed')
            .description('Collapse all, one match will be found with entered search criteria.')
            .it(async () => {
                await Utils.navigateToRoute('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');
                await ObjectDetailsPage.clickCollapseAll();
                step('Click to collapse all');
                await ObjectDetailsPage.enterSearchCriteria('multiple results');
                await ObjectDetailsPage.assertRowToContainTextAndBeDisplayed('1', 'multiple results');
                await ObjectDetailsPage.assertTreeNodeStatus('1', 'expanded');
                step('Entered search criteria leads to one match that is expanded');
                await ObjectDetailsPage.resetSearchCriteriaWithEsc();
                step('Reset search criteria with ESC');
            });

        scenario('Double click')
            .description('Double click on root node expands all child nodes.')
            .it(async () => {
                await Utils.navigateToRoute('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');
                await ObjectDetailsPage.clickCollapseAll();
                step('Click collapse all');
                await ObjectDetailsPage.doubleClickOnNode('0');
                step('Double click on root node to expand all childs');
                await ObjectDetailsPage.assertTreeNodeStatus('11', 'collapsed');
                await ObjectDetailsPage.assertTreeNodeIsDisplayed('12');
                step('All childs are visible and expanded');
            });

    });
