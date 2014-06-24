'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function BranchAliasesPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.branchAliasTable = element(by.css('table.table-responsive'));
    this.saveButton = element(by.css('input.btn[value="Save"]'));
    this.resetButton = element(by.css('input.btn[value="Reset"]'));

}

util.inherits(BranchAliasesPage, BaseWebPage);

BranchAliasesPage.prototype.assertNumberOfAliases = function(expectedCount) {
    this.branchAliasTable.findElements(by.css('tbody tr')).then(function (elements) {
        // + 1 due to empty row
        expect(elements.length).toBe(expectedCount + 1);
    });
};

BranchAliasesPage.prototype.enterAlias = function(name, referencedBuildIndex, description) {
    this.branchAliasTable.findElements(by.css('tbody tr')).then(function(elements) {
        var lastRow = elements[elements.length-1];
        var aliasNameField = lastRow.findElement(by.css('input[name="aliasName"]'));
        var referencedBranchField = lastRow.findElement(by.css('select[name="referencedBranch"]'));
        var aliasDescriptionField = lastRow.findElement(by.css('input[name="aliasDescription"]'));

        aliasNameField.sendKeys(name);

        if(referencedBuildIndex !== '') {
            referencedBranchField.findElement(by.css('option[value="' + referencedBuildIndex +'"]')).click();
        }

        aliasDescriptionField.sendKeys(description);
    });
};

BranchAliasesPage.prototype.save = function() {
    this.saveButton.click();
};

BranchAliasesPage.prototype.reset = function() {
    this.resetButton.click();
};

BranchAliasesPage.prototype.assertSaveNotPossible = function() {
    expect(this.saveButton.isEnabled()).toBe(false);
};

BranchAliasesPage.prototype.deleteAlias = function(rowIndex) {
    this.branchAliasTable.findElements(by.css('tbody tr input.btn[value="Delete"]')).then(function(elements) {
        elements[rowIndex].click();
    });
};

BranchAliasesPage.prototype.updateAlias = function(rowIndex, newAlias, referencedBuildIndex, newDescription) {
    this.branchAliasTable.findElements(by.css('tbody tr')).then(function(elements) {
        var row = elements[rowIndex];
        var aliasNameField = row.findElement(by.css('input[name="aliasName"]'));
        var referencedBranchField = row.findElement(by.css('select[name="referencedBranch"]'));
        var aliasDescriptionField = row.findElement(by.css('input[name="aliasDescription"]'));

        aliasNameField.clear();
        aliasNameField.sendKeys(newAlias);

        if(referencedBuildIndex !== '') {
            referencedBranchField.findElement(by.css('option[value="' + referencedBuildIndex +'"]')).click();
        }

        aliasDescriptionField.clear();
        aliasDescriptionField.sendKeys(newDescription);
    });
};


BranchAliasesPage.prototype.assertAlias = function(rowIndex, expectedAlias, referencedBuildIndex, expectedDescription) {
    this.branchAliasTable.findElements(by.css('tbody tr')).then(function(elements) {
        console.log('num rows ' + elements.length);
        var row = elements[rowIndex];
        var aliasNameField = row.findElement(by.css('input[name="aliasName"]'));
        var referencedBranchField = row.findElement(by.css('select[name="referencedBranch"]'));
        var aliasDescriptionField = row.findElement(by.css('input[name="aliasDescription"]'));

        expect(aliasNameField.getAttribute('value')).toEqual(expectedAlias);
        expect(aliasDescriptionField.getAttribute('value')).toEqual(expectedDescription);

    });
};

module.exports = BranchAliasesPage;
