'use strict';

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

SearchResultsPage.prototype.assertResultTableTitle = function() {
    expect(element(by.id('sc-treeviewtable-title')).getText()).toBe('Search Results');
};

SearchResultsPage.prototype.assertNumberOfResultRows = function(expectedNumber) {
    expect(element.all(by.css('#treeviewtable tbody tr')).count()).toBe(expectedNumber);
};

SearchResultsPage.prototype.openFirstScenarioAndClickStep = function() {
    element(by.id('img_1')).click();
    element(by.css('#node_2 span')).click();
};

module.exports = SearchResultsPage;
