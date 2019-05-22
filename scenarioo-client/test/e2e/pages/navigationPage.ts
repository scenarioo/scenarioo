'use strict';

import { by, element, ElementFinder, $ } from 'protractor';

class NavigationPage {

    private searchBoxTextField = element(by.id('sc-global-search-box-textfield'));
    private searchBoxButton = element(by.id('sc-global-search-box-button'));

    async enterSearchTerm(searchTerm) {
        await this.searchBoxTextField.clear();
        return this.searchBoxTextField.sendKeys(searchTerm);
    }

    async clickSearchButton() {
        return this.searchBoxButton.click();
    }

    async chooseBranch(branchName) {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Branch:')).click();
        return $('#branchSelectionDropdown .dropdown-menu').all(by.partialLinkText(branchName)).first().click();
    }

    async chooseBuild(buildName) {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Build:')).click();
        return $('#build-selection-dropdown .dropdown-menu').all(by.partialLinkText(buildName)).first().click();
    }

    async chooseComparison(comparisonName) {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Comparison:')).click();
        return $('#comparison-selection-dropdown .dropdown-menu').all(by.partialLinkText(comparisonName)).first().click();
    }

    async disableComparison() {
        // Open menu first, otherwise we cannot click
        await element(by.partialLinkText('Comparison:')).click();

        const comparisonElements = $('#comparison-selection-dropdown .dropdown-menu');
        const disableEntry = comparisonElements.element(by.partialLinkText('Disable'));

        // It's not there if it's already disabled
        if (await disableEntry.isPresent()) {
            return disableEntry.click();
        }
    }

    async assertSelectedComparison(comparisonName) {
        return expect($('#comparison-selection-dropdown > a').getText()).toContain(comparisonName);
    }

}

export default new NavigationPage();
