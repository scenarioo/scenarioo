'use strict';
import {browser, by, element} from "protractor";

var BaseWebPage = require('./baseWebPage'),
    util = require('util');

function StepPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.stepNavigation = element(by.css('div.step-navigation'));
}

util.inherits(StepPage, BaseWebPage);

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
    this.clickElementById('nextStepBtn');
};

StepPage.prototype.goToNextPage = function () {
    this.clickElementById('nextPageBtn');
};

StepPage.prototype.goToPreviousStep = function () {
    this.clickElementById('prevStepBtn');
};

StepPage.prototype.goToPreviousPage = function () {
    this.clickElementById('prevPageBtn');
};

StepPage.prototype.goToPreviousPageVariant = function () {
    this.clickElementById('prevPageVariantBtn');
};

StepPage.prototype.goToNextPageVariant = function () {
    this.clickElementById('nextPageVariantBtn');
};

StepPage.prototype.assertNextPageVariantButtonIsDisabled = function () {
    this.assertElementIsDisabled('nextPageVariantBtn');
};

StepPage.prototype.clickAllPageVariantsLink = function () {
    this.clickElementById('allPageVariants');
};


StepPage.prototype.assertErrorMessageIsShown = function () {
    expect(element(by.id('stepNotFoundErrorMessage')).isDisplayed()).toBeTruthy();
    expect(element(by.id('fallbackMessage')).isDisplayed()).toBeFalsy();
};

StepPage.prototype.assertFallbackMessageIsShown = function () {
    expect(element(by.id('fallbackMessage')).isDisplayed()).toBeTruthy();
    expect(element(by.id('stepNotFoundErrorMessage')).isDisplayed()).toBeFalsy();
};

StepPage.prototype.assertFallbackMessageContainsText = function (text) {
    expect(element(by.id('fallbackMessage')).getText()).toContain(text);
};

StepPage.prototype.assertScenarioLabelsContain = function (label) {
    this.openMetadataTabIfClosed('labels');
    expect(element(by.id('scenario-labels')).getText()).toContain(label);
};

StepPage.prototype.clickShareThisPageLink = function () {
    element(by.id('shareThisPageLink')).click();
};

StepPage.prototype.clickHtmlTabButton = function () {
    element(by.id('html-tab')).click();
};

StepPage.prototype.clickScreenshotTabButton = function () {
    element(by.id('screenshot-tab')).click();
};

StepPage.prototype.assertHtmlTabIsHidden = function () {
    expect(element(by.id('html-tab')).isDisplayed()).toBe(false);
};

StepPage.prototype.assertHtmlSourceEquals = function (expected) {
    expect(element(by.id('html-source')).getText()).toBe(expected);
};

StepPage.prototype.assertStepLinksDialogVisible = function () {
    browser.wait(function () {
        return element(by.id('stepLinksDialog')).isDisplayed();
    }, 10000);
};

StepPage.prototype.assertPageVariantIndicatorValue = function (value) {
    var pageVariantIndicator = element(by.id('pageVariantIndicator'));
    expect(pageVariantIndicator.isDisplayed()).toBeTruthy();
    expect(pageVariantIndicator.getText()).toBe(value);
};

StepPage.prototype.openMetadataTabIfClosed = function (index) {
    var metadataPanelContentCss = '#metadata_panel_' + index + ' .metadata';

    browser.findElement(by.css(metadataPanelContentCss)).isDisplayed().then(function (displayed) {
        if (!displayed) {
            element(by.id('collapsable_panel_' + index)).click();
        }
    });
};

StepPage.prototype.clickOnLink = function (linkId) {
    element(by.id(linkId)).click();
};

StepPage.prototype.assertToolTipInBreadcrumb = function (expectedTooltip) {
    var toolTip = element(by.id('tooltip_1')).getAttribute('tooltip');
    expect(toolTip).toBe(expectedTooltip);
};

