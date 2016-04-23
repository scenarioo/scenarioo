'use strict';

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

useCase('Configure label colors')
    .description('Each label string can be configured to be displayed in a certain color.')
    .describe(function () {

        var labelConfigurationsPage = new pages.labelConfigurationsPage();
        var homePage = new pages.homePage();

        beforeEach(function () {
            new pages.homePage().initLocalStorage();
        });

        scenario('Create, edit and delete label configurations')
            .it(function () {

                labelConfigurationsPage.goToPage();
                step('show label configurations');

                labelConfigurationsPage.assertNumConfigurations(0);

                labelConfigurationsPage.addLabelConfiguration('corner-case', 5);
                step('add label configuration');

                homePage.goToPage();
                step('navigate away from the label config page to some other page');

                labelConfigurationsPage.goToPage();
                labelConfigurationsPage.assertNumConfigurations(1);
                step('go back to label config page, label is still there');

                labelConfigurationsPage.updateLabelConfiguration(0, 'updated', 4);
                step('update label configuration');

                labelConfigurationsPage.deleteLabelConfiguration(0);

                labelConfigurationsPage.goToPage();
                labelConfigurationsPage.assertNumConfigurations(0);

            });

    });
