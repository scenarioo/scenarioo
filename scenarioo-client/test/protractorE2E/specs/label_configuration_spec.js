'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

scenarioo.describeUseCase('Label configuration', function () {
    scenarioo.describeScenario('Create, edit and delete label configurations', function () {
        var labelConfigurationsPage = new pages.labelConfigurationsPage();
        var homePage = new pages.homePage();

        browser.get('#/manage?tab=labelConfigurations');
        scenarioo.docuWriter.saveStep('show label configurations');

        homePage.closeScenariooInfoDialogIfOpen();

        labelConfigurationsPage.assertNumConfigurations(0);

        labelConfigurationsPage.addLabelConfiguration('corner-case', 5);
        scenarioo.docuWriter.saveStep('add label configuration');

        browser.get('#/manage?tab=labelConfigurations');
        labelConfigurationsPage.assertNumConfigurations(1);

        labelConfigurationsPage.updateLabelConfiguration(0, 'updated', 4);
        scenarioo.docuWriter.saveStep('update label configuration');

        labelConfigurationsPage.deleteLabelConfiguration(0);

        browser.get('#/manage?tab=labelConfigurations');
        labelConfigurationsPage.assertNumConfigurations(0);
    });
});