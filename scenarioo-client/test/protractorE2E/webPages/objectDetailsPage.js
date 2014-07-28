'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function ObjectDetailsPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.branchAliasTable = element(by.id('treeviewtable'));
}

util.inherits(ObjectDetailsPage, BaseWebPage);

ObjectDetailsPage.prototype.clickNthTreeTableRow = function (rowNumberWithoutHeader) {
    this.branchAliasTable.findElements(by.css('tbody tr')).then(function(elements) {
        var nthRow = elements[rowNumberWithoutHeader + 1]; // + 1 because 0th row is the header
        var link = nthRow.findElement(by.css('span'));
        link.click();
    });
};

module.exports = ObjectDetailsPage;
