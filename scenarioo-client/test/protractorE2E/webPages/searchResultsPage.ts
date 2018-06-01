'use strict';

import {by, element, $, $$} from 'protractor';
import * as Utils from '../util/util';

class SearchResultsPage {

    async assertResultTableTitle(expectedTitle) {
        return expect(element(by.id('sc-treeviewtable-title')).getText()).toBe(expectedTitle);
    }

    async assertNumberOfResultRows(expectedNumber) {
        return expect($$('#treeviewtable tbody tr').count()).toBe(expectedNumber);
    }

    async assertNoResultsShown() {
        await expect(element(by.id('sc-search-no-results-message')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('sc-search-results-table')).isDisplayed()).toBeFalsy();
    }

    async openFirstScenarioAndClickStep() {
        const image = element(by.id('img_1'));
        await Utils.waitForElementVisible(image);
        await image.click();
        return $('#node_2 span').click();
    }

    async clickIncludeHtml() {
        return element(by.id('sc-search-include-html')).click();
    }

}

export default new SearchResultsPage();
