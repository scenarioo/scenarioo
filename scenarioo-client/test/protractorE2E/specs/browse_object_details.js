'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Browse object details')
    .description('The object details view includes a reference tree with all Use Cases, '
        + 'Scenarios, Steps and other objects that reference this object.')
    .describe(function () {

        var objectDetailsPage = new pages.objectDetailsPage();
        var usecasePage = new pages.usecasePage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Default expand and collapse')
            .description('Only the first level of nodes (use cases) is expanded initially. All other tree levels are collapsed.')
            .it(function () {
                objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');
                objectDetailsPage.assertNumberOfRows(30);
                objectDetailsPage.assertTreeNodeStatus('0', 'collapsed');
                objectDetailsPage.assertTreeNodeStatus('1', 'expanded');
                objectDetailsPage.assertTreeNodeStatus('4', 'expanded');
                objectDetailsPage.assertTreeNodeStatus('7', 'expanded');
                objectDetailsPage.assertTreeNodeStatus('10', 'expanded');
            });

        scenario('Nodes have links')
            .description('Nodes in the object reference tree are linked to their respective Scenario pages.')
            .it(function () {
                objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');

                objectDetailsPage.clickNthTreeTableRow(0);
                objectDetailsPage.assertRoute('/usecase/Find%20Page');
                usecasePage.clickBrowserBackButton();

                objectDetailsPage.clickNthTreeTableRow(1);
                objectDetailsPage.assertRoute('/scenario/Find%20Page/find_multiple_results');
                usecasePage.clickBrowserBackButton();

                objectDetailsPage.clickToExpand('1');
                objectDetailsPage.clickNthTreeTableRow(2);
                objectDetailsPage.assertRoute('/step/Find%20Page/find_multiple_results/startSearch.jsp/0/0');
                usecasePage.clickBrowserBackButton();
            });

        scenario('Search collapsed')
            .description('Collapse all, one match will be found with entered search criteria.')
            .it(function () {
                objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');
                objectDetailsPage.clickCollapseAll();
                step('Click to collapse all');
                objectDetailsPage.enterSearchCriteria('multiple results');
                objectDetailsPage.assertRowToContainTextAndBeDisplayed('1', 'multiple results');
                objectDetailsPage.assertTreeNodeStatus('1', 'expanded');
                step('Entered search criteria leads to one match that is expanded');
                objectDetailsPage.resetSearchCriteriaWithEsc();
                step('Reset search criteria with ESC');
            });

        scenario('Double click')
            .description('Double click on root node expands all child nodes.')
            .it(function () {
                objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
                step('Display object details page');
                objectDetailsPage.clickCollapseAll();
                step('Click collapse all');
                objectDetailsPage.doubleClickOnNode('0');
                step('Double click on root node to expand all childs');
                objectDetailsPage.assertTreeNodeStatus('11', 'collapsed');
                objectDetailsPage.assertTreeNodeIsDisplayed('12');
                step('All childs are visible and expanded');
            });

    });
