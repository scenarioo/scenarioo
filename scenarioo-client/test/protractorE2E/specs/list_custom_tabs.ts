'use strict';
import {scenario, step, useCase} from "scenarioo-js";

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var NUMBER_OF_USE_CASES = 4;

useCase('List custom tabs')
    .description('Custom tabs can be defined to show aggregated documentation data..')
    .describe(function () {

        var homePage = new pages.homePage();

        beforeEach(function () {
            homePage.initLocalStorage();
        });

        scenario('Display and filter pages')
            .it(function () {
                homePage.goToPage();
                step('display the homePage');
                homePage.selectPagesTab();
                homePage.assertPagesTabContainsPage('startSearch.jsp');
                step('select the custom tab for pages');
                homePage.filterPages('startSearch');
                homePage.assertCustomTabEntriesShown(1);
                step('filter by name of the page');
            });

    });
