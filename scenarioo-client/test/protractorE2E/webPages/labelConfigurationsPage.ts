'use strict';

import { by, element, ElementFinder } from 'protractor';
import * as Utils from '../util/util';

export default class LabelConfigurationsPage {

    private static labelConfigurationsTable: ElementFinder = element(by.id('label-configurations-table'));
    private static saveButton: ElementFinder = element(by.css('input.btn[value="Save"]'));
    private static savedSuccessfullyText: ElementFinder = element(by.id('changed-label-config-successfully'));

    static async navigateToPage() {
        return Utils.navigateToRoute('/manage?tab=labelConfigurations');
    }

    static async assertNumConfigurations(expectedCount) {
        const tableElement = element(by.id('label-configurations-table'));
        // adding one, because there's always an empty row
        return Utils.assertNumberOfTableRows(tableElement, expectedCount + 1);
    }

    static async addLabelConfiguration(labelName, colorIndex) {
        const elements = await this.labelConfigurationsTable.all(by.css('tbody tr'));
        const lastRow = elements[elements.length - 1];
        const labelNameField = lastRow.element(by.css('input[name="labelName"]'));
        const colors = await lastRow.all(by.css('ul li span'));
        await colors[colorIndex].click();
        await labelNameField.sendKeys(labelName);

        await this.saveButton.click();
        return expect(this.savedSuccessfullyText.isDisplayed()).toBe(true);
    }

    static async updateLabelConfiguration(rowIndex, labelName, colorIndex) {
        const elements = await this.labelConfigurationsTable.all(by.css('tbody tr'));
        const row = elements[rowIndex];
        const labelNameField = row.element(by.css('input[name="labelName"]'));
        const colors = await row.all(by.css('ul li span'));
        await colors[colorIndex].click();

        await labelNameField.clear();
        await labelNameField.sendKeys(labelName);

        return this.saveButton.click();
    }

    static async deleteLabelConfiguration(rowIndex) {
        await element(by.css('#label-configuration-' + rowIndex + ' input[value="Delete"]')).click();
        await Utils.assertNumberOfTableRows(this.labelConfigurationsTable, 1); // only the empty row is shown
        await this.saveButton.click();
        return Utils.waitForElementVisible(element(by.id('changed-label-config-successfully')));
    }

}
