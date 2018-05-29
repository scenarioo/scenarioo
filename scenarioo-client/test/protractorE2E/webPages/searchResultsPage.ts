'use strict';

import { by, element, ElementFinder } from "protractor";
import * as Utils from "../util/util";

export default class SearchResultsPage {

    private path: string = "/";
    private static searchBox: ElementFinder = element(by.id('sc-global-search-box'));
    private static searchBoxTextField: ElementFinder = element(by.id('sc-global-search-box-textfield'));

    async enterSearchTerm(searchTerm) {
        // TODO: fix
        // this.searchBoxTextField.clear();
        // this.searchBox.sendKeys(searchTerm);
    };

    async clickSearchButton() {
        // TODO: fix
        // this.searchBoxButton.click();
    };

    async assertResultTableTitle(expectedTitle) {
        expect(element(by.id('sc-treeviewtable-title')).getText()).toBe(expectedTitle);
    };

    async assertNumberOfResultRows(expectedNumber) {
        expect(element.all(by.css('#treeviewtable tbody tr')).count()).toBe(expectedNumber);
    };

    async assertNoResultsShown() {
        expect(element(by.id('sc-search-no-results-message')).isDisplayed()).toBeTruthy();
        expect(element(by.id('sc-search-results-table')).isDisplayed()).toBeFalsy();
    };

    async openFirstScenarioAndClickStep() {
        var image = element(by.id('img_1'));
        Utils.waitForElementVisible(image);
        image.click();
        element(by.css('#node_2 span')).click();
    };

    async clickIncludeHtml() {
        element(by.id('sc-search-include-html')).click();
    };

}
