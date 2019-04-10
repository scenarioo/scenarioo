'use strict';

import {by, element, ElementFinder, $, $$} from 'protractor';
import * as Utils from '../util';

class ComparisonsPage {

    private comparisonsTable = $('table.table-responsive');

    async goToPage() {
        return Utils.navigateToRoute('/manage?tab=comparisons');
    }

    async getNumberOfComparisons() {
        const rows = this.comparisonsTable.all(by.css('tbody tr'));
        return rows.count();
    }
    async assertNumberOfComparisons(expectedCount) {
        const rows = this.comparisonsTable.all(by.css('tbody tr'));
        // + 1 due to empty row
        return expect(rows.count()).toBe(expectedCount + 1);
    }
}

export default new ComparisonsPage();
