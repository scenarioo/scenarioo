'use strict';

import { by, element, ElementFinder } from "protractor";
import * as Utils from "../util/util";

export default class SearchResultsPage {

    private static searchBox: ElementFinder = element(by.id('sc-global-search-box'));
    private static searchBoxTextField: ElementFinder = element(by.id('sc-global-search-box-textfield'));
    private static searchBoxButton = element(by.id('sc-global-search-box-button'));

    static async enterSearchTerm(searchTerm) {
        await this.searchBoxTextField.clear();
        return this.searchBox.sendKeys(searchTerm);
    };

    static async clickSearchButton() {
        return this.searchBoxButton.click();
    };

    static async assertResultTableTitle(expectedTitle) {
        return expect(element(by.id('sc-treeviewtable-title')).getText()).toBe(expectedTitle);
    };

    static async assertNumberOfResultRows(expectedNumber) {
        return expect(element.all(by.css('#treeviewtable tbody tr')).count()).toBe(expectedNumber);
    };

    static async assertNoResultsShown() {
        await expect(element(by.id('sc-search-no-results-message')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('sc-search-results-table')).isDisplayed()).toBeFalsy();
    };

    static async openFirstScenarioAndClickStep() {
        const image = element(by.id('img_1'));
        await Utils.waitForElementVisible(image);
        await image.click();
        return element(by.css('#node_2 span')).click();
    };

    static async clickIncludeHtml() {
        return element(by.id('sc-search-include-html')).click();
    };

}
