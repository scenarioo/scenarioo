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

}

util.inherits(LabelConfigurationsPage, BaseWebPage);

LabelConfigurationsPage.prototype.assertNumConfigurations = function(expectedCount) {
    this.labelConfigurationsTable.all(by.css('tbody tr')).then(function (elements) {
        // -1 due to empty row
        expect(elements.length - 1).toBe(expectedCount);
    });
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
