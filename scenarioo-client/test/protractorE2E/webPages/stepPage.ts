'use strict';

import {browser, by, element} from 'protractor';
import * as Utils from '../util/util';

export default class StepPage {

    static async assertPreviousStepIsDisabled() {
        return Utils.assertElementIsDisabled('prevStepBtn');
    }

    static async assertPreviousPageIsDisabled() {
        return Utils.assertElementIsDisabled('prevPageBtn');
    }

    static async assertNextStepIsDisabled() {
        return Utils.assertElementIsDisabled('nextStepBtn');
    }

    static async assertNextPageIsDisabled() {
        return Utils.assertElementIsDisabled('nextPageBtn');
    }

    static async assertPreviousStepIsEnabled() {
        return Utils.assertElementIsEnabled('prevStepBtn');
    }

    static async assertPreviousPageIsEnabled() {
        return Utils.assertElementIsEnabled('prevPageBtn');
    }

    static async assertNextStepIsEnabled() {
        return Utils.assertElementIsEnabled('nextStepBtn');
    }

    static async assertNextPageIsEnabled() {
        return Utils.assertElementIsEnabled('nextPageBtn');
    }

    static async goToNextStep() {
        return Utils.clickElementById('nextStepBtn');
    }

    static async goToNextPage() {
        return Utils.clickElementById('nextPageBtn');
    }

    static async goToPreviousStep() {
        return Utils.clickElementById('prevStepBtn');
    }

    static async goToPreviousPage() {
        return Utils.clickElementById('prevPageBtn');
    }

    static async goToPreviousPageVariant() {
        return Utils.clickElementById('prevPageVariantBtn');
    }

    static async goToNextPageVariant() {
        return Utils.clickElementById('nextPageVariantBtn');
    }

    static async assertNextPageVariantButtonIsDisabled() {
        return Utils.assertElementIsDisabled('nextPageVariantBtn');
    }

    static async clickAllPageVariantsLink() {
        return Utils.clickElementById('allPageVariants');
    }

    static async assertErrorMessageIsShown() {
        await expect(element(by.id('stepNotFoundErrorMessage')).isDisplayed()).toBeTruthy();
        await expect(element(by.id('fallbackMessage')).isDisplayed()).toBeFalsy();
    }

    static async assertFallbackMessageIsShown() {
        await expect(element(by.id('fallbackMessage')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('stepNotFoundErrorMessage')).isDisplayed()).toBeFalsy();
    }

    static async assertFallbackMessageContainsText(text) {
        return expect(element(by.id('fallbackMessage')).getText()).toContain(text);
    }

    static async assertScenarioLabelsContain(label) {
        await this.openMetadataTabIfClosed('labels');
        return expect(element(by.id('scenario-labels')).getText()).toContain(label);
    }

    static async clickShareThisPageLink() {
        return element(by.id('shareThisPageLink')).click();
    }

    static async clickHtmlTabButton() {
        return element(by.id('html-tab')).click();
    }

    static async clickScreenshotTabButton() {
        return element(by.id('screenshot-tab')).click();
    }

    static async assertHtmlTabIsHidden() {
        return expect(element(by.id('html-tab')).isDisplayed()).toBe(false);
    }

    static async assertHtmlSourceEquals(expected) {
        return expect(element(by.id('html-source')).getText()).toBe(expected);
    }

    static async assertStepLinksDialogVisible() {
        return browser.wait(async function () {
            return await element(by.id('stepLinksDialog')).isDisplayed();
        }, 10000);
    }

    static async assertPageVariantIndicatorValue(value) {
        const pageVariantIndicator = element(by.id('pageVariantIndicator'));
        await expect(pageVariantIndicator.isDisplayed()).toBeTruthy();
        return expect(pageVariantIndicator.getText()).toBe(value);
    }

    static async openMetadataTabIfClosed(index) {
        const metadataPanelContentCss = '#metadata_panel_' + index + ' .metadata';
        const displayed = await browser.findElement(by.css(metadataPanelContentCss)).isDisplayed();
        if (!displayed) {
            return element(by.id('collapsable_panel_' + index)).click();
        }
    }

    static async clickOnLink(linkId) {
        return element(by.id(linkId)).click();
    }

    static async assertToolTipInBreadcrumb(expectedTooltip) {
        const toolTip = await element(by.id('tooltip_1')).getAttribute('uib-tooltip');
        await expect(toolTip).toBe(expectedTooltip);
    }

    static async assertScreenshotIsShown() {
        await expect(element.all(by.className('sc-real-screenshot')).count()).toBe(1);
        return expect(element(by.className('sc-real-screenshot')).isDisplayed()).toBeTruthy();
    }

    static async assertNoScreenAnnotationsArePresent() {
        await expect(element(by.className('sc-screenshot-annotation')).isPresent()).toBeFalsy();
        return expect(element(by.id('sc-showHideScreenAnnotationsButton')).isDisplayed()).toBeFalsy();
    }

    static async assertNoScreenAnnotationsAreVisible() {
        return expect(element.all(by.className('sc-screenshot-annotation')).isDisplayed()).toEqual([false, false]);
    }

    static async assertNumberOfVisibleScreenAnnotationsIs(expectedNumberOfScreenAnnotations) {
        await expect(element.all(by.className('sc-screenshot-annotation')).count()).toBe(expectedNumberOfScreenAnnotations);
        return element.all(by.className('sc-screenshot-annotation')).each(async function (element) {
            await expect(element.isDisplayed()).toBe(true);
        });
    }

    static async clickShowScreenAnnotationsButton() {
        return element(by.id('sc-showHideScreenAnnotationsButton')).click();
    }

