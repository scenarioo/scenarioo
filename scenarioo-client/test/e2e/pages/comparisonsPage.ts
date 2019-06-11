'use strict';

import {by, element, ElementFinder, $, $$} from 'protractor';
import * as Utils from '../util';

class ComparisonsPage {

    private comparisonsTable = $('table.table-responsive');

    async goToPage() {
        return Utils.navigateToRoute('/manage?tab=comparisons');
    }

    async getNumberOfComparisons() {
        const elements = $$('tbody tr');
        return await elements.count();
    }

    async assertNumberOfComparisons(expectedCount: number) {
        return Utils.assertNumberOfTableRows(this.comparisonsTable, expectedCount);
    }

    async refreshComparisons() {
        return Utils.clickElementById('refreshComparisons');
    }

    async resetComparisonsSearch() {
        return Utils.clickElementById('resetComparisonsSearchField');
    }

    async filterComparisons(comparisonName: string) {
        const comparisonsSearchField = element(by.id('comparisonsSearchField'));
        return comparisonsSearchField.sendKeys(comparisonName);
    }

    async assertComparisonStatus(rowIndex: number, status: string) {
        const elements = $$('tbody tr');
        const row = elements.get(rowIndex);
        const cells = row.all(by.tagName('td'));
        return expect(cells.get(5).getText()).toBe(status);
    }

    async recalculateComparison(rowIndex: number) {
        const elements = $$('tbody tr');
        const row = elements.get(rowIndex);
        return row.element(by.linkText('Recalculate')).click();
    }

    async openComparisonDetails(rowIndex: number) {
        const elements = $$('tbody tr');
        const row = elements.get(rowIndex);
        return row.element(by.partialLinkText('Details')).click();
    }
}
export default new ComparisonsPage();
