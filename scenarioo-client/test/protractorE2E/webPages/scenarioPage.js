'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function ScenarioPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.stepView = element(by.css('div.step-view'));

}

util.inherits(ScenarioPage, BaseWebPage);

ScenarioPage.prototype.openStepByName = function(stepName) {
    this.stepView.findElement(by.linkText(stepName)).then(function(element) {
        element.click();
    });

} ;

module.exports = ScenarioPage;