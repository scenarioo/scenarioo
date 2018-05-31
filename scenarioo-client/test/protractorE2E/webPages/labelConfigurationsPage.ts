'use strict';

import { by, element, ElementFinder, $ } from 'protractor';
import * as Utils from '../util/util';

class LabelConfigurationsPage {

    private labelConfigurationsTable: ElementFinder = element(by.id('label-configurations-table'));
    private saveButton: ElementFinder = $('input.btn[value="Save"]');
    private savedSuccessfullyText: ElementFinder = element(by.id('changed-label-config-successfully'));

    async navigateToPage() {
        return Utils.navigateToRoute('/manage?tab=labelConfigurations');
    }

    async assertNumConfigurations(expectedCount) {
        const tableElement = element(by.id('label-configurations-table'));
        // adding one, because there's always an empty row
        return Utils.assertNumberOfTableRows(tableElement, expectedCount + 1);
    }

    async addLabelConfiguration(labelName, colorIndex) {
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const numberOfElements = await elements.count();
        const lastRow = elements.get(numberOfElements - 1);
        const labelNameField = lastRow.$('input[name="labelName"]');
        const colors = lastRow.all(by.css('ul li span'));
        await colors.get(colorIndex).click();
        await labelNameField.sendKeys(labelName);

        await this.saveButton.click();
        return expect(this.savedSuccessfullyText.isDisplayed()).toBe(true);
    }

    async updateLabelConfiguration(rowIndex, labelName, colorIndex) {
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        const labelNameField = row.$('input[name="labelName"]');
        const colors = row.all(by.css('ul li span'));
        await colors.get(colorIndex).click();

        await labelNameField.clear();
        await labelNameField.sendKeys(labelName);

        return this.saveButton.click();
    }

    async deleteLabelConfiguration(rowIndex) {
        await $('#label-configuration-' + rowIndex + ' input[value="Delete"]').click();
        await Utils.assertNumberOfTableRows(this.labelConfigurationsTable, 1); // only the empty row is shown
        await this.saveButton.click();
        return Utils.waitForElementVisible(element(by.id('changed-label-config-successfully')));
    }

}

export default new LabelConfigurationsPage();
