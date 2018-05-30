'use strict';

import { by, element, ElementFinder } from 'protractor';

export default class NavigationPage {

    private static searchBoxTextField: ElementFinder = element(by.id('sc-global-search-box-textfield'));
    private static searchBoxButton: ElementFinder = element(by.id('sc-global-search-box-button'));

    static async enterSearchTerm(searchTerm) {
        await this.searchBoxTextField.clear();
        return this.searchBoxTextField.sendKeys(searchTerm);
    }

    static async clickSearchButton() {
        return this.searchBoxButton.click();
    }

    static async chooseBranch(branchName) {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Branch:')).click();
        return element(by.css('#branchSelectionDropdown .dropdown-menu')).all(by.partialLinkText(branchName)).first().click();
    }

    static async chooseBuild(buildName) {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Build:')).click();
        return element(by.css('#build-selection-dropdown .dropdown-menu')).all(by.partialLinkText(buildName)).first().click();
    }

    static async chooseComparison(comparisonName) {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Comparison:')).click();
        return element(by.css('#comparison-selection-dropdown .dropdown-menu')).all(by.partialLinkText(comparisonName)).first().click();
    }

    static async disableComparison() {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Comparison:')).click();

        const comparisonElements = element(by.css('#comparison-selection-dropdown .dropdown-menu'));
        const disableEntry = comparisonElements.element(by.partialLinkText('Disable'));

        // It's not there if it's already disabled
        if (await disableEntry.isPresent()) {
            return disableEntry.click();
        }
    }

    static async assertSelectedComparison(comparisonName) {
        return expect(element(by.css('#comparison-selection-dropdown > a')).getText()).toContain(comparisonName);
    }

}
