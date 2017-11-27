'use strict';
import {by, element} from "protractor";

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util');

function SearchResultsPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }
}

util.inherits(SearchResultsPage, BaseWebPage);

SearchResultsPage.prototype.enterSearchTerm = function(searchTerm) {
    this.searchBoxTextField.clear();
    this.searchBox.sendKeys(searchTerm);
};

SearchResultsPage.prototype.clickSearchButton = function() {
    this.searchBoxButton.click();
};

SearchResultsPage.prototype.assertResultTableTitle = function(expectedTitle) {
    expect(element(by.id('sc-treeviewtable-title')).getText()).toBe(expectedTitle);
};

SearchResultsPage.prototype.assertNumberOfResultRows = function(expectedNumber) {
    expect(element.all(by.css('#treeviewtable tbody tr')).count()).toBe(expectedNumber);
};

SearchResultsPage.prototype.assertNoResultsShown = function() {
    expect(element(by.id('sc-search-no-results-message')).isDisplayed()).toBeTruthy();
    expect(element(by.id('sc-search-results-table')).isDisplayed()).toBeFalsy();
};

SearchResultsPage.prototype.openFirstScenarioAndClickStep = function() {
    element(by.id('img_1')).click();
    element(by.css('#node_2 span')).click();
};

SearchResultsPage.prototype.clickIncludeHtml = function() {
    element(by.id('sc-search-include-html')).click();
};

module.exports = SearchResultsPage;
