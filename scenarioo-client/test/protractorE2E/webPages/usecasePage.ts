'use strict';

import { by, ElementFinder, $ } from 'protractor';
import * as Utils from '../util/util';

export default class UsecasePage {

    private static scenarioTable: ElementFinder = $('table.scenario-table');

    static async selectScenario(scenarioIndex) {
        const elements = this.scenarioTable.all(by.css('tbody tr'));
        return elements.get(scenarioIndex).click();
    }

    static async sortByChanges() {
        return this.scenarioTable.$('th.sort-diff-info').click();
    }

    static async assertNumberOfDiffInfos(count) {
        const elements = this.scenarioTable.all(by.css('.diff-info-wrapper'));
        return expect(elements.count()).toBe(count);
    }

    static async assertLastUseCase(lastName) {
        return Utils.assertTextPresentInElement(this.scenarioTable.element(by.css('tr:last-of-type td:nth-of-type(2)')), lastName);
    }

    static async assertFirstUseCase(firstName) {
        return Utils.assertTextPresentInElement(this.scenarioTable.element(by.css('tr:first-of-type td:nth-of-type(2)')), firstName);
    }

    static async assertNoDiffInfoDisplayed() {
        return expect($('.sort-diff-info').isPresent()).toBeFalsy();
    }

}
