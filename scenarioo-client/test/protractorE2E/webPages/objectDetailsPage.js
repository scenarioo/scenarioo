'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util');

function ObjectDetailsPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }
}

util.inherits(ObjectDetailsPage, BaseWebPage);

ObjectDetailsPage.prototype.clickNthTreeTableRow = function (rowNumberWithoutHeader) {
    element.all(by.css('#treeviewtable tbody tr')).then(function(elements) {
        var nthRow = elements[rowNumberWithoutHeader];
        var link = nthRow.element(by.css('span'));
        link.click();
    });
};

ObjectDetailsPage.prototype.clickToExpand = function (nodeId) {
    var node = element(by.id('node_' + nodeId));
    var imageId = 'img_' + nodeId;
    expect(node.isDisplayed()).toBe(true);

    var imageElement = node.element(by.id(imageId));
    imageElement.click();
};

ObjectDetailsPage.prototype.assertTreeNodeStatus = function (nodeId, status) {
    var node = element(by.id('node_' + nodeId));
    var imageId = 'img_' + nodeId;
    expect(node.getText()).not.toBe(null);
    expect(node.isDisplayed()).toBe(true);
    var imageElement = node.element(by.id(imageId));
    var imgAttribute = imageElement.getAttribute('src');
    expect(imgAttribute).toBe(browser.params.baseUrl + '/images/' + status + '.png');
};

ObjectDetailsPage.prototype.assertNumberOfRows = function(expectedRowCount) {
    element(protractor.By.tagName('tbody')).all(protractor.By.tagName('tr')).then(function(rows){
        expect(rows.length).toBe(expectedRowCount);
    });
};

ObjectDetailsPage.prototype.enterSearchCriteria = function(searchCriteria) {
    var searchField = element(by.name('searchCriteria'));
    searchField.sendKeys(searchCriteria);
};

ObjectDetailsPage.prototype.assertRowToContainTextAndBeDisplayed = function(row, text) {
    var matchElement = element(by.id('node_' + row));
    expect(matchElement.getText()).toContain(text);
    expect(matchElement.isDisplayed()).toBeTruthy();
};

ObjectDetailsPage.prototype.resetSearchCriteriaWithEsc = function() {
    var searchField = element(by.name('searchCriteria'));
    searchField.sendKeys(protractor.Key.ESCAPE);
};

ObjectDetailsPage.prototype.clickCollapseAll = function() {
    var toggleButton = element(by.id('toggleButton'));
    toggleButton.click();
    expect(toggleButton.getText()).toContain('expand all');
};

ObjectDetailsPage.prototype.doubleClickOnNode = function(nodeId) {
    var node = element(by.id('node_' + nodeId));
    var imageId = 'img_' + nodeId;
    var imageElement = node.element(by.id(imageId));

    imageElement.click();
    browser.actions().doubleClick(imageElement).perform();
};

ObjectDetailsPage.prototype.assertTreeNodeIsDisplayed = function(nodeId) {
    var node = element(by.id('node_' + nodeId));
    expect(node.isDisplayed()).toBeTruthy();
    expect(node.getText()).not.toBe(null);
};

module.exports = ObjectDetailsPage;
