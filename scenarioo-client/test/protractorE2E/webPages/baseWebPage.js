'use strict';

var e2eUtils = require('../util/util.js');


function BaseWebPage(path) {
    this.path = path;
}

BaseWebPage.prototype.assertPageIsDisplayed = function () {
    e2eUtils.assertRoute(this.path);
};

BaseWebPage.prototype.assertRoute = function (expectedUrl) {
    e2eUtils.assertRoute(expectedUrl);
};


BaseWebPage.prototype.clickBrowserBackButton = function (rowNumberWithoutHeader) {
    e2eUtils.clickBrowserBackButton();
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

BaseWebPage.prototype.type = function(value) {
    element(by.css('body')).sendKeys(value);
};

module.exports = BaseWebPage;