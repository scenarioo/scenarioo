'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Object details', function () {
/*
    scenarioo.describeScenario('Nodes in the object reference tree are linked to their respective Scenarioo pages', function () {
        var homePage = new pages.homePage();
        var usecasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();
        var stepPage = new pages.stepPage();
        var objectDetailsPage = new pages.objectDetailsPage();

        browser.get('#/object/uiAction/example.action.StartInitAction');
        homePage.closeScenariooInfoDialogIfOpen();
        scenarioo.docuWriter.saveStep('Display object details page');

        objectDetailsPage.clickNthTreeTableRow(0);
        objectDetailsPage.assertRoute('/usecase/Find%20Page');
        usecasePage.clickBrowserBackButton();

        objectDetailsPage.clickNthTreeTableRow(1);
        objectDetailsPage.assertRoute('/scenario/Find%2520Page/find_page_no_result');
        usecasePage.clickBrowserBackButton();

        objectDetailsPage.clickNthTreeTableRow(2);
        objectDetailsPage.assertRoute('/step/Find%2520Page/find_page_no_result/startSearch.jsp/0/0');
        usecasePage.clickBrowserBackButton();
    });
 */
    scenarioo.describeScenario('Only the first child from root-node is expanded', function () {
        var homePage = new pages.homePage();
        var objectDetailsPage = new pages.objectDetailsPage();

        browser.get('#/object/uiAction/example.action.StartInitAction');
        homePage.closeScenariooInfoDialogIfOpen();
        scenarioo.docuWriter.saveStep('Display object details page');
        objectDetailsPage.assertElementIsExpanded(0);
    });

});