'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Browse object details', 'The object details view includes a reference tree with all Use Cases, '
    + 'Scenarios, Steps and other objects that reference this object.',
    function () {

        var objectDetailsPage = new pages.objectDetailsPage();
        var usecasePage = new pages.usecasePage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenarioo.describeScenario('Default expand and collapse', 'Only the first child from root-node is expanded initially. All further nodes are collapsed.', function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.docuWriter.saveStep('Display object details page');
            objectDetailsPage.assertNumberOfRows(30);
            objectDetailsPage.assertTreeNodeStatus('0', 'collapsed');
            objectDetailsPage.assertTreeNodeStatus('1', 'expanded');
            objectDetailsPage.assertTreeNodeStatus('4', 'expanded');
            objectDetailsPage.assertTreeNodeStatus('7', 'expanded');
            objectDetailsPage.assertTreeNodeStatus('10', 'expanded');
        });

        scenarioo.describeScenario('Nodes have links', 'Nodes in the object reference tree are linked to their respective Scenario pages.', function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.docuWriter.saveStep('Display object details page');

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

        scenarioo.describeScenario('Search collapsed', 'Collapse all, one match will be found with entered search criteria.', function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.docuWriter.saveStep('Display object details page');
            objectDetailsPage.clickCollapseAll();
            scenarioo.docuWriter.saveStep('Click to collapse all');
            objectDetailsPage.enterSearchCriteria('multiple results');
            objectDetailsPage.assertRowToContainTextAndBeDisplayed('1', 'multiple results');
            objectDetailsPage.assertTreeNodeStatus('1', 'expanded');
            scenarioo.docuWriter.saveStep('Entered search criteria leads to one match that is expanded');
            objectDetailsPage.resetSearchCriteriaWithEsc();
            scenarioo.docuWriter.saveStep('Reset search criteria with ESC');
        });

        scenarioo.describeScenario('Double click', 'Double click on root node expands all child nodes.', function () {
            objectDetailsPage.goToPage('/object/uiAction/example.action.StartInitAction');
            scenarioo.docuWriter.saveStep('Display object details page');
            objectDetailsPage.clickCollapseAll();
            scenarioo.docuWriter.saveStep('Click collapse all');
            objectDetailsPage.doubleClickOnNode('0');
            scenarioo.docuWriter.saveStep('Double click on root node to expand all childs');
            objectDetailsPage.assertTreeNodeStatus('11', 'collapsed');
            objectDetailsPage.assertTreeNodeIsDisplayed('12');
            scenarioo.docuWriter.saveStep('All childs are visible and expanded');
        });

    }
);
