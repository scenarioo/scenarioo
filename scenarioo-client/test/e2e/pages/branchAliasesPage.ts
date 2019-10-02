'use strict';

import {$, $$, by, element} from 'protractor';
import * as Utils from '../util';

class BranchAliasesPage {

    private branchAliasTable = $('table.table-responsive');
    private saveButton = $('input.btn[value="Save"]');
    private resetButton = $('input.btn[value="Reset"]');

    async goToPage() {
        return Utils.navigateToRoute('/manage?tab=branchAliases');
    }

    async assertNumberOfAliases(expectedCount) {
        const rows = this.branchAliasTable.$$('tbody tr');
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

    async enterAliasWithoutReferenceBranch(name, description) {
        return this.enterAlias(name, '', description);
    }

    async enterAlias(name, referencedBranchName, description) {
        const rows = this.branchAliasTable.$$('tbody tr');
        const count = await rows.count();
        const rowToEditIndex = count - 1;
        const rowToEdit = rows.get(rowToEditIndex);

        await rowToEdit.$('input[data-type="branchAlias"]').sendKeys(name);
        if (referencedBranchName !== '') {
            await rowToEdit.$('select[data-type="referencedBranch"]').click();
            await rowToEdit.$('select[data-type="referencedBranch"] option[value="' + referencedBranchName + '"]').click();
        }
        return rowToEdit.$('input[data-type="aliasDescription"]').sendKeys(description);
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

    async deleteAlias(rowIndex) {
        return this.branchAliasTable.$$('tbody tr input.btn[value="Delete"]').get(rowIndex).click();
    }

    async updateAlias(rowIndex, newAlias, referencedBranchName, newDescription) {
        const rows = this.branchAliasTable.$$('tbody tr');
        const rowToEdit = rows.get(rowIndex);

        const aliasNameField = rowToEdit.$('input[data-type="branchAlias"]');
        const referencedBranchField = rowToEdit.$('select[data-type="referencedBranch"]');
        const aliasDescriptionField = rowToEdit.$('input[data-type="aliasDescription"]');

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

    async assertRequiredFieldsError() {
        return expect(element(by.id('requiredFieldsErrorId')).isPresent()).toBe(true);
    }
}

export default new BranchAliasesPage();
