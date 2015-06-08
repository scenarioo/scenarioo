'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util');

function UsecasePage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.stepView = element(by.css('table.scenario-table'));

}

util.inherits(UsecasePage, BaseWebPage);

UsecasePage.prototype.selectScenario = function(scenarioIndex) {
    this.stepView.all(by.css('tbody tr')).then(function(elements) {
        elements[scenarioIndex].click();
    });
};

module.exports = UsecasePage;
