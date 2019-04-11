'use strict';

import {$, by, element} from 'protractor';

class CreateComparisonDialog {

    async clickCreateComparisonLink() {
        return element(by.id('createComparisonBtn')).click();
    }

    async enterComparisonName(comparisonName) {
        const comparisonNameField = element(by.id('comparisonName'));
        return comparisonNameField.sendKeys(comparisonName);
    }
    async openTargetBranchSelectionDropdown() {
        return element.all(by.partialLinkText('Branch:')).first().click();
    }

    async openTargetBuildSelectionDropdown() {
        return element.all(by.partialLinkText('Build:')).first().click();
    }

    async chooseTargetBranch(branchName) {
        return $('#target #branchSelectionDropdown .dropdown-menu').all(by.partialLinkText(branchName)).first().click();
    }

    async chooseTargetBuild(buildName) {
        return $('#target #build-selection-dropdown .dropdown-menu').all(by.partialLinkText(buildName)).first().click();
    }

    async openComparisonBranchSelectionDropdown() {
        return element.all(by.partialLinkText('Branch:')).get(1).click();
    }

    async openComparisonBuildSelectionDropdown() {
        return element.all(by.partialLinkText('Build:')).get(1).click();
    }

    async chooseComparisonBranch(branchName) {
        return $('#compareWith #branchSelectionDropdown .dropdown-menu').all(by.partialLinkText(branchName)).first().click();
    }

    async chooseComparisonBuild(buildName) {
        return $('#compareWith #build-selection-dropdown .dropdown-menu').all(by.partialLinkText(buildName)).first().click();
    }

    async assertComparisonAlreadyExistsError() {
        await this.assertValidationMessage('Comparison with that name already exists on selected target build');
    }

    async assertComparisonBranchNeededError() {
        await this.assertValidationMessage('Please choose branch and build to compare with!');
    }

    private async assertValidationMessage(expectedMessage: string) {
        await expect(element(by.id('validationMessage')).isPresent()).toBe(true);
        await expect(element(by.id('validationMessage')).getText()).toContain(expectedMessage);
    }

    async createBranch() {
        return element(by.id('createComparisonBtn')).click();
    }
}

export default new CreateComparisonDialog();