StepPage.prototype.assertScreenshotIsShown = function () {
    expect(element.all(by.className('sc-real-screenshot')).count()).toBe(1);
    expect(element(by.className('sc-real-screenshot')).isDisplayed()).toBeTruthy();
};

StepPage.prototype.assertNoScreenAnnotationsArePresent = function () {
    expect(element(by.className('sc-screenshot-annotation')).isPresent()).toBeFalsy();
    expect(element(by.id('sc-showHideScreenAnnotationsButton')).isDisplayed()).toBeFalsy();
};

StepPage.prototype.assertNoScreenAnnotationsAreVisible = function () {
    expect(element.all(by.className('sc-screenshot-annotation')).isDisplayed()).toEqual([false, false]);
};

StepPage.prototype.assertNumberOfVisibleScreenAnnotationsIs = function (expectedNumberOfScreenAnnotations) {
    expect(element.all(by.className('sc-screenshot-annotation')).count()).toBe(expectedNumberOfScreenAnnotations);
    element.all(by.className('sc-screenshot-annotation')).each(function (element) {
        expect(element.isDisplayed()).toBe(true);
    });
};

StepPage.prototype.clickShowScreenAnnotationsButton = function () {
    element(by.id('sc-showHideScreenAnnotationsButton')).click();
};

StepPage.prototype.clickFirstScreenAnnotation = function () {
    element.all(by.className('sc-screenshot-annotation-icon')).first().click();
};

StepPage.prototype.assertScreenAnnotationPopupIsDisplayed = function () {
    expect(element(by.className('modal-content')).isDisplayed()).toBe(true);
};

StepPage.prototype.assertTitleOfAnnotationPopupIs = function (expectedTitle) {
    expect(element(by.className('modal-header')).getText()).toEqual(expectedTitle);
};

StepPage.prototype.clickCreateSketchButton = function () {
    element(by.id('sketchThis')).click();
};

StepPage.prototype.assertHtmlSourceEquals = function (expected) {
    expect(element(by.id('html-source')).getText()).toBe(expected);
};

StepPage.prototype.openComparisonTab = function () {
    element(by.id('comparison-tab')).click();
};

StepPage.prototype.toggleHighlights = function () {
    element(by.id('sc-step-comparison-highlight-changes-button')).click();
};

StepPage.prototype.expectHighlightsDisplayed = function () {
    expect(element(by.id('sc-step-comparison-highlight-changes-button')).isDisplayed()).toBeTruthy();
    expect(element(by.id('sc-step-comparison-highlight-changes-button')).getAttribute('class')).toContain('active');
};

StepPage.prototype.expectHighlightsHidden = function () {
    expect(element(by.id('sc-step-comparison-highlight-changes-button')).isDisplayed()).toBeTruthy();
    expect(element(by.id('sc-step-comparison-highlight-changes-button')).getAttribute('class')).not.toContain('active');
};

StepPage.prototype.showHighlights = function () {
    this.toggleHighlights();
    this.expectHighlightsDisplayed();
};

StepPage.prototype.hideHighlights = function () {
    this.toggleHighlights();
    this.expectHighlightsHidden();
};

StepPage.prototype.expectHighlightsButtonHidden = function () {
    expect(element(by.id('sc-step-comparison-highlight-changes-button')).isPresent()).toBeFalsy();
};


StepPage.prototype.showSideBySideView = function () {
    element(by.id('sc-step-comparison-side-by-side-view-button')).click();
};

// former showSinglePageView
// and switchToBaseScreenshot
StepPage.prototype.showComparisonCurrentScreenView = function () {
    element(by.id('sc-step-comparison-current-screen-view-button')).click();
};

// former showSinglePageView
// and switchToComparisonScreenshot
StepPage.prototype.showComparisonOtherScreenView = function () {
    element(by.id('sc-step-comparison-other-screen-view-button')).click();
};

StepPage.prototype.switchComparisonSingleScreens = function () {
    element(by.id('sc-step-comparison-switch-screens-button')).click();
};