    static async clickFirstScreenAnnotation() {
        return element.all(by.className('sc-screenshot-annotation-icon')).first().click();
    }

    static async assertScreenAnnotationPopupIsDisplayed() {
        return expect(element(by.className('modal-content')).isDisplayed()).toBe(true);
    }

    static async assertTitleOfAnnotationPopupIs(expectedTitle) {
        return expect(element(by.className('modal-header')).getText()).toEqual(expectedTitle);
    }

    static async clickCreateSketchButton() {
        return element(by.id('sketchThis')).click();
    }

    static async openComparisonTab() {
        return element(by.id('comparison-tab')).click();
    }

    static async toggleHighlights() {
        return element(by.id('sc-step-comparison-highlight-changes-button')).click();
    }

    static async expectHighlightsDisplayed() {
        await expect(element(by.id('sc-step-comparison-highlight-changes-button')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('sc-step-comparison-highlight-changes-button')).getAttribute('class')).toContain('active');
    }

    static async expectHighlightsHidden() {
        await expect(element(by.id('sc-step-comparison-highlight-changes-button')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('sc-step-comparison-highlight-changes-button')).getAttribute('class')).not.toContain('active');
    }

    static async showHighlights() {
        await this.toggleHighlights();
        return this.expectHighlightsDisplayed();
    }

    static async hideHighlights() {
        await this.toggleHighlights();
        return this.expectHighlightsHidden();
    }

    static async expectHighlightsButtonHidden() {
        return expect(element(by.id('sc-step-comparison-highlight-changes-button')).isPresent()).toBeFalsy();
    }

    static async showSideBySideView() {
        return element(by.id('sc-step-comparison-side-by-side-view-button')).click();
    }

    static async showComparisonCurrentScreenView() {
        return element(by.id('sc-step-comparison-current-screen-view-button')).click();
    }

    static async switchComparisonSingleScreens() {
        return element(by.id('sc-step-comparison-switch-screens-button')).click();
    }

    static async expectSwitchComparisonSingleScreensButtonDisabled() {
        return expect(element(by.id('sc-step-comparison-switch-screens-button')).getAttribute('disabled')).toEqual('true');
    }

    static async expectSwitchComparisonSingleScreensButtonEnabled() {
        return expect(element(by.id('sc-step-comparison-switch-screens-button')).getAttribute('disabled')).toBeFalsy();
    }

    static async expectStepComparisonLegendText(legendText) {
        return expect(element(by.id('sc-step-comparison-legend-info-text')).getText()).toContain(legendText);
    }

    static async assertNoDiffInfoDisplayed() {
        return expect(element(by.id('comparison-tab')).isPresent()).toBeFalsy();
    }

    static async assertStepComparisonSideBySideViewIsActive() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeTruthy();
        return expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeFalsy();
    }

    static async assertStepComparisonCurrentScreenViewIsActive() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeFalsy();
        await expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeTruthy();
        await expect(element(by.css('.sc-step-comparison-current-screenshot')).isPresent()).toBeTruthy();
        return expect(element(by.css('.sc-step-comparison-other-screenshot')).isPresent()).toBeFalsy();
    }

    static async assertStepComparisonOtherScreenViewIsActive() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeFalsy();
        await expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeTruthy();
        await expect(element(by.css('.sc-step-comparison-current-screenshot')).isPresent()).toBeFalsy();
        return expect(element(by.css('.sc-step-comparison-other-screenshot')).isPresent()).toBeTruthy();
    };

    static async expectStepComparisonCurrentScreenTitle(title: string, infoText: string) {
        await expect(element(by.css('sc-screenshot-title[build="baseBuild"] h3')).getText()).toBe(title);
        await element(by.css('sc-screenshot-title[build="baseBuild"] h3 i.icon-info-sign')).click();
        await expect(element(by.css('sc-screenshot-title[build="baseBuild"] div.tooltip')).isDisplayed()).toBeTruthy();
        await expect(element(by.css('sc-screenshot-title[build="baseBuild"] div.tooltip')).getText()).toBe(infoText);
        return element(by.css('sc-screenshot-title[build="baseBuild"] h3')).click();
    }

    static async expectStepComparisonOtherScreenTitle(title: string, infoText: string) {
        await expect(element(by.css('sc-screenshot-title[build="comparisonBuild"] h3')).getText()).toBe(title);
        await element(by.css('sc-screenshot-title[build="comparisonBuild"] h3 i.icon-info-sign')).click();
        await expect(element(by.css('sc-screenshot-title[build="comparisonBuild"] div.tooltip')).isDisplayed()).toBeTruthy();
        await expect(element(by.css('sc-screenshot-title[build="comparisonBuild"] div.tooltip')).getText()).toBe(infoText);
        return element(by.css('sc-screenshot-title[build="comparisonBuild"] h3')).click();
    };

    static async expectStepComparisonOtherScreenViewIsDisabled() {
        return expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('disabled')).toEqual('true');
    }

    static async assertStepNoComparisonScreenshot() {
        return expect(element(by.css('.sc-step-comparison-other-screenshot img')).isPresent()).toBeFalsy();
    }

    static async assertStepComparisonScreenshotSrcEquals(expected) {
        return expect(element(by.css('.sc-step-comparison-other-screenshot img.sc-real-screenshot')).getAttribute('src')).toContain(expected);
    }

    static async assertStepBaseScreenshotSrcEquals(expected) {
        return expect(element(by.css('.sc-step-comparison-current-screenshot img.sc-real-screenshot')).getAttribute('src')).toContain(expected);
    }

}
