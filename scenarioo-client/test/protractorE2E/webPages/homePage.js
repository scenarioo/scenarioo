'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util'),
    e2eUtils = require('../util/util.js');

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
    this.sketchesTab = element(by.id('sc-main-tab-sketches'));
    this.pagesTab = element(by.id('sc-main-tab-pages'));
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

HomePage.prototype.selectSketchesTab = function() {
    this.sketchesTab.click();
};

HomePage.prototype.assertSketchesListContainsEntryWithSketchName = function(sketchName) {
    e2eUtils.assertTextPresentInElement(element(by.id('sc-sketches-list')), sketchName);
};

HomePage.prototype.selectPagesTab = function() {
    this.pagesTab.click();
};

HomePage.prototype.assertPagesTabContainsPage = function(pageName) {
    e2eUtils.assertTextPresentInElement(element(by.id('treeviewtable')), pageName);
};

HomePage.prototype.filterPages = function (filterQuery) {
    var pagesSearchField = element(by.id('pagesSearchField'));
    pagesSearchField.clear();
    pagesSearchField.sendKeys(filterQuery);
};

HomePage.prototype.assertCustomTabEntriesShown = function (count) {
    element(by.id('treeviewtable')).all(by.css('tr')).filter(function(e) {
        return e.isDisplayed();
    }).then(function (elements) {
        // Not a great workaround, at the moment the filterable table header column is also a normal tr
        expect(elements.length).toBe(count + 1);
    });
};

module.exports = HomePage;
