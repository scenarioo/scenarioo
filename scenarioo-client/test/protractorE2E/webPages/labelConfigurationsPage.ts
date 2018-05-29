'use strict';

import { browser, by, element, ElementFinder } from "protractor";
import * as Utils from "../util/util";

export default class LabelConfigurationsPage {

    private path: string = '/manage?tab=labelConfigurations';
    private labelConfigurationsTable: ElementFinder = element(by.id('label-configurations-table'));
    private saveButton: ElementFinder = element(by.css('input.btn[value="Save"]'));
    private resetButton: ElementFinder = element(by.css('input.btn[value="Reset"]'));
    private savedSuccessfullyText: ElementFinder = element(by.id('changed-label-config-successfully'));

    async assertNumConfigurations(expectedCount) {
        var tableElement = element(by.id('label-configurations-table'));
        // adding one, because there's always an empty row
        Utils.assertNumberOfTableRows(tableElement, expectedCount + 1);
    };

    async addLabelConfiguration(labelName, colorIndex) {
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

    async updateLabelConfiguration(rowIndex, labelName, colorIndex) {
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

    async deleteLabelConfiguration(rowIndex) {
        element(by.css('#label-configuration-' + rowIndex + ' input[value="Delete"]')).click();
        Utils.assertNumberOfTableRows(this.labelConfigurationsTable, 1); // only the empty row is shown
        this.saveButton.click();
        Utils.waitForElementVisible(element(by.id('changed-label-config-successfully')));
    };

}
