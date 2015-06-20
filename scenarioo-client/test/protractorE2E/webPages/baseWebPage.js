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


BaseWebPage.prototype.clickBrowserBackButton = function () {
    e2eUtils.clickBrowserBackButton();
};

BaseWebPage.prototype.assertElementIsEnabled = function (elementId) {
    var htmlElement = this.stepNavigation.element(by.id(elementId));
    expect(htmlElement.isEnabled());
};

BaseWebPage.prototype.assertElementIsDisabled = function (elementId) {
    expect(this.stepNavigation.element(by.id(elementId)).isEnabled()).toBeFalsy();
};

BaseWebPage.prototype.clickElementById = function (elementId) {
    element(by.id(elementId)).click();
};

BaseWebPage.prototype.type = function (value) {
    element(by.css('body')).sendKeys(value);
};

/**
 * Navigate browser to a speficied path. By default (if not parameter is speficied) the path of the Page Object isused.
 *
 * @param path Overrides the default path of the page object. Specify only the part behind the # character.
 */
BaseWebPage.prototype.goToPage = function (path) {
    var targetPath;

    if (arguments.length === 1) {
        targetPath = path;
    } else {
        targetPath = this.path;
    }

    e2eUtils.getRoute(targetPath);
};

BaseWebPage.prototype.initLocalStorage = function () {
    e2eUtils.initLocalStorage();
};

module.exports = BaseWebPage;
