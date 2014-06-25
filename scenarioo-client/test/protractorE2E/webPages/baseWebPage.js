'use strict';

var e2eUtils = require('../util/util.js');


function BaseWebPage(path) {
    this.path = path;
}

BaseWebPage.prototype.assertPageIsDisplayed = function () {
    e2eUtils.assertRoute(this.path);
};


BaseWebPage.prototype.assertElementIsEnabled = function(elementId) {
    this.stepNavigation.findElement(by.id(elementId)).then(function(element) {
        expect(element.isEnabled());
    });
};

BaseWebPage.prototype.assertElementIsDisabled = function(elementId) {
    this.stepNavigation.findElement(by.id(elementId)).then(function(element) {
        expect(element.isDisabled);
    });
};

module.exports = BaseWebPage;