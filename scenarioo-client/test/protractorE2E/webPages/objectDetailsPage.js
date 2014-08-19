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

ObjectDetailsPage.prototype.assertElementIsExpanded = function (elementId) {
    var element = this.objectDetailsPage.findElement(by.id('0'));
    /*expect(element.isVisible).toBe(true);

    var imgElement = element.getElementsByTagName('img');
    expect(imgElement.attribute('src')).toBe('images/expanded.png');*/
};

module.exports = ObjectDetailsPage;
