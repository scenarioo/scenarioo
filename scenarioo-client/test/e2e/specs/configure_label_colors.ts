'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import HomePage from '../webPages/homePage';
import LabelConfigurationsPage from '../webPages/labelConfigurationsPage';

useCase('Configure label colors')
    .description('Each label string can be configured to be displayed in a certain color.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Create, edit and delete label configurations')
            .it(async () => {
                await LabelConfigurationsPage.navigateToPage();
                await step('show label configurations');

                await LabelConfigurationsPage.assertNumConfigurations(0);

                await LabelConfigurationsPage.addLabelConfiguration('corner-case', 5);
                await step('add label configuration');

                await HomePage.goToPage();
                await step('navigate away from the label config page to some other page');

                await LabelConfigurationsPage.navigateToPage();
                await LabelConfigurationsPage.assertNumConfigurations(1);
                await step('go back to label config page, label is still there');

                await LabelConfigurationsPage.updateLabelConfiguration(0, 'updated', 4);
                await step('update label configuration');

                await LabelConfigurationsPage.deleteLabelConfiguration(0);

                await LabelConfigurationsPage.navigateToPage();
                await LabelConfigurationsPage.assertNumConfigurations(0);
            });

    });
