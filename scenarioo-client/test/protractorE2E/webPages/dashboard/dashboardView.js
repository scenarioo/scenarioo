'use strict';

var BaseWebPage = require('./../baseWebPage.js'),
    util = require('util'),
    e2eUtils = require('../../util/util.js');

function DashboardView(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/feature');
    }

    this.featureViewTab = element(by.css('[name=feature]'));
    this.mapViewTab = element(by.css('[name=dashboard]'));
    this.docuViewTab = element(by.css('[name=detailNav]'));
    this.scenariosViewTab = element(by.css('[name=testScenarios]'));
}

util.inherits(DashboardView, BaseWebPage);

module.exports = DashboardView;
