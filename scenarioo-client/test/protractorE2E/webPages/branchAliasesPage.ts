'use strict';

import { by, element, ElementFinder } from "protractor";
import * as Utils from "../util/util";

export default class BranchAliasesPage {

    private path: string = '/manage?tab=branchAliases';
    private branchAliasTable: ElementFinder = element(by.css('table.table-responsive'));
    private saveButton: ElementFinder = element(by.css('input.btn[value="Save"]'));
    private resetButton: ElementFinder = element(by.css('input.btn[value="Reset"]'));
    private branchesDropDown: ElementFinder = element(by.css('div.navbar ul.nav li.dropdown ul.dropdown-menu'));

    async assertNumberOfAliases(expectedCount) {
        var rows = this.branchAliasTable.all(by.css('tbody tr'));
        // + 1 due to empty row
        expect(rows.count()).toBe(expectedCount + 1);
    };

    async openBranchSelectionMenu() {
        element(by.id('branchSelectionDropdown')).click();
    };

    async assertAliasesAreShownFirstInTheNavigationMenu() {
        var branchOptions = element.all(by.css('#branchSelectionDropdown .branchOption'));
        expect(branchOptions.get(2).getText()).toBe('Test Alias 1 (wikipedia-docu-example)');
        expect(branchOptions.get(3).getText()).toBe('Test Alias 2 (wikipedia-docu-example)');
    };

    async enterAlias(name, referencedBranchName, description) {
        var rows = this.branchAliasTable.all(by.css('tbody tr'));
        rows.count().then(function (count) {
            var rowToEditIndex = count - 1;
            var rowToEdit = rows.get(rowToEditIndex);

            rowToEdit.element(by.css('input[name="aliasName"]')).sendKeys(name);
            if (referencedBranchName !== '') {
                // 'select option:nth-child(1)
                rowToEdit.element(by.css('select[name="referencedBranch"]')).click();
                rowToEdit.element(by.css('select[name="referencedBranch"] option[value="' + referencedBranchName + '"]')).click();
            }
            rowToEdit.element(by.css('input[name="aliasDescription"]')).sendKeys(description);
        });
    };

    async saveAndAssertSuccessMessage() {
        this.saveButton.click();
        Utils.waitForElementVisible(element(by.id('updated-branch-aliases-successfully')));
    };

    async save() {
        this.saveButton.click();
    };

    async reset() {
        this.resetButton.click();
    };

    async assertSaveNotPossible() {
        expect(this.saveButton.isEnabled()).toBe(false);
    };

    async deleteAlias(rowIndex) {
        this.branchAliasTable.all(by.css('tbody tr input.btn[value="Delete"]')).get(rowIndex).click();
    };

    async updateAlias(rowIndex, newAlias, referencedBranchName, newDescription) {
        var rows = this.branchAliasTable.all(by.css('tbody tr'));
        var rowToEdit = rows.get(rowIndex);

        var aliasNameField = rowToEdit.element(by.css('input[name="aliasName"]'));
        var referencedBranchField = rowToEdit.element(by.css('select[name="referencedBranch"]'));
        var aliasDescriptionField = rowToEdit.element(by.css('input[name="aliasDescription"]'));

        aliasNameField.clear();
        aliasNameField.sendKeys(newAlias);

        if (referencedBranchName !== '') {
            referencedBranchField.element(by.css('option[value="' + referencedBranchName + '"]')).click();
        }

        aliasDescriptionField.clear();
        aliasDescriptionField.sendKeys(newDescription);
    };

    async assertAlias(rowIndex, expectedAlias, referencedBuildIndex, expectedDescription) {
        var rows = this.branchAliasTable.all(by.css('tbody tr'));
        var row = rows.get(rowIndex);

        var aliasNameField = row.element(by.css('input[name="aliasName"]'));
        var aliasDescriptionField = row.element(by.css('input[name="aliasDescription"]'));

        expect(aliasNameField.getAttribute('value')).toEqual(expectedAlias);
        expect(aliasDescriptionField.getAttribute('value')).toEqual(expectedDescription);
    };

    async assertDuplicateAliasError() {
        expect(element(by.id('duplicateAliasErrorId')).isPresent()).toBe(true);
    };

}
