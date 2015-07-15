'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util'),
    e2eUtils = require('../util/util.js');

function EditorPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.saveButton = element(by.id('save'));
    this.issueSaved = element(by.css('.alert'));

}

util.inherits(EditorPage, BaseWebPage);


EditorPage.prototype.assertPageIsDisplayed = function () {
    expect(this.saveButton.isDisplayed()).toBe(true);
};

EditorPage.prototype.assertIssueSaved = function() {
    expect(this.issueSaved.isDisplayed()).toBe(true);
};

EditorPage.prototype.assertIssueNotSaved = function() {
    expect(this.issueSaved.isDisplayed()).toBe(false);
};

module.exports = EditorPage;
