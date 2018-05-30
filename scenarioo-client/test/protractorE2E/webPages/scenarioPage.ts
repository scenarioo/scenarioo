'use strict';

import { by, element, ElementFinder } from 'protractor';

export default class ScenarioPage {

    private static stepView: ElementFinder = element(by.css('div.step-view'));
    private static expandAllButton: ElementFinder = element(by.id('expandAllPages'));
    private static collapseAllButton: ElementFinder =  element(by.id('collapseAllPages'));

    static async openStepByName(stepName) {
        return this.stepView.element(by.linkText(stepName)).click();
    }

    static async expandAllPages() {
        return this.expandAllButton.click();
    }

    static async toggleShowAllStepsOfPage(pageIndex) {
        const elements = this.stepView.all(by.css('.toggle-show-all-steps-of-page'));
        return elements.get(pageIndex).click();
    }

    static async assertFirstChangedPageDiffIconHasValue() {
        return expect(element(by.css('.step-view div:first-child .sc-step-in-overview .sc-scenario-page-title .diff-info-wrapper span')).getText()).toContain('%');
    }

    static async assertFirstChangedStepDiffIconHasValue() {
        return expect(element(by.css('.step-view div:first-child .sc-step-in-overview:first-child .step-title span')).getText()).toContain('%');
    }

    static async assertAddedPageDiffIconTextEqualsAdded() {
        return expect(element(by.css('div.sc-step-in-overview.added:first-of-type .sc-scenario-page-title span.added')).getText()).toContain('added');
    }

    static async assertAddedStepDiffIconTextEqualsAdded() {
        return expect(element(by.css('div.sc-step-in-overview.added:first-of-type .step-title:first-of-type span.added')).getText()).toContain('added');
    }

    static async assertRemovedPageDiffIconTextEqualsRemoved() {
        return expect(element(by.css('div.sc-step-in-overview.removed:first-of-type .sc-scenario-page-title span.removed')).getText()).toContain('removed');
    }

    static async assertRemovedStepDiffIconTextEqualsRemoved() {
        return expect(element(by.css('div.sc-step-in-overview.removed:first-of-type .step-title:first-of-type span.removed')).getText()).toContain('removed');
    }

    static async expectOnlyExpandAllButtonIsDisplayed() {
        await expect(this.expandAllButton.isDisplayed()).toBeTruthy();
        return expect(this.collapseAllButton.isDisplayed()).toBeFalsy();
    }

    static async expectOnlyCollapseAllButtonIsDisplayed() {
        await expect(this.expandAllButton.isDisplayed()).toBeFalsy();
        return expect(this.collapseAllButton.isDisplayed()).toBeTruthy();
    }

    static async expectExpandAllAndCollapseAllButtonBothDisplayed() {
        await expect(this.expandAllButton.isDisplayed()).toBeTruthy();
        return expect(this.collapseAllButton.isDisplayed()).toBeTruthy();
    }

    static async assertNoDiffInfoDisplayed() {
        return expect(element(by.css('.sc-container .diff-info-wrapper')).isPresent()).toBeFalsy();
    }

}
