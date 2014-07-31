'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function BreadcrumpPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.breadcrumbs = element(by.css('.breadcrumb'));
}

util.inherits(BreadcrumpPage, BaseWebPage);

BreadcrumpPage.prototype.clickOnBreadcrump = function (breadcrumpId) {
    this.breadcrumbs.findElement(by.id(breadcrumpId)).then(function (element) {
        element.click();
    });
};

BreadcrumpPage.prototype.getUsecaseName = function (breadcrumpId) {
    this.breadcrumbs.findElement(by.id(breadcrumpId)).then(function (element) {
        return element.linkText;
    });
};

BreadcrumpPage.prototype.assertUseCaseName = function (usecaseName) {

};

module.exports = BreadcrumpPage;