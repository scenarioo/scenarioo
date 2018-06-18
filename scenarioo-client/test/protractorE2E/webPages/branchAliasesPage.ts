'use strict';

import {by, element, ElementFinder, $, $$} from 'protractor';
import * as Utils from '../util/util';

class BranchAliasesPage {

    private branchAliasTable = $('table.table-responsive');
    private saveButton = $('input.btn[value="Save"]');
    private resetButton = $('input.btn[value="Reset"]');

    async goToPage() {
        return Utils.navigateToRoute('/manage?tab=branchAliases');
    }

    async assertNumberOfAliases(expectedCount) {
        const rows = this.branchAliasTable.all(by.css('tbody tr'));
        // + 1 due to empty row
        return expect(rows.count()).toBe(expectedCount + 1);
    }

    async openBranchSelectionMenu() {
        return element(by.id('branchSelectionDropdown')).click();
    }

    async assertAliasesAreShownFirstInTheNavigationMenu() {
        const branchOptions = $$('#branchSelectionDropdown .branchOption');
        await expect(branchOptions.get(2).getText()).toBe('Test Alias 1 (wikipedia-docu-example)');
        return expect(branchOptions.get(3).getText()).toBe('Test Alias 2 (wikipedia-docu-example)');
    }

    async enterAlias(name, referencedBranchName, description) {
        const rows = this.branchAliasTable.all(by.css('tbody tr'));
        const count = await rows.count();
        const rowToEditIndex = count - 1;
        const rowToEdit = rows.get(rowToEditIndex);

        await rowToEdit.$('input[name="aliasName"]').sendKeys(name);
        if (referencedBranchName !== '') {
            // 'select option:nth-child(1)
            await rowToEdit.$('select[name="referencedBranch"]').click();
            await rowToEdit.$('select[name="referencedBranch"] option[value="' + referencedBranchName + '"]').click();
        }
        return rowToEdit.$('input[name="aliasDescription"]').sendKeys(description);
    }

    async saveAndAssertSuccessMessage() {
        await this.saveButton.click();
        return Utils.waitForElementVisible(element(by.id('updated-branch-aliases-successfully')));
    }

    async save() {
        return this.saveButton.click();
    }

    async reset() {
        return this.resetButton.click();
    }

    async assertSaveNotPossible() {
        return expect(this.saveButton.isEnabled()).toBe(false);
    }

    async deleteAlias(rowIndex) {
        return this.branchAliasTable.all(by.css('tbody tr input.btn[value="Delete"]')).get(rowIndex).click();
    }

    async updateAlias(rowIndex, newAlias, referencedBranchName, newDescription) {
        const rows = this.branchAliasTable.all(by.css('tbody tr'));
        const rowToEdit = rows.get(rowIndex);

        const aliasNameField = rowToEdit.$('input[name="aliasName"]');
        const referencedBranchField = rowToEdit.$('select[name="referencedBranch"]');
        const aliasDescriptionField = rowToEdit.$('input[name="aliasDescription"]');

        await aliasNameField.clear();
        await aliasNameField.sendKeys(newAlias);

        if (referencedBranchName !== '') {
            await referencedBranchField.$('option[value="' + referencedBranchName + '"]').click();
        }

        await aliasDescriptionField.clear();
        return aliasDescriptionField.sendKeys(newDescription);
    }

    async assertDuplicateAliasError() {
        return expect(element(by.id('duplicateAliasErrorId')).isPresent()).toBe(true);
    }

}

export default new BranchAliasesPage();
