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

BreadcrumpPage.prototype.clickOnBreadcrumb = function(breadcrumpId) {
    var useCaseElement =  this.breadcrumbs.findElement(by.id(breadcrumpId));
    var ptor = protractor.getInstance();

    ptor.actions().click(useCaseElement).perform();
};

BreadcrumpPage.prototype.assertUseCaseNameInBreadcrumb = function(breadcrumpId, useCaseName) {
    var useCaseElement =  this.breadcrumbs.findElement(by.id(breadcrumpId));
    expect(useCaseElement.getText()).toContain(useCaseName);
};

BreadcrumpPage.prototype.assertThatTooltipIsShown = function(toolTipId, toolTipText){
    var toolTipElement =  this.breadcrumbs.findElement(by.id(toolTipId));
    var toolTipAttribute = toolTipElement.getAttribute('tooltip');

    expect(toolTipAttribute).toBe(toolTipText);
};

module.exports = BreadcrumpPage;