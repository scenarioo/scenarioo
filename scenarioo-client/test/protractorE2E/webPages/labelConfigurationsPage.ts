'use strict';
import {browser, by, element} from "protractor";

var BaseWebPage = require('./baseWebPage'),
    util = require('util');

function LabelConfigurationsPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/manage?tab=labelConfigurations');
    }

    this.labelConfigurationsTable = element(by.id('label-configurations-table'));
    this.saveButton = element(by.css('input.btn[value="Save"]'));
    this.resetButton = element(by.css('input.btn[value="Reset"]'));
    this.savedSuccessfullyText = element(by.id('changed-label-config-successfully'));
}

util.inherits(LabelConfigurationsPage, BaseWebPage);

LabelConfigurationsPage.prototype.assertNumConfigurations = function(expectedCount) {
    var tableElement = element(by.id('label-configurations-table'));
    // adding one, because there's always an empty row
    this.assertNumberOfTableRows(tableElement, expectedCount + 1);
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
    element(by.css('#label-configuration-' + rowIndex + ' input[value="Delete"]')).click();
    this.assertNumberOfTableRows(this.labelConfigurationsTable, 1); // only the empty row is shown
    this.saveButton.click();
    this.waitForElementVisible(element(by.id('changed-label-config-successfully')));
};

module.exports = LabelConfigurationsPage;
