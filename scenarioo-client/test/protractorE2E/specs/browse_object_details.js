'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Browse object details', function () {

    scenarioo.describeScenario('Only the first child from root-node is expanded initially. All further nodes are collapsed', function () {
        var homePage = new pages.homePage();
        var objectDetailsPage = new pages.objectDetailsPage();

        browser.get('#/object/uiAction/example.action.StartInitAction');
        homePage.closeScenariooInfoDialogIfOpen();
        scenarioo.docuWriter.saveStep('Display object details page');
        objectDetailsPage.assertTreeNodeStatus('0', 'collapsed');
        objectDetailsPage.assertTreeNodeStatus('1', 'expanded');
        objectDetailsPage.assertTreeNodeStatus('4', 'expanded');
        objectDetailsPage.assertTreeNodeStatus('7', 'expanded');
        objectDetailsPage.assertTreeNodeStatus('10', 'expanded');
    });

    scenarioo.describeScenario('Nodes in the object reference tree are linked to their respective Scenarioo pages', function () {
        var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
        var objectDetailsPage = new pages.objectDetailsPage();

        browser.get('#/object/uiAction/example.action.StartInitAction');
        homePage.closeScenariooInfoDialogIfOpen();
        scenarioo.docuWriter.saveStep('Display object details page');

        objectDetailsPage.clickNthTreeTableRow(0);
        objectDetailsPage.assertRoute('/usecase/Find%20Page');
        usecasePage.clickBrowserBackButton();

        objectDetailsPage.clickNthTreeTableRow(1);
        objectDetailsPage.assertRoute('/scenario/Find%20Page/find_page_no_result');
        usecasePage.clickBrowserBackButton();

        objectDetailsPage.clickToExpand('1');
        objectDetailsPage.clickNthTreeTableRow(2);
        objectDetailsPage.assertRoute('/step/Find%20Page/find_page_no_result/startSearch.jsp/0/0');
        usecasePage.clickBrowserBackButton();
    });

    scenarioo.describeScenario('Collapse all, one match will be found with entered search criteria', function () {
        var homePage = new pages.homePage();
        var objectDetailsPage = new pages.objectDetailsPage();
        homePage.closeScenariooInfoDialogIfOpen();

        browser.get('#/object/uiAction/example.action.StartInitAction');
        scenarioo.docuWriter.saveStep('Display object details page');
        objectDetailsPage.clickCollapseAll();
        scenarioo.docuWriter.saveStep('Click to collapse all');
        objectDetailsPage.enterSearchCriteria('multiple results');
        scenarioo.docuWriter.saveStep('Entered search criteria leads to one match');
        objectDetailsPage.assertTreeNodeStatus('4', 'expanded');
        objectDetailsPage.resetSearchCriteriaWithEsc();
        scenarioo.docuWriter.saveStep('Reset search criteria with ESC');
    });

    scenarioo.describeScenario('Double click on root node expands all child nodes', function () {
        var homePage = new pages.homePage();
        var objectDetailsPage = new pages.objectDetailsPage();
        homePage.closeScenariooInfoDialogIfOpen();

        browser.get('#/object/uiAction/example.action.StartInitAction');
        scenarioo.docuWriter.saveStep('Display object details page');
        objectDetailsPage.clickCollapseAll();
        scenarioo.docuWriter.saveStep('Click collapse all');
        objectDetailsPage.doubleClickOnNode('0');
        scenarioo.docuWriter.saveStep('Double click on root node to expand all childs');
        objectDetailsPage.assertTreeNodeStatus('11', 'collapsed');
        objectDetailsPage.assertTreeNodeIsDisplayed('12');
        scenarioo.docuWriter.saveStep('All childs are visible and expanded');
    });

});
