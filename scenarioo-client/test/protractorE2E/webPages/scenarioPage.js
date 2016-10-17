'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util');

function ScenarioPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.stepView = element(by.css('div.step-view'));
}

function expandAllButton() {
    return element(by.id('expandAllPages'));
}

function collapseAllButton() {
    return element(by.id('collapseAllPages'));
}


util.inherits(ScenarioPage, BaseWebPage);

ScenarioPage.prototype.openStepByName = function (stepName) {
    this.stepView.element(by.linkText(stepName)).click();
};

ScenarioPage.prototype.expandAllPages = function () {
    expandAllButton().click();
};

ScenarioPage.prototype.toggleShowAllStepsOfPage = function (pageIndex) {
    this.stepView.all(by.css('.toggle-show-all-steps-of-page')).then(function(elements) {
        elements[pageIndex].click();
    });
};


ScenarioPage.prototype.assertFirstChangedPageDiffIconHasValue = function () {
    expect(element(by.css('.step-view div:first-child .sc-step-in-overview .sc-scenario-page-title .diff-info-wrapper span')).getText()).toContain('%');
};

ScenarioPage.prototype.assertFirstChangedStepDiffIconHasValue = function () {
    expect(element(by.css('.step-view div:first-child .sc-step-in-overview:first-child .step-title span')).getText()).toContain('%');
};

ScenarioPage.prototype.assertAddedPageDiffIconTextEqualsAdded = function () {
    expect(element(by.css('div.sc-step-in-overview.added:first-of-type .sc-scenario-page-title span.added')).getText()).toContain('added');
};

ScenarioPage.prototype.assertAddedStepDiffIconTextEqualsAdded = function () {
    expect(element(by.css('div.sc-step-in-overview.added:first-of-type .step-title:first-of-type span.added')).getText()).toContain('added');
};

ScenarioPage.prototype.assertRemovedPageDiffIconTextEqualsRemoved = function () {
    expect(element(by.css('div.sc-step-in-overview.removed:first-of-type .sc-scenario-page-title span.removed')).getText()).toContain('removed');
};

ScenarioPage.prototype.assertRemovedStepDiffIconTextEqualsRemoved = function () {
    expect(element(by.css('div.sc-step-in-overview.removed:first-of-type .step-title:first-of-type span.removed')).getText()).toContain('removed');
};

ScenarioPage.prototype.expectOnlyExpandAllButtonIsDisplayed = function () {
    expect(expandAllButton().isDisplayed()).toBeTruthy();
    expect(collapseAllButton().isDisplayed()).toBeFalsy();
};

ScenarioPage.prototype.expectOnlyCollapseAllButtonIsDisplayed = function () {
    expect(expandAllButton().isDisplayed()).toBeFalsy();
    expect(collapseAllButton().isDisplayed()).toBeTruthy();
};

ScenarioPage.prototype.expectExpandAllAndCollapseAllButtonBothDisplayed = function () {
    expect(expandAllButton().isDisplayed()).toBeTruthy();
    expect(collapseAllButton().isDisplayed()).toBeTruthy();
};

ScenarioPage.prototype.assertNoDiffInfoDisplayed = function () {
    expect(element(by.css('.sc-container .diff-info-wrapper')).isPresent()).toBeFalsy();
};

module.exports = ScenarioPage;
