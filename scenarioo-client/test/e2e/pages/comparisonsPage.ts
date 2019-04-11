'use strict';

import {by, element, ElementFinder, $, $$} from 'protractor';
import * as Utils from '../util';

class ComparisonsPage {

    private comparisonsTable = $('table.table-responsive');

    async goToPage() {
        return Utils.navigateToRoute('/manage?tab=comparisons');
    }

    async getNumberOfComparisons() {
        const elements = this.comparisonsTable.all(by.css('tbody tr'));
        return await elements.count();
    }

    async assertNumberOfComparisons(expectedCount) {
        return Utils.assertNumberOfTableRows(this.comparisonsTable, expectedCount);
    }

    async clickRefreshLink() {
        return Utils.clickElementById('refreshComparisons');
    }

    async clickResetButton() {
        return Utils.clickElementById('resetComparisonsSearchField');
    }

    async filterComparisons(comparisonName) {
        const comparisonsSearchField = element(by.id('comparisonsSearchField'));
        return comparisonsSearchField.sendKeys(comparisonName);
    }

    async assertComparisonStatus(rowIndex, status) {
        const elements = this.comparisonsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        const cells = row.all(by.tagName('td'));
        return expect(cells.get(5).getText()).toBe(status);
    }

    async recalculateComparison(rowIndex) {
        const elements = this.comparisonsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        return row.element(by.linkText('Recalculate')).click();
    }

    async openComparisonDetails(rowIndex) {
        const elements = this.comparisonsTable.all(by.css('tbody tr'));
        const row = elements.get(rowIndex);
        return row.element(by.partialLinkText('Details')).click();
    }
}
export default new ComparisonsPage();
