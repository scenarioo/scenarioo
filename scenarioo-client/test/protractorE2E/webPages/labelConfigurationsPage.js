'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util');

function LabelConfigurationsPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/manage?tab=labelConfigurations');
    }

    this.labelConfigurationsTable = element(by.css('table.table-responsive'));
    this.saveButton = element(by.css('input.btn[value="Save"]'));
    this.resetButton = element(by.css('input.btn[value="Reset"]'));
    this.savedSuccessfullyText = element(by.id('changed-label-config-successfully'));
}

util.inherits(LabelConfigurationsPage, BaseWebPage);

LabelConfigurationsPage.prototype.assertNumConfigurations = function(expectedCount) {
    // There is always one row more than the expectedCount, because there's always an empty row
    expect(element(by.id('label-configuration-' + expectedCount)).isDisplayed()).toBe(true);
    expect(element(by.id('label-configuration-' + (expectedCount + 1))).isPresent()).toBe(false);
};

LabelConfigurationsPage.prototype.addLabelConfiguration = function(labelName, colorIndex) {
    this.labelConfigurationsTable.all(by.css('tbody tr')).then(function(elements) {
        var lastRow = elements[elements.length - 1];
        var labelNameField = lastRow.element(by.css('input[name="labelName"]'));
        lastRow.all(by.css('ul li span')).then(function(colors) {
            colors[colorIndex].click();
        });

        labelNameField.sendKeys(labelName);

    });

    this.saveButton.click();
    expect(this.savedSuccessfullyText.isDisplayed()).toBe(true);
};

LabelConfigurationsPage.prototype.updateLabelConfiguration = function(rowIndex, labelName, colorIndex) {
    this.labelConfigurationsTable.all(by.css('tbody tr')).then(function(elements) {
        var row = elements[rowIndex];
        var labelNameField = row.element(by.css('input[name="labelName"]'));
        row.all(by.css('ul li span')).then(function(colors) {
            colors[colorIndex].click();
        });

        labelNameField.clear();
        labelNameField.sendKeys(labelName);

    });

    this.saveButton.click();
};

LabelConfigurationsPage.prototype.deleteLabelConfiguration = function(rowIndex) {
    this.labelConfigurationsTable.all(by.css('tbody tr')).then(function(elements) {
        var row = elements[rowIndex];
        var deleteButton = row.element(by.css('input[value="Delete"]'));
        deleteButton.click();
    });

    this.saveButton.click();
};

module.exports = LabelConfigurationsPage;
