'use strict';

import {$, browser, by, element} from 'protractor';
import * as Utils from '../util';

class StepPage {

    async assertPreviousStepIsDisabled() {
        return expect(element(by.id('prevStepBtn')).getAttribute('class')).toContain('step-arrow-grey');
    }

    async assertPreviousPageIsDisabled() {
        return expect(element(by.id('prevPageBtn')).getAttribute('class')).toContain('page-arrow-grey');
    }

    async assertNextStepIsDisabled() {
        return expect(element(by.id('nextStepBtn')).getAttribute('class')).toContain('step-arrow-grey');
    }

    async assertNextPageIsDisabled() {
        return expect(element(by.id('nextPageBtn')).getAttribute('class')).toContain('page-arrow-grey');
    }

    async assertNextPageVariantButtonIsDisabled() {
        return expect(element(by.id('nextPageVariantBtn')).getAttribute('class')).toContain('page-arrow-grey');
    }

    async assertPreviousStepIsEnabled() {
        return Utils.assertElementIsEnabled('prevStepBtn');
    }

    async assertPreviousPageIsEnabled() {
        return Utils.assertElementIsEnabled('prevPageBtn');
    }

    async assertNextStepIsEnabled() {
        return Utils.assertElementIsEnabled('nextStepBtn');
    }

    async assertNextPageIsEnabled() {
        return Utils.assertElementIsEnabled('nextPageBtn');
    }

    async goToNextStep() {
        return Utils.clickElementById('nextStepBtn');
    }

    async goToNextPage() {
        return Utils.clickElementById('nextPageBtn');
    }

    async goToPreviousStep() {
        return Utils.clickElementById('prevStepBtn');
    }

    async goToPreviousPage() {
        return Utils.clickElementById('prevPageBtn');
    }

    async goToPreviousPageVariant() {
        return Utils.clickElementById('prevPageVariantBtn');
    }

    async goToNextPageVariant() {
        return Utils.clickElementById('nextPageVariantBtn');
    }

    async clickAllPageVariantsLink() {
        return Utils.clickElementById('pageVariantIndicator');
    }

    async assertErrorMessageIsShown() {
        await expect(element(by.id('stepNotFoundErrorMessage')).isDisplayed()).toBeTruthy();
        await expect(element(by.id('fallbackMessage')).isDisplayed()).toBeFalsy();
    }

    async assertErrorResponseIsShown(expectedCurrentUrl: string, expectedResponseMethod: string, expectedResponseStatus: string) {
        await expect(element(by.id('stepNotFoundErrorCurrentUrl')).getText()).toContain(expectedCurrentUrl);
        await expect(element(by.id('stepNotFoundErrorResponseMethod')).getText()).toEqual(expectedResponseMethod);
        await expect(element(by.id('stepNotFoundErrorResponseStatus')).getText()).toEqual(expectedResponseStatus);
    }

    async assertFallbackMessageIsShown() {
        await expect(element(by.id('fallbackMessage')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('stepNotFoundErrorMessage')).isDisplayed()).toBeFalsy();
    }

    async assertFallbackMessageContainsText(text) {
        return expect(element(by.id('fallbackMessage')).getText()).toContain(text);
    }

    async assertScenarioLabelsContain(label) {
        await this.openMetadataTabIfClosed('labels');
        return expect(element(by.id('scenario-labels')).getText()).toContain(label);
    }

    async clickShareThisPageLink() {
        return element(by.id('shareButton')).click();
    }

    async clickHtmlTabButton() {
        return element(by.id('html-tab')).click();
    }

    async clickScreenshotTabButton() {
        return element(by.id('screenshot-tab')).click();
    }

    async assertHtmlTabIsHidden() {
        return expect(element(by.id('html-tab')).isDisplayed()).toBe(false);
    }

    async assertHtmlSourceEquals(expected) {
        return expect(element(by.id('html-source')).getText()).toBe(expected);
    }

    async assertStepLinksDialogVisible() {
        return browser.wait(async () => {
            return await element(by.id('stepLinksDialog')).isDisplayed();
        }, 10000);
    }

    async assertPageVariantIndicatorValue(value) {
        const pageVariantIndicator = element(by.id('pageVariantIndicator'));
        await expect(pageVariantIndicator.isDisplayed()).toBeTruthy();
        return expect(pageVariantIndicator.getText()).toBe(value);
    }

