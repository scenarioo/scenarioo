'use strict';

import {$$, browser, by, element, protractor} from 'protractor';
import * as Utils from '../util/util';

class ObjectDetailsPage {

    async clickNthTreeTableRow(rowNumberWithoutHeader) {
        const elements = $$('#treeviewtable tbody tr');
        const nthRow = elements.get(rowNumberWithoutHeader);
        const link = nthRow.$('span');
        await link.click();
    }

    async clickToExpand(nodeId) {
        const node = element(by.id('node_' + nodeId));
        const imageId = 'img_' + nodeId;
        await expect(node.isDisplayed()).toBe(true);

        const imageElement = node.element(by.id(imageId));
        await Utils.waitForElementVisible(imageElement);
        return imageElement.click();
    }

    async assertTreeNodeStatus(nodeId, status) {
        const node = element(by.id('node_' + nodeId));
        const imageId = 'img_' + nodeId;
        await expect(node.getText()).not.toBe(null);
        await expect(node.isDisplayed()).toBe(true);
        const imageElement = node.element(by.id(imageId));
        const imgAttribute = imageElement.getAttribute('src');
        return expect(imgAttribute).toBe(browser.params.baseUrl + '/images/' + status + '.png');
    }

    async assertNumberOfRows(expectedRowCount) {
        const rows = element(protractor.By.tagName('tbody')).all(protractor.By.tagName('tr'));
        return expect(rows.count()).toBe(expectedRowCount);
    }

    async enterSearchCriteria(searchCriteria) {
        const searchField = element(by.name('searchCriteria'));
        return searchField.sendKeys(searchCriteria);
    }

    async assertRowToContainTextAndBeDisplayed(row, text) {
        const matchElement = element(by.id('node_' + row));
        await expect(matchElement.getText()).toContain(text);
        return expect(matchElement.isDisplayed()).toBeTruthy();
    }

    async resetSearchCriteriaWithEsc() {
        const searchField = element(by.name('searchCriteria'));
        return searchField.sendKeys(protractor.Key.ESCAPE);
    }

    async clickCollapseAll() {
        const toggleButton = element(by.id('toggleButton'));
        await toggleButton.click();
        return expect(toggleButton.getText()).toContain('expand all');
    }

    async doubleClickOnNode(nodeId) {
        const node = element(by.id('node_' + nodeId));
        const imageId = 'img_' + nodeId;
        const imageElement = node.element(by.id(imageId));

        await Utils.waitForElementVisible(imageElement);

        await imageElement.click();
        return browser.actions().doubleClick(imageElement).perform();
    }

    async assertTreeNodeIsDisplayed(nodeId) {
        const node = element(by.id('node_' + nodeId));
        await expect(node.isDisplayed()).toBeTruthy();
        await expect(node.getText()).not.toBe(null);
    }

}

export default new ObjectDetailsPage();
