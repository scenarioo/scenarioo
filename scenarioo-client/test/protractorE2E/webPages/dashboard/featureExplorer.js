'use strict';

var DashboardView = require('./dashboardView.js'),
    util = require('util'),
    e2eUtils = require('../../util/util.js');

function FeatureExplorer(overridePath) {
    if (overridePath && overridePath.length > 0) {
        DashboardView.call(this, overridePath);
    } else {
        DashboardView.call(this, '/feature');
    }

    this.expandable = element(by.css('.navExpandable'));

    this.clickFeatureByFeatureId = function (featureId) {
        element(by.css('.click-'+featureId)).click();
    };
}

util.inherits(FeatureExplorer, DashboardView);

module.exports = FeatureExplorer;