StepPage.prototype.expectSwitchComparisonSingleScreensButtonDisabled = function () {
    expect(element(by.id('sc-step-comparison-switch-screens-button')).getAttribute('disabled')).toEqual('true');
};

StepPage.prototype.expectSwitchComparisonSingleScreensButtonEnabled = function () {
    expect(element(by.id('sc-step-comparison-switch-screens-button')).getAttribute('disabled')).toBeFalsy();
};

StepPage.prototype.expectStepComparisonLegendText = function(legendText) {
    expect(element(by.id('sc-step-comparison-legend-info-text')).getText()).toContain(legendText);
};

StepPage.prototype.assertNoDiffInfoDisplayed = function () {
    expect(element(by.id('comparison-tab')).isPresent()).toBeFalsy();
};

StepPage.prototype.assertStepComparisonSideBySideViewIsActive = function () {
    expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).toContain('active');
    expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).not.toContain('active');
    expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).not.toContain('active');
    expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeTruthy();
    expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeFalsy();
};

StepPage.prototype.assertStepComparisonCurrentScreenViewIsActive = function () {
    expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).not.toContain('active');
    expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).toContain('active');
    expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).not.toContain('active');
    expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeFalsy();
    expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeTruthy();
    expect(element(by.css('.sc-step-comparison-current-screenshot')).isPresent()).toBeTruthy();
    expect(element(by.css('.sc-step-comparison-other-screenshot')).isPresent()).toBeFalsy();
};

StepPage.prototype.assertStepComparisonOtherScreenViewIsActive = function () {
    expect(element(by.id('sc-step-comparison-side-by-side-view-button')).getAttribute('class')).not.toContain('active');
    expect(element(by.id('sc-step-comparison-current-screen-view-button')).getAttribute('class')).not.toContain('active');
    expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('class')).toContain('active');
    expect(element(by.id('sc-step-comparison-side-by-side-view')).isPresent()).toBeFalsy();
    expect(element(by.id('sc-step-comparison-single-page-view')).isPresent()).toBeTruthy();
    expect(element(by.css('.sc-step-comparison-current-screenshot')).isPresent()).toBeFalsy();
    expect(element(by.css('.sc-step-comparison-other-screenshot')).isPresent()).toBeTruthy();
};

StepPage.prototype.expectStepComparisonCurrentScreenTitle = function(title: string, infoText: string) {
    expect(element(by.css('sc-screenshot-title[build="baseBuild"] h3')).getText()).toBe(title);
};

StepPage.prototype.expectStepComparisonOtherScreenTitle = function(title: string, infoText: string) {
    expect(element(by.css('sc-screenshot-title[build="comparisonBuild"] h3')).getText()).toBe(title);
    element(by.css('sc-screenshot-title[build="comparisonBuild"] h3 i.icon-info-sign')).click();
    expect(element(by.css('sc-screenshot-title[build="comparisonBuild"] div.tooltip')).getText()).toBe(infoText);
    element(by.css('sc-screenshot-title[build="comparisonBuild"] h3')).click();
};

StepPage.prototype.expectStepComparisonOtherScreenViewIsDisabled = function () {
    expect(element(by.id('sc-step-comparison-other-screen-view-button')).getAttribute('disabled')).toEqual('true');
};

StepPage.prototype.assertStepNoComparisonScreenshot = function (expected) {
    expect(element(by.css('.sc-step-comparison-other-screenshot img')).isPresent()).toBeFalsy();
};

StepPage.prototype.assertStepComparisonScreenshotSrcEquals = function (expected) {
    expect(element(by.css('.sc-step-comparison-other-screenshot img')).getAttribute('src')).toContain(expected);
};

StepPage.prototype.assertStepBaseScreenshotSrcEquals = function (expected) {
    expect(element(by.css('.sc-step-comparison-current-screenshot img')).getAttribute('src')).toContain(expected);
};

module.exports = StepPage;