    async openMetadataTabIfClosed(index) {
        const metadataPanelContentCss = '#metadata_panel_' + index + ' .metadata';
        const displayed = await $(metadataPanelContentCss).isDisplayed();
        if (!displayed) {
            return element(by.id('collapsable_panel_' + index)).click();
        }
    }

    async clickOnLink(linkId) {
        return element(by.id(linkId)).click();
    }

    async assertToolTipInBreadcrumb(expectedTooltip) {
        const toolTip = await element(by.id('tooltip_1')).getAttribute('data-tooltip-text');
        await expect(toolTip).toBe(expectedTooltip);
    }

    async assertScreenshotIsShown() {
        await expect(element.all(by.className('sc-real-screenshot')).count()).toBe(1);
        return expect(element(by.className('sc-real-screenshot')).isDisplayed()).toBeTruthy();
    }

    async assertNoScreenAnnotationsArePresent() {
        await expect(element(by.className('sc-screenshot-annotation')).isPresent()).toBeFalsy();
        return expect(element(by.id('sc-showHideScreenAnnotationsButton')).isDisplayed()).toBeFalsy();
    }

    async assertNoScreenAnnotationsAreVisible() {
        return expect(element.all(by.className('sc-screenshot-annotation')).isDisplayed()).toEqual([false, false]);
    }

    async assertNumberOfVisibleScreenAnnotationsIs(expectedNumberOfScreenAnnotations) {
        await expect(element.all(by.className('sc-screenshot-annotation')).count()).toBe(expectedNumberOfScreenAnnotations);
        return element.all(by.className('sc-screenshot-annotation')).each(async (e) => {
            await expect(e.isDisplayed()).toBe(true);
        });
    }

    async clickShowScreenAnnotationsButton() {
        return element(by.id('sc-showHideScreenAnnotationsButton')).click();
    }

    async clickLastScreenAnnotation() {
        return element.all(by.className('sc-screenshot-annotation-icon')).last().click();
    }

    async assertScreenAnnotationPopupIsDisplayed() {
        return expect(element(by.className('modal-content')).isDisplayed()).toBe(true);
    }

    async assertTitleOfAnnotationPopupIs(expectedTitle) {
        return expect(element(by.className('modal-header')).getText()).toEqual(expectedTitle);
    }

    async clickGoToNextStepInAnnotationPopup() {
        return element(by.className('link-no-hover')).click();
    }

    async clickCreateSketchButton() {
        return element(by.id('sketchThis')).click();
    }

    async openComparisonTab() {
        return element(by.id('comparison-tab')).click();
    }

    async toggleHighlights() {
        return element(by.id('sc-step-comparison-highlight-changes-button')).click();
    }

    async expectHighlightsDisplayed() {
        await expect(element(by.id('sc-step-comparison-highlight-changes-button')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('sc-step-comparison-highlight-changes-button')).getAttribute('class')).toContain('active');
    }

    async expectHighlightsHidden() {
        await expect(element(by.id('sc-step-comparison-highlight-changes-button')).isDisplayed()).toBeTruthy();
        return expect(element(by.id('sc-step-comparison-highlight-changes-button')).getAttribute('class')).not.toContain('active');
    }

    async showHighlights() {
        await this.toggleHighlights();
        return this.expectHighlightsDisplayed();
    }

    async hideHighlights() {
        await this.toggleHighlights();
        return this.expectHighlightsHidden();
    }

    async expectHighlightsButtonHidden() {
        return expect(element(by.id('sc-step-comparison-highlight-changes-button')).isPresent()).toBeFalsy();
    }

    async showSideBySideView() {
        return element(by.id('sc-step-comparison-side-by-side-view-button')).click();
    }

    async showComparisonCurrentScreenView() {
        return element(by.id('sc-step-comparison-current-screen-view-button')).click();
    }

    async switchComparisonSingleScreens() {
        return element(by.id('sc-step-comparison-switch-screens-button')).click();
    }

    async expectSwitchComparisonSingleScreensButtonHidden() {
        return expect(element(by.id('sc-step-comparison-switch-screens-button')).isPresent()).toBeFalsy();
    }

    async expectSwitchComparisonSingleScreensButtonEnabled() {
        return expect(element(by.id('sc-step-comparison-switch-screens-button')).getAttribute('disabled')).toBeFalsy();
    }

