'use strict';

var BaseWebPage = require('./baseWebPage.js'),
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

BreadcrumbPage.prototype.assertBreadcrumbElementText = function(breadcrumbId, featureName) {
    //var featureElement =  this.breadcrumbs.element(by.id(breadcrumbId));
    //expect(featureElement.getText()).toContain(featureName);
};

BreadcrumbPage.prototype.assertThatTooltipIsShown = function(toolTipId, toolTipText){
    var toolTipElement =  this.breadcrumbs.element(by.id(toolTipId));
    //var toolTipAttribute = toolTipElement.getAttribute('tooltip');

    //expect(toolTipAttribute).toBe(toolTipText);
};

module.exports = BreadcrumbPage;
