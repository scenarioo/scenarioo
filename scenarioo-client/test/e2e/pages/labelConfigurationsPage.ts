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

    async assertNumConfigurations(expectedCount) {
        const rows = this.labelConfigurationsTable.$$('tbody tr');
        // adding one, because there's always an empty row
        return expect(rows.count()).toBe(expectedCount + 1);
    }

    async addLabelConfiguration(labelName, colorIndex) {
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

    async updateLabelConfiguration(rowIndex, labelName, colorIndex) {
        const elements = this.labelConfigurationsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        const labelNameField = row.$('input[data-type="labelName"]');
        await labelNameField.clear();
        await labelNameField.sendKeys(labelName);
        const colors = row.all(by.css('ul li span'));
        await colors.get(colorIndex).click();

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
