'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function ObjectDetailsPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.objectDetailsPage = element(by.id('treeviewtable'));
}

util.inherits(ObjectDetailsPage, BaseWebPage);

ObjectDetailsPage.prototype.clickNthTreeTableRow = function (rowNumberWithoutHeader) {
    this.objectDetailsPage.findElements(by.css('tbody tr')).then(function(elements) {
        var nthRow = elements[rowNumberWithoutHeader + 1]; // + 1 because 0th row is the header
        var link = nthRow.findElement(by.css('span'));
        link.click();
    });
};

ObjectDetailsPage.prototype.clickToExpand = function (nodeId) {
    var element = this.objectDetailsPage.findElement(by.id('node_' + nodeId));
    var imageId = 'img_' + nodeId;
    expect(element.isDisplayed()).toBe(true);

    var imageElement = element.findElement(by.id(imageId));
    imageElement.click();
};

ObjectDetailsPage.prototype.assertTreeNodeStatus = function (nodeId, status) {
    var element = this.objectDetailsPage.findElement(by.id('node_' + nodeId));
    var ptor = protractor.getInstance();
    var imageId = 'img_' + nodeId;
    expect(element.getText()).not.toBe(null);
    expect(element.isDisplayed()).toBe(true);

    ptor.findElement(protractor.By.tagName('tbody')).findElements(protractor.By.tagName('tr')).then(function(rows){
        expect(rows.length).toBe(27);
    });

    var imageElement = element.findElement(by.id(imageId));
    var imgAttribute = imageElement.getAttribute('src');
    expect(imgAttribute).toBe(browser.params.baseUrl + '/images/' + status + '.png');
};

ObjectDetailsPage.prototype.enterSearchCriteria = function(searchCriteria) {
    var ptor = protractor.getInstance();
    ptor.findElement(by.name('searchCriteria')).then(function(searchField) {
        searchField.sendKeys(searchCriteria);
    });

    var matchElement = this.objectDetailsPage.findElement(by.id('node_' + '4'));
    expect(matchElement.getText()).toContain('multiple results');
    expect(matchElement.isDisplayed()).toBeTruthy();
};

ObjectDetailsPage.prototype.resetSearchCriteriaWithEsc = function() {
    var ptor = protractor.getInstance();
    ptor.actions().sendKeys(protractor.Key.ESCAPE).perform();
};

ObjectDetailsPage.prototype.clickCollapseAll = function() {
    var ptor = protractor.getInstance();
    ptor.findElement(by.id('toggleButton')).then(function (element) {
        element.click();
        expect(element.getText()).toContain('expand all');
    });
};

ObjectDetailsPage.prototype.doubleClickOnNode = function(nodeId) {
    var element = this.objectDetailsPage.findElement(by.id('node_' + nodeId));
    var ptor = protractor.getInstance();
    var imageId = 'img_' + nodeId;
    var imageElement = element.findElement(by.id(imageId));

    imageElement.click();
    ptor.actions().doubleClick(imageElement).perform();
};

ObjectDetailsPage.prototype.assertTreeNodeIsDisplayed = function(nodeId) {
    var ptor = protractor.getInstance();
    ptor.findElement(by.id('node_' + nodeId)).then(function (element) {
        expect(element.isDisplayed()).toBeTruthy();
        expect(element.getText()).not.toBe(null);
    });
};

module.exports = ObjectDetailsPage;
