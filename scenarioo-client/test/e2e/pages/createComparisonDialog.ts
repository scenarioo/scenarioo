'use strict';

import {$, by, element} from 'protractor';
import * as Utils from '../util';

class CreateComparisonDialog {

    async openCreateComparison() {
        return Utils.clickElementById('createComparisonBtn');
    }

    async enterComparisonName(comparisonName: string) {
        const comparisonNameField = element(by.id('comparisonName'));
        return comparisonNameField.sendKeys(comparisonName);
    }

    async openTargetBranchSelection() {
        return element.all(by.partialLinkText('Branch:')).first().click();
    }

    async openTargetBuildSelection() {
        return element.all(by.partialLinkText('Build:')).first().click();
    }

    async chooseTargetBranch(branchName: string) {
        return $('#target #branchSelectionDropdown .dropdown-menu').all(by.partialLinkText(branchName)).first().click();
    }

    async chooseTargetBuild(buildName: string) {
        return $('#target #build-selection-dropdown .dropdown-menu').all(by.partialLinkText(buildName)).first().click();
    }

    async openComparisonBranchSelection() {
        return element.all(by.partialLinkText('Branch:')).get(1).click();
    }

    async openComparisonBuildSelection() {
        return element.all(by.partialLinkText('Build:')).get(1).click();
    }

    async chooseComparisonBranch(branchName: string) {
        return $('#compareWith #branchSelectionDropdown .dropdown-menu').all(by.partialLinkText(branchName)).first().click();
    }

    async chooseComparisonBuild(buildName: string) {
        return $('#compareWith #build-selection-dropdown .dropdown-menu').all(by.partialLinkText(buildName)).first().click();
    }

    async assertComparisonAlreadyExistsError() {
        await this.assertValidationMessage('Comparison with that name already exists on selected target build');
    }

    async assertComparisonBranchNeededError() {
        await this.assertValidationMessage('Please choose branch and build to compare with!');
    }

    async assertComparisonOfSelectedBuildsExistsError() {
        await this.assertValidationMessage('Comparison of selected builds already exists!');
    }

    private async assertValidationMessage(expectedMessage: string) {
        await expect(element(by.id('validationMessage')).isPresent()).toBe(true);
        await expect(element(by.id('validationMessage')).getText()).toContain(expectedMessage);
    }

    async createComparison() {
        return Utils.clickElementById('createComparison');
    }
}

export default new CreateComparisonDialog();
