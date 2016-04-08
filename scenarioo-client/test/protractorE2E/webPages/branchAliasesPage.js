'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util');

function BranchAliasesPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/manage?tab=branchAliases');
    }

    this.branchAliasTable = element(by.css('table.table-responsive'));
    this.saveButton = element(by.css('input.btn[value="Save"]'));
    this.resetButton = element(by.css('input.btn[value="Reset"]'));

    this.branchesDropDown = element(by.css('div.navbar ul.nav li.dropdown ul.dropdown-menu'));

}

util.inherits(BranchAliasesPage, BaseWebPage);

BranchAliasesPage.prototype.assertNumberOfAliases = function (expectedCount) {
    var rows = this.branchAliasTable.all(by.css('tbody tr'));
    // + 1 due to empty row
    expect(rows.count()).toBe(expectedCount + 1);
};

BranchAliasesPage.prototype.openBranchSelectionMenu = function () {
    element(by.id('branchSelectionDropdown')).click();
};

BranchAliasesPage.prototype.assertAliasesAreShownFirstInTheNavigationMenu = function () {
    var branchOptions = element.all(by.css('#branchSelectionDropdown .branchOption'));
    expect(branchOptions.get(0).getText()).toBe('Test Alias 1 (wikipedia-docu-example)');
    expect(branchOptions.get(1).getText()).toBe('Test Alias 2 (wikipedia-docu-example)');
};

BranchAliasesPage.prototype.enterAlias = function (name, referencedBranchName, description) {
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

BranchAliasesPage.prototype.save = function () {
    this.saveButton.click();
};

BranchAliasesPage.prototype.reset = function () {
    this.resetButton.click();
};

BranchAliasesPage.prototype.assertSaveNotPossible = function () {
    expect(this.saveButton.isEnabled()).toBe(false);
};

BranchAliasesPage.prototype.deleteAlias = function (rowIndex) {
    this.branchAliasTable.all(by.css('tbody tr input.btn[value="Delete"]')).get(rowIndex).click();
};

BranchAliasesPage.prototype.updateAlias = function (rowIndex, newAlias, referencedBranchName, newDescription) {
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


BranchAliasesPage.prototype.assertAlias = function (rowIndex, expectedAlias, referencedBuildIndex, expectedDescription) {
    var rows = this.branchAliasTable.all(by.css('tbody tr'));
    var row = rows.get(rowIndex);

    var aliasNameField = row.element(by.css('input[name="aliasName"]'));
    var aliasDescriptionField = row.element(by.css('input[name="aliasDescription"]'));

    expect(aliasNameField.getAttribute('value')).toEqual(expectedAlias);
    expect(aliasDescriptionField.getAttribute('value')).toEqual(expectedDescription);
};

BranchAliasesPage.prototype.assertDuplicateAliasError = function () {
    expect(element(by.id('duplicateAliasErrorId')).isPresent()).toBe(true);
};

BranchAliasesPage.prototype.chooseBranch = function (branchName) {
    // Open menu first, otherwise we cannot click
    element(by.partialLinkText('Branch:')).click();
    element(by.partialLinkText(branchName)).click();
};

module.exports = BranchAliasesPage;