    async expectStepComparisonLegendText(legendText) {
        return expect(element(by.id('sc-step-comparison-legend-info-text')).getText()).toContain(legendText);
    }

    async assertNoDiffInfoDisplayed() {
        return expect(element(by.id('comparison-tab')).isPresent()).toBeFalsy();
    }

    async assertStepComparisonSideBySideViewIsActiveWithOtherScreenVisible() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeTruthy();
        return expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeFalsy();
    }

    async assertStepComparisonCurrentScreenViewIsActiveWithOtherScreenVisible() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeFalsy();
        await expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeTruthy();
        await expect($('.sc-step-comparison-current-screenshot').isPresent()).toBeTruthy();
        return expect($('.sc-step-comparison-other-screenshot').isPresent()).toBeFalsy();
    }

    async assertStepComparisonSideBySideViewIsActiveWithOtherScreenNotVisible() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeTruthy();
        return expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeFalsy();
    }

    async assertStepComparisonCurrentScreenViewIsActiveWithOtherScreenNotVisible() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeFalsy();
        await expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeTruthy();
        await expect($('.sc-step-comparison-current-screenshot').isPresent()).toBeTruthy();
        return expect($('.sc-step-comparison-other-screenshot').isPresent()).toBeFalsy();
    }

    async assertStepComparisonOtherScreenViewIsActive() {
        await expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).not.toContain('active');
        await expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).toContain('active');
        await expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeFalsy();
        await expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeTruthy();
        await expect($('.sc-step-comparison-current-screenshot').isPresent()).toBeFalsy();
        return expect($('.sc-step-comparison-other-screenshot').isPresent()).toBeTruthy();
    }

    async expectStepComparisonCurrentScreenTitle(title: string, infoText: string) {
        await expect($('sc-screenshot-title[build="$ctrl.baseBuild"] h3').getText()).toBe(title);
        await $('sc-screenshot-title[build="$ctrl.baseBuild"] h3 span[uib-tooltip]').click();
        await expect($('sc-screenshot-title[build="$ctrl.baseBuild"] div.tooltip').isDisplayed()).toBeTruthy();
        await expect($('sc-screenshot-title[build="$ctrl.baseBuild"] div.tooltip').getText()).toBe(infoText);
        return $('sc-screenshot-title[build="$ctrl.baseBuild"] h3').click();
    }

    async expectStepComparisonOtherScreenTitle(title: string, infoText: string) {
        await expect($('sc-screenshot-title[build="$ctrl.comparisonBuild"] h3').getText()).toBe(title);
        await $('sc-screenshot-title[build="$ctrl.comparisonBuild"] h3 span[uib-tooltip]').click();
        await expect($('sc-screenshot-title[build="$ctrl.comparisonBuild"] div.tooltip').isDisplayed()).toBeTruthy();
        await expect($('sc-screenshot-title[build="$ctrl.comparisonBuild"] div.tooltip').getText()).toBe(infoText);
        return $('sc-screenshot-title[build="$ctrl.comparisonBuild"] h3').click();
    }

    async expectStepComparisonCurrentScreenViewButtonHidden() {
        return expect(element(by.id('sc-step-comparison-other-screen-view-button')).isPresent()).toBeFalsy();
    }

    async expectStepComparisonOtherScreenViewButtonHidden() {
        return expect(element(by.id('sc-step-comparison-other-screen-view-button')).isPresent()).toBeFalsy();
    }

    async assertStepNoComparisonScreenshot() {
        return expect($('.sc-step-comparison-other-screenshot img').isPresent()).toBeFalsy();
    }

    async assertStepComparisonScreenshotSrcEquals(expected) {
        return expect($('.sc-step-comparison-other-screenshot img.sc-real-screenshot').getAttribute('src')).toContain(expected);
    }

    async assertStepBaseScreenshotSrcEquals(expected) {
        return expect($('.sc-step-comparison-current-screenshot img.sc-real-screenshot').getAttribute('src')).toContain(expected);
    }

    async assertPageLink(pageLink: string) {
        return expect($('#stepLinkField').getAttribute('value')).toBe(pageLink);
    }

    async assertScreenshotLink(screenshotLink: string) {
        return expect($('#screenshotLinkField').getAttribute('value')).toBe(screenshotLink);
    }
}

export default new StepPage();
