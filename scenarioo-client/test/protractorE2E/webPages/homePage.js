'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function HomePage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.useCasesSearchField = element(by.id('useCasesSearchField'));
    this.aboutScenariooPopup = element(by.css('.modal.about-popup'));
    this.popupCloseButton = element(by.css('.modal-footer button.btn'));
    this.stepView = element(by.css('table.usecase-table'));
    this.showMetaDataButton = element(by.id('sc-showHideDetailsButton-show'));
    this.hideMetaDataButton = element(by.id('sc-showHideDetailsButton-hide'));
    this.metaDataPanel = element(by.id('sc-metadata-panel'));
    this.issuesTab = element(by.repeater('tab in tabs').row(3));

}

util.inherits(HomePage, BaseWebPage);


HomePage.prototype.assertPageIsDisplayed = function () {
    // call assertPageIsDisplayed on BaseWebPage
    BaseWebPage.prototype.assertPageIsDisplayed.apply(this);
    expect(this.useCasesSearchField.isDisplayed()).toBe(true);
};

HomePage.prototype.assertScenariooInfoDialogShown = function () {
    expect(this.aboutScenariooPopup.isDisplayed()).toBe(true);
    expect(this.popupCloseButton.isDisplayed()).toBe(true);
};

HomePage.prototype.assertScenariooInfoDialogNotShown = function () {
    e2eUtils.assertElementNotPresentInDom(by.css('.modal-dialog.about-popup'));
};

HomePage.prototype.closeScenariooInfoDialogIfOpen = function () {
    browser.isElementPresent(by.css('.modal-footer button.btn')).then(function(present){
        if(present) {
            element(by.css('.modal-footer button.btn')).click();
        }
    });
};

HomePage.prototype.filterUseCases = function (filterQuery) {
    this.useCasesSearchField.clear();
    this.useCasesSearchField.sendKeys(filterQuery);
};

HomePage.prototype.assertUseCasesShown = function (count) {
    this.stepView.all(by.css('tbody tr')).then(function (elements) {
        expect(elements.length).toBe(count);
    });
};

HomePage.prototype.selectUseCase = function(useCaseIndex) {
    this.stepView.all(by.css('tbody tr')).then(function(elements) {
        elements[useCaseIndex].click();
    });
};

HomePage.prototype.showMetaData = function() {
    this.showMetaDataButton.click();
};

HomePage.prototype.assertMetaDataShown = function() {
    expect(this.metaDataPanel.isDisplayed()).toBe(true);
};

HomePage.prototype.assertMetaDataHidden = function() {
    expect(this.metaDataPanel.isDisplayed()).toBe(false);
};

HomePage.prototype.hideMetaData = function() {
    this.hideMetaDataButton.click();
};

HomePage.prototype.selectIssuesTab = function() {
    //var issuesTab = document.querySelector('heading=Issues');
    this.issuesTab.click();
};

module.exports = HomePage;
