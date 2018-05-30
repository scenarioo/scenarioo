'use strict';

import { browser, by, element, protractor } from 'protractor';
import * as Utils from '../util/util';

export default class ObjectDetailsPage {

    static async clickNthTreeTableRow(rowNumberWithoutHeader) {
        const elements = await element.all(by.css('#treeviewtable tbody tr'));
        const nthRow = elements[rowNumberWithoutHeader];
        const link = await nthRow.element(by.css('span'));
        link.click();
    }

    static async clickToExpand(nodeId) {
        const node = element(by.id('node_' + nodeId));
        const imageId = 'img_' + nodeId;
        await expect(node.isDisplayed()).toBe(true);

        const imageElement = node.element(by.id(imageId));
        await Utils.waitForElementVisible(imageElement);
        imageElement.click();
    }

    static async assertTreeNodeStatus(nodeId, status) {
        const node = element(by.id('node_' + nodeId));
        const imageId = 'img_' + nodeId;
        await expect(node.getText()).not.toBe(null);
        await expect(node.isDisplayed()).toBe(true);
        const imageElement = node.element(by.id(imageId));
        const imgAttribute = imageElement.getAttribute('src');
        return expect(imgAttribute).toBe(browser.params.baseUrl + '/images/' + status + '.png');
    }

    static async assertNumberOfRows(expectedRowCount) {
        const rows = await element(protractor.By.tagName('tbody')).all(protractor.By.tagName('tr'));
        return expect(rows.length).toBe(expectedRowCount);
    }

    static async enterSearchCriteria(searchCriteria) {
        const searchField = element(by.name('searchCriteria'));
        return searchField.sendKeys(searchCriteria);
    }

    static async assertRowToContainTextAndBeDisplayed(row, text) {
        const matchElement = element(by.id('node_' + row));
        await expect(matchElement.getText()).toContain(text);
        return expect(matchElement.isDisplayed()).toBeTruthy();
    }

    static async resetSearchCriteriaWithEsc() {
        const searchField = element(by.name('searchCriteria'));
        return searchField.sendKeys(protractor.Key.ESCAPE);
    }

    static async clickCollapseAll() {
        const toggleButton = element(by.id('toggleButton'));
        await toggleButton.click();
        return expect(toggleButton.getText()).toContain('expand all');
    }

    static async doubleClickOnNode(nodeId) {
        const node = element(by.id('node_' + nodeId));
        const imageId = 'img_' + nodeId;
        const imageElement = node.element(by.id(imageId));

        await Utils.waitForElementVisible(imageElement);

        await imageElement.click();
        return browser.actions().doubleClick(imageElement).perform();
    }

    static async assertTreeNodeIsDisplayed(nodeId) {
        const node = element(by.id('node_' + nodeId));
        await expect(node.isDisplayed()).toBeTruthy();
        await expect(node.getText()).not.toBe(null);
    }

}
