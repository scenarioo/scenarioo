'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

describeUseCaseE('Configure label colors', {
    description: 'Each label string can be configured to be displayed in a certain color.'
}, function () {

    var labelConfigurationsPage = new pages.labelConfigurationsPage();
    var homePage = new pages.homePage();

    beforeEach(function(){
        new pages.homePage().initLocalStorage();
    });

    describeScenarioE('Create, edit and delete label configurations', function () {
        labelConfigurationsPage.goToPage();
        scenarioo.saveStep('show label configurations');

        labelConfigurationsPage.assertNumConfigurations(0);

        labelConfigurationsPage.addLabelConfiguration('corner-case', 5);
        scenarioo.saveStep('add label configuration');

        homePage.goToPage();
        scenarioo.saveStep('navigate away from the label config page to some other page');

        labelConfigurationsPage.goToPage();
        labelConfigurationsPage.assertNumConfigurations(1);
        scenarioo.saveStep('go back to label config page, label is still there');

        labelConfigurationsPage.updateLabelConfiguration(0, 'updated', 4);
        scenarioo.saveStep('update label configuration');

        labelConfigurationsPage.deleteLabelConfiguration(0);

        labelConfigurationsPage.goToPage();
        labelConfigurationsPage.assertNumConfigurations(0);
    });

});
