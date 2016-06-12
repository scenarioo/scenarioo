'use strict';

var BaseWebPage = require('./baseWebPage.js'),
    util = require('util'),
    e2eUtils = require('../util/util.js');

function UsecasePage(overridePath) {
    if (overridePath && overridePath.length > 0) {
        BaseWebPage.call(this, overridePath);
    } else {
        BaseWebPage.call(this, '/');
    }

    this.scenarioTable = element(by.css('table.scenario-table'));

}

util.inherits(UsecasePage, BaseWebPage);

UsecasePage.prototype.selectScenario = function(scenarioIndex) {
    this.scenarioTable.all(by.css('tbody tr')).then(function(elements) {
        elements[scenarioIndex].click();
    });
};

UsecasePage.prototype.clickSortByChanges = function(){
    this.scenarioTable.element(by.css('th.sort-diff-info')).click();
};

UsecasePage.prototype.assertNumberOfDiffInfos = function(count){
    this.scenarioTable.all(by.css('.diff-info-wrapper')).then(function (elements) {
        expect(elements.length).toBe(count);
    });
};

UsecasePage.prototype.assertValueOfFirstDiffInfo = function(value){
    var changeRateSpan = this.scenarioTable.element(by.css('.diff-info-wrapper:first-of-type .change-rate span'));
    e2eUtils.assertTextPresentInElement(changeRateSpan, value);
};

UsecasePage.prototype.assertNoDiffInfoDisplayed = function () {
    expect(element(by.css('.sort-diff-info')).isPresent()).toBeFalsy();
};

module.exports = UsecasePage;
