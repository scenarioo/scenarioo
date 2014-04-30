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
    this.popupCloseButton = element(by.css('.modal.about-popup .modal-footer .btn-primary'));
    this.stepView = element(by.css('table.usecase-table'));

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

HomePage.prototype.closeScenariooInfoDialog = function () {
    this.popupCloseButton.click();
};

HomePage.prototype.filterUseCases = function (filterQuery) {
    this.useCasesSearchField.clear();
    this.useCasesSearchField.sendKeys(filterQuery);
};

HomePage.prototype.assertUseCasesShown = function (count) {
    this.stepView.findElements(by.css('tbody tr')).then(function (elements) {
        expect(elements.length).toBe(count);
    });
};

HomePage.prototype.selectUseCase = function(useCaseIndex) {
    this.stepView.findElements(by.css('tbody tr')).then(function(elements) {
        elements[useCaseIndex].click();
    });

} ;

module.exports = HomePage;