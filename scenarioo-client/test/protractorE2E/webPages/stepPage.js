'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function StepPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.stepNavigation = element(by.css('div.step-navigation'));
}

util.inherits(StepPage, BaseWebPage);

// DISABLED
StepPage.prototype.assertPreviousStepIsDisabled = function () {
    this.assertElementIsDisabled('prevStepBtn');
};

StepPage.prototype.assertPreviousPageIsDisabled = function () {
    this.assertElementIsDisabled('prevPageBtn');
};

StepPage.prototype.assertNextStepIsDisabled = function () {
    this.assertElementIsDisabled('nextStepBtn');
};

StepPage.prototype.assertNextPageIsDisabled = function () {
    this.assertElementIsDisabled('nextPageBtn');
};

// ENABLED
StepPage.prototype.assertPreviousStepIsEnabled = function () {
    this.assertElementIsEnabled('prevStepBtn');
};

StepPage.prototype.assertPreviousPageIsEnabled = function () {
    this.assertElementIsEnabled('prevPageBtn');
};

StepPage.prototype.assertNextStepIsEnabled = function () {
    this.assertElementIsEnabled('nextStepBtn');
};

StepPage.prototype.assertNextPageIsEnabled = function () {
    this.assertElementIsEnabled('nextPageBtn');
};

StepPage.prototype.goToNextStep = function () {
    this.stepNavigation.findElement(by.id('nextStepBtn')).then(function (element) {
        element.click();
    });
};

StepPage.prototype.goToNextPage = function () {
    this.stepNavigation.findElement(by.id('nextPageBtn')).then(function (element) {
        element.click();
    });
};

StepPage.prototype.goToPreviousStep = function () {
    this.stepNavigation.findElement(by.id('prevStepBtn')).then(function (element) {
        element.click();
    });
};

StepPage.prototype.goToPreviousPage = function () {
    this.stepNavigation.findElement(by.id('prevPageBtn')).then(function (element) {
        element.click();
    });
};

StepPage.prototype.assertErrorMessageIsShown = function () {
    expect(element(by.id('stepNotFoundErrorMessage')).isDisplayed()).toBeTruthy();
};

StepPage.prototype.clickShowStepLinksButton = function() {
    element(by.id('showStepLinksButton')).click();
};

StepPage.prototype.assertStepLinksDialogVisible = function() {
    expect(element(by.id('stepLinksDialog')).isDisplayed()).toBeTruthy();
}

module.exports = StepPage;
