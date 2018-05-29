'use strict';
import {by, element} from "protractor";

var BaseWebPage = require('./baseWebPage'),
    util = require('util');

function BreadcrumbPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.breadcrumbs = element(by.css('.breadcrumb'));
}

util.inherits(BreadcrumbPage, BaseWebPage);

BreadcrumbPage.prototype.clickOnBreadcrumb = function(breadcrumbId) {
    this.breadcrumbs.element(by.id(breadcrumbId)).click();
};

BreadcrumbPage.prototype.assertBreadcrumbElementText = function(breadcrumbId, useCaseName) {
    var useCaseElement =  this.breadcrumbs.element(by.id(breadcrumbId));
    expect(useCaseElement.getText()).toContain(useCaseName);
};

BreadcrumbPage.prototype.assertThatTooltipIsShown = function(toolTipId, toolTipText){
    var toolTipElement =  this.breadcrumbs.element(by.id(toolTipId));
    var toolTipAttribute = toolTipElement.getAttribute('uib-tooltip');

    expect(toolTipAttribute).toBe(toolTipText);
};

module.exports = BreadcrumbPage;
