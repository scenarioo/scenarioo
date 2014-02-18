'use strict';

var e2eUtils = require('../util/util.js');


function BaseWebPage(path) {
    this.path = path;
}

BaseWebPage.prototype.assertPageIsDisplayed = function () {
    e2eUtils.assertRoute(this.path);
};

module.exports = BaseWebPage;