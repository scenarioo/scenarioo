'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Create a new Issue', function () {

  scenarioo.describeScenario('Create a new issue successfully', function () {
    var homePage = new pages.homePage();

    browser.get('#/');
    homePage.closeScenariooInfoDialogIfOpen();

    homePage.selectIssuesTab();

    var numberOfIssues = 0;
    var issuesList = element(by.css('table')).findElements(by.css('tbody tr')).then(function (elements) {
      this.numberOfIssues = elements.length;
    });


    var newIssueName = element(by.model("issueName"));
    var newIssueDesc = element(by.id("issueDescription"));

    newIssueName.sendKeys("This is my Test issue name" + Date.now());
    newIssueDesc.sendKeys("And this is the description");

    var saveButton = element(by.id('addBtn'));

    saveButton.click();

    issuesList = element(by.css('table')).findElements(by.css('tbody tr')).then(function (elements) {
      expect(elements.length).toBe(this.numberOfIssues + 1);
    });

  });

  scenarioo.describeScenario('Create a new issue with a too long name', function () {
    var homePage = new pages.homePage();

    browser.get('#/');
    homePage.closeScenariooInfoDialogIfOpen();

    homePage.selectIssuesTab();

    var numberOfIssues = 0;
    var issuesList = element(by.css('table')).findElements(by.css('tbody tr')).then(function (elements){
      this.numberOfIssues = elements.length;
    });


    var newIssueName = element(by.model("issueName"));
    var newIssueDesc = element(by.id("issueDescription"));

    newIssueName.sendKeys("This is my Test issue name which is going to be way too long" +
                          "because I make it way too long in order to test that it does not" +
                          "get created as we do not want names to be so long" +
                          "although eventually this test should be wrong since we can shorten names" +
                          "once we start using the longobjectnamesresolver");
    newIssueDesc.sendKeys("And this is the description");

    var saveButton = element(by.id('addBtn'));

    saveButton.click();

    issuesList = element(by.css('table')).findElements(by.css('tbody tr')).then(function (elements){
      expect(elements.length).toBe(this.numberOfIssues);
    });

  });

});
