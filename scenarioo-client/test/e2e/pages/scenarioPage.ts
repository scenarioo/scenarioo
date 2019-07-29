'use strict';

import { by, element, ElementFinder, $ } from 'protractor';

class ScenarioPage {

    private stepView = $('div.steps-overview');
    private expandAllButton = element(by.id('expandAllPages'));
    private collapseAllButton =  element(by.id('collapseAllPages'));

    async openStepByName(stepName) {
        return this.stepView.element(by.linkText(stepName)).click();
    }

    async expandAllPages() {
        return this.expandAllButton.click();
    }

    async toggleShowAllStepsOfPage(pageIndex) {
        const elements = this.stepView.all(by.css('.toggle-show-all-steps-of-page'));
        return elements.get(pageIndex).click();
    }

    async assertFirstChangedPageDiffIconHasValue() {
        return expect($('.steps-overview div:first-child .sc-step-in-overview .sc-scenario-page-title .diff-info-wrapper span').getText()).toContain('%');
    }

    async assertFirstChangedStepDiffIconHasValue() {
        return expect($('.steps-overview div:first-child .sc-step-in-overview:first-child .step-title span').getText()).toContain('%');
    }

    async assertAddedPageDiffIconTextEqualsAdded() {
        return expect($('div.sc-step-in-overview.added:first-of-type .sc-scenario-page-title span.added').getText()).toContain('added');
    }

    async assertAddedStepDiffIconTextEqualsAdded() {
        return expect($('div.sc-step-in-overview.added:first-of-type .step-title:first-of-type span.added').getText()).toContain('added');
    }

    async assertRemovedPageDiffIconTextEqualsRemoved() {
        return expect($('div.sc-step-in-overview.removed:first-of-type .sc-scenario-page-title span.removed').getText()).toContain('removed');
    }

    async assertRemovedStepDiffIconTextEqualsRemoved() {
        return expect($('div.sc-step-in-overview.removed:first-of-type .step-title:first-of-type span.removed').getText()).toContain('removed');
    }

    async expectOnlyExpandAllButtonIsDisplayed() {
        await expect(this.expandAllButton.isDisplayed()).toBeTruthy();
        return expect(this.collapseAllButton.isDisplayed()).toBeFalsy();
    }

    async expectOnlyCollapseAllButtonIsDisplayed() {
        await expect(this.expandAllButton.isDisplayed()).toBeFalsy();
        return expect(this.collapseAllButton.isDisplayed()).toBeTruthy();
    }

    async expectExpandAllAndCollapseAllButtonBothDisplayed() {
        await expect(this.expandAllButton.isDisplayed()).toBeTruthy();
        return expect(this.collapseAllButton.isDisplayed()).toBeTruthy();
    }

    async assertNoDiffInfoDisplayed() {
        return expect($('.sc-container .diff-info-wrapper').isPresent()).toBeFalsy();
    }

}

export default new ScenarioPage();
