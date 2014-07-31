'use strict';

var e2eUtils = require('../util/util.js'), BaseWebPage = require('./baseWebPage.js'), util = require('util');

function BreadcrumpPage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.breadcrumb = element(by.css('table.scenario-table'));

}

util.inherits(ObjectDetailsPage, BaseWebPage);


}
