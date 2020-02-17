'use strict';

import {$, by, element} from 'protractor';
import * as Utils from '../util';

class LabelConfigurationsPage {

    private labelConfigurationsTable = element(by.id('label-configurations-table'));
    private saveButton = $('input.btn[value="Save"]');
    private savedSuccessfullyText = element(by.id('changed-label-config-successfully'));

    async navigateToPage() {
        return Utils.navigateToRoute('/manage?tab=labelConfigurations');
    }

    async assertNumConfigurations(expectedCount: number) {
        const rows = this.labelConfigurationsTable.$$('tbody tr');
        // adding one, because there's always an empty row
        return expect(rows.count()).toBe(expectedCount + 1);
    }

    async assertConfigurationColor(rowIndex: number, expectedColorAsHex: string) {
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        const labelColorField = row.$('input[data-type="labelColor"]');
        return expect(labelColorField.getAttribute('value')).toBe(expectedColorAsHex);
    }

    async addLabelConfigurationWithPresetColor(labelName: string, colorIndex: number) {
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const numberOfElements = await elements.count();
        const lastRow = elements.get(numberOfElements - 1);
        const labelNameField = lastRow.$('input[data-type="labelName"]');
        await labelNameField.sendKeys(labelName);
        const colors = lastRow.all(by.css('ul li span'));
        await colors.get(colorIndex).click();
        await this.saveButton.click();
        return Utils.waitForElementVisible(this.savedSuccessfullyText);
    }

    async addLabelConfigurationWithCustomColor(labelName: string, colorAsHex: string) {
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const numberOfElements = await elements.count();
        const lastRow = elements.get(numberOfElements - 1);
        const labelNameField = lastRow.$('input[data-type="labelName"]');
        await labelNameField.sendKeys(labelName);
        const colorInputField = lastRow.$('input[data-type="labelColor"');
        await colorInputField.sendKeys(colorAsHex);
        await this.saveButton.click();
        return Utils.waitForElementVisible(this.savedSuccessfullyText);
    }

    async updateLabelConfigurationWithPresetColor(rowIndex: number, labelName: string, colorIndex: number) {
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        const labelNameField = row.$('input[data-type="labelName"]');
        await labelNameField.clear();
        await labelNameField.sendKeys(labelName);
        const colors = row.all(by.css('ul li span'));
        await colors.get(colorIndex).click();

        return this.saveButton.click();
    }

    async updateLabelConfigurationWithRandomColor(rowIndex: number) {
        const hexColorDefinitionLength = 7;
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        const colorInputField = row.$('input[data-type="labelColor"]');
        await colorInputField.clear();
        const randomColorButton = row.$('button[data-type="buttonColor"]');
        await randomColorButton.click();
        const colorInputValue = await colorInputField.getAttribute('value');
        return expect(colorInputValue.length).toBe(hexColorDefinitionLength);
    }

    async deleteLabelConfiguration(rowIndex, currentNumberOfConfiguredLabels) {
        // One additional row for adding a new label configuration
        const numberOfTableRows = currentNumberOfConfiguredLabels + 1;
        await $('#label-configuration-' + rowIndex + ' input[value="Delete"]').click();
        await Utils.assertNumberOfTableRows(this.labelConfigurationsTable, numberOfTableRows - 1);
        await this.saveButton.click();
        return Utils.waitForElementVisible(element(by.id('changed-label-config-successfully')));
    }

}

export default new LabelConfigurationsPage();
