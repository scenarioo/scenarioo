'use strict';

import { browser, by, element, ElementFinder } from "protractor";
import * as e2eUtils from "../util/util";

export default class HomePage {

    private static path: string = "/";
    private static useCasesSearchField: ElementFinder = element(by.id('useCasesSearchField'));
    private static aboutScenariooPopup: ElementFinder = element(by.css('.modal.about-popup'));
    private static popupCloseButton: ElementFinder = element(by.css('.modal-footer button.btn'));
    private static usecaseTable: ElementFinder = element(by.css('table.usecase-table'));
    private static showMetaDataButton: ElementFinder = element(by.id('sc-showHideDetailsButton-show'));
    private static hideMetaDataButton: ElementFinder = element(by.id('sc-showHideDetailsButton-hide'));
    private static metaDataPanel: ElementFinder = element(by.id('sc-metadata-panel'));
    private static sketchesTab: ElementFinder = element(by.id('sc-main-tab-sketches'));
    private static pagesTab: ElementFinder = element(by.id('sc-main-tab-pages'));

    static async assertPageIsDisplayed() {
        await e2eUtils.assertPageIsDisplayed(this.path);
        return expect(this.useCasesSearchField.isDisplayed()).toBe(true);
    }

    static async assertScenariooInfoDialogShown() {
        await expect(this.aboutScenariooPopup.isDisplayed()).toBe(true);
        return expect(this.popupCloseButton.isDisplayed()).toBe(true);
    }

    static async assertScenariooInfoDialogNotShown() {
        return e2eUtils.assertElementNotPresentInDom(by.css('.modal-dialog.about-popup'));
    }

    static async assertComparisonMenuNotShown() {
        return e2eUtils.assertElementNotPresentInDom(by.id('#comparison-selection-dropdown'));
    }

    static async closeScenariooInfoDialogIfOpen() {
        const present = await browser.isElementPresent(by.css('.modal-footer button.btn'));
        if(present) {
            await element(by.css('.modal-footer button.btn')).click();
        }
    }

    static async filterUseCases(filterQuery) {
        await this.useCasesSearchField.clear();
        return this.useCasesSearchField.sendKeys(filterQuery);
    }

    static async assertUseCasesShown(count) {
        return this.usecaseTable.all(by.css('tbody tr')).then(function (elements) {
            return expect(elements.length).toBe(count);
        });
    }

    static async selectUseCase(useCaseIndex) {
        return this.usecaseTable.all(by.css('tbody tr')).then(function(elements) {
            return elements[useCaseIndex].click();
        });
    }

    static async showMetaData() {
        return this.showMetaDataButton.click();
    }

    static async assertMetaDataShown() {
        return expect(this.metaDataPanel.isDisplayed()).toBe(true);
    }

    static async assertMetaDataHidden() {
        return expect(this.metaDataPanel.isDisplayed()).toBe(false);
    }

    static async hideMetaData() {
        return this.hideMetaDataButton.click();
    }

    static async sortByChanges(){
        return this.usecaseTable.element(by.css('th.sort-diff-info')).click();
    }

    static async assertNumberOfDiffInfos(count){
        this.usecaseTable.all(by.css('.diff-info-wrapper')).then(function (elements) {
            return expect(elements.length).toBe(count);
        });
    }

    static async assertLastUseCase(lastName) {
        return e2eUtils.assertTextPresentInElement(this.usecaseTable.element(by.css('tr:last-of-type td:nth-of-type(2)')), lastName);
    };

    static async assertFirstUseCase(firstName) {
        return e2eUtils.assertTextPresentInElement(this.usecaseTable.element(by.css('tr:first-of-type td:nth-of-type(2)')), firstName);
    };

    static async selectSketchesTab() {
        return this.sketchesTab.click();
    };

    static async assertSketchesListContainsEntryWithSketchName(sketchName) {
        return e2eUtils.assertTextPresentInElement(element(by.id('sc-sketches-list')), sketchName);
    };

    static async selectPagesTab() {
        return this.pagesTab.click();
    };

    static async assertPagesTabContainsPage(pageName) {
        return e2eUtils.assertTextPresentInElement(element(by.id('treeviewtable')), pageName);
    };

    static async filterPages(filterQuery) {
        const pagesSearchField = element(by.id('pagesSearchField'));
        await pagesSearchField.clear();
        return pagesSearchField.sendKeys(filterQuery);
    };

    static async assertCustomTabEntriesShown(count) {
        const elements = await element(by.id('treeviewtable')).all(by.css('tbody tr')).filter(e => e.isDisplayed());
        // Not a great workaround, at the moment the filterable table header column is also a normal tr
        return expect(elements.length).toBe(count);
    };

    static async assertNoDiffInfoDisplayed() {
        return expect(element(by.css('.sort-diff-info')).isPresent()).toBeFalsy();
    };

}
