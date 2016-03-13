'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

describeUseCaseE('Browse object details', {
    description: 'The object details view includes a reference tree with all Use Cases, '
            + 'Scenarios, Steps and other objects that reference this object.'
    },
    function () {

        var objectDetailsPage = new pages.objectDetailsPage();
        var usecasePage = new pages.usecasePage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        describeScenarioE('Default expand and collapse', {
            description: 'Only the first child from root-node is expanded initially. All further nodes are collapsed.'
        }, function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.saveStep('Display object details page');
            objectDetailsPage.assertNumberOfRows(30);
            objectDetailsPage.assertTreeNodeStatus('0', 'collapsed');
            objectDetailsPage.assertTreeNodeStatus('1', 'expanded');
            objectDetailsPage.assertTreeNodeStatus('4', 'expanded');
            objectDetailsPage.assertTreeNodeStatus('7', 'expanded');
            objectDetailsPage.assertTreeNodeStatus('10', 'expanded');
        });

        describeScenarioE('Nodes have links', {
            description: 'Nodes in the object reference tree are linked to their respective Scenario pages.'
        }, function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.saveStep('Display object details page');

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

        describeScenarioE('Search collapsed', {
            description: 'Collapse all, one match will be found with entered search criteria.'
        }, function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.saveStep('Display object details page');
            objectDetailsPage.clickCollapseAll();
            scenarioo.saveStep('Click to collapse all');
            objectDetailsPage.enterSearchCriteria('multiple results');
            objectDetailsPage.assertRowToContainTextAndBeDisplayed('1', 'multiple results');
            objectDetailsPage.assertTreeNodeStatus('1', 'expanded');
            scenarioo.saveStep('Entered search criteria leads to one match that is expanded');
            objectDetailsPage.resetSearchCriteriaWithEsc();
            scenarioo.saveStep('Reset search criteria with ESC');
        });

        describeScenarioE('Double click', {
            description: 'Double click on root node expands all child nodes.'
        }, function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.saveStep('Display object details page');
            objectDetailsPage.clickCollapseAll();
            scenarioo.saveStep('Click collapse all');
            objectDetailsPage.doubleClickOnNode('0');
            scenarioo.saveStep('Double click on root node to expand all childs');
            objectDetailsPage.assertTreeNodeStatus('11', 'collapsed');
            objectDetailsPage.assertTreeNodeIsDisplayed('12');
            scenarioo.saveStep('All childs are visible and expanded');
        });

    }
);
