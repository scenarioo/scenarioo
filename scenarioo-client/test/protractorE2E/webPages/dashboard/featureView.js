'use strict';

var DashboardView = require('./dashboardView.js'),
    util = require('util'),
    e2eUtils = require('../../util/util.js');

function FeatureView(overridePath) {
    if (overridePath && overridePath.length > 0) {
        DashboardView.call(this, overridePath);
    } else {
        DashboardView.call(this, '/feature');
    }
}

util.inherits(FeatureView, DashboardView);

module.exports = FeatureView;
