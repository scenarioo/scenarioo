'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function LabelConfigurationsPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.labelConfigurationsTable = element(by.css('table.table-responsive'));
    this.saveButton = element(by.css('input.btn[value="Save"]'));
    this.resetButton = element(by.css('input.btn[value="Reset"]'));

}

util.inherits(LabelConfigurationsPage, BaseWebPage);

LabelConfigurationsPage.prototype.assertNumConfigurations = function(expectedCount) {
    this.labelConfigurationsTable.findElements(by.css('tbody tr')).then(function (elements) {
        // -1 due to empty row
        expect(elements.length -1).toBe(expectedCount);
    });
};

LabelConfigurationsPage.prototype.addLabelConfiguration = function(labelName, colorIndex) {
    this.labelConfigurationsTable.findElements(by.css('tbody tr')).then(function(elements) {
        var lastRow = elements[elements.length-1];
        var labelNameField = lastRow.findElement(by.css('input[name="labelName"]'));
        lastRow.findElements(by.css('ul li span')).then(function(colors) {
            colors[colorIndex].click();
        });

        labelNameField.sendKeys(labelName);

    });

    this.saveButton.click();
};

LabelConfigurationsPage.prototype.updateLabelConfiguration = function(rowIndex, labelName, colorIndex) {
    this.labelConfigurationsTable.findElements(by.css('tbody tr')).then(function(elements) {
        var row = elements[rowIndex];
        var labelNameField = row.findElement(by.css('input[name="labelName"]'));
        row.findElements(by.css('ul li span')).then(function(colors) {
            colors[colorIndex].click();
        });

        labelNameField.clear();
        labelNameField.sendKeys(labelName);

    });

    this.saveButton.click();
};

LabelConfigurationsPage.prototype.deleteLabelConfiguration = function(rowIndex) {
    this.labelConfigurationsTable.findElements(by.css('tbody tr')).then(function(elements) {
        var row = elements[rowIndex];
        var deleteButton = row.findElement(by.css('input[value="Delete"]'));
        deleteButton.click();
    });

    this.saveButton.click();
};

module.exports = LabelConfigurationsPage;
