'use strict';

var BaseWebPage = require('./baseWebPage.js'),
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


module.exports = NavigationPage;
