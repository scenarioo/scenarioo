'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Create a new Issue', function () {

  scenarioo.describeScenario('Create a new issue successfully', function () {
    var homePage = new pages.homePage();

    browser.get('#/');
    homePage.closeScenariooInfoDialogIfOpen();

    homePage.selectIssuesTab();



  });

});
