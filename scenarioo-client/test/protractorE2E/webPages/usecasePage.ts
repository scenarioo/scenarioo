'use strict';

import { by, ElementFinder, $ } from 'protractor';
import * as Utils from '../util/util';

class UsecasePage {

    private scenarioTable = $('table.scenario-table');

    async selectScenario(scenarioIndex) {
        const elements = this.scenarioTable.all(by.css('tbody tr'));
        return elements.get(scenarioIndex).click();
    }

    async sortByChanges() {
        return this.scenarioTable.$('th.sort-diff-info').click();
    }

    async assertNumberOfDiffInfos(count) {
        const elements = this.scenarioTable.all(by.css('.diff-info-wrapper'));
        return expect(elements.count()).toBe(count);
    }

    async assertLastUseCase(lastName) {
        return Utils.assertTextPresentInElement(this.scenarioTable.element(by.css('tr:last-of-type td:nth-of-type(2)')), lastName);
    }

    async assertFirstUseCase(firstName) {
        return Utils.assertTextPresentInElement(this.scenarioTable.element(by.css('tr:first-of-type td:nth-of-type(2)')), firstName);
    }

    async assertNoDiffInfoDisplayed() {
        return expect($('.sort-diff-info').isPresent()).toBeFalsy();
    }

}

export default new UsecasePage();
