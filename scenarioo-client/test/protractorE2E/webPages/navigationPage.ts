'use strict';
import {by, element} from "protractor";

var BaseWebPage = require('./baseWebPage'),
    util = require('util');

function NavigationPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.searchBox = element(by.id('sc-global-search-box'));
    this.searchBoxTextField = element(by.id('sc-global-search-box-textfield'));
    this.searchBoxButton = element(by.id('sc-global-search-box-button'));
}

util.inherits(NavigationPage, BaseWebPage);

NavigationPage.prototype.enterSearchTerm = function(searchTerm) {
    this.searchBoxTextField.clear();
    this.searchBoxTextField.sendKeys(searchTerm);
};

NavigationPage.prototype.clickSearchButton = function() {
    this.searchBoxButton.click();
};

NavigationPage.prototype.chooseBranch = function (branchName) {
    // Open menu first, otherwise we cannot click
    element(by.partialLinkText('Branch:')).click();
    element(by.css('#branchSelectionDropdown .dropdown-menu')).all(by.partialLinkText(branchName)).first().click();
};

NavigationPage.prototype.chooseBuild = function (buildName) {
    // Open menu first, otherwise we cannot click
    element(by.partialLinkText('Build:')).click();
    element(by.css('#build-selection-dropdown .dropdown-menu')).all(by.partialLinkText(buildName)).first().click();
};

NavigationPage.prototype.chooseComparison = function (comparisonName) {
    // Open menu first, otherwise we cannot click
    element(by.partialLinkText('Comparison:')).click();
    element(by.css('#comparison-selection-dropdown .dropdown-menu')).all(by.partialLinkText(comparisonName)).first().click();
};

NavigationPage.prototype.disableComparison = function () {
    element(by.partialLinkText('Comparison:')).isPresent().then(function(isPresent) {
        // Open menu first, otherwise we cannot click
        element(by.partialLinkText('Comparison:')).click();
        var comparisonElements = element(by.css('#comparison-selection-dropdown .dropdown-menu'));
        var disableEntry = comparisonElements.element(by.partialLinkText('Disable'));
        // It's not there if it's already disabled
        disableEntry.isPresent().then(function(isPresent) {
            if(isPresent) {
                disableEntry.click();
            }
        });
    });
};

NavigationPage.prototype.assertSelectedComparison = function (comparisonName) {
    expect(element(by.css('#comparison-selection-dropdown > a')).getText()).toContain(comparisonName);
};


module.exports = NavigationPage;
