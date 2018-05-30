'use strict';

import { by, element, ElementFinder } from 'protractor';

const Utils = require('../util/util');

export default class UsecasePage {

    private static scenarioTable: ElementFinder = element(by.css('table.scenario-table'));

    static async selectScenario(scenarioIndex) {
        const elements = await this.scenarioTable.all(by.css('tbody tr'));
        return elements[scenarioIndex].click();
    }

    static async sortByChanges(){
        return this.scenarioTable.element(by.css('th.sort-diff-info')).click();
    }

    static async assertNumberOfDiffInfos(count){
        const elements = await this.scenarioTable.all(by.css('.diff-info-wrapper'));
        return expect(elements.length).toBe(count);
    }

    static async assertLastUseCase(lastName) {
        return Utils.assertTextPresentInElement(this.scenarioTable.element(by.css('tr:last-of-type td:nth-of-type(2)')), lastName);
    }

    static async assertFirstUseCase(firstName) {
        return Utils.assertTextPresentInElement(this.scenarioTable.element(by.css('tr:first-of-type td:nth-of-type(2)')), firstName);
    }

    static async assertNoDiffInfoDisplayed() {
        return expect(element(by.css('.sort-diff-info')).isPresent()).toBeFalsy();
    }

}
