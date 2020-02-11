'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import HomePage from '../pages/homePage';
import LabelConfigurationsPage from '../pages/labelConfigurationsPage';

useCase('Configure label colors')
    .description('Each label string can be configured to be displayed in a certain color.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.clearLocalStorageAndSetPreviouslyVisited();
        });

        scenario('Create, edit and delete label configurations')
            .it(async () => {

                // Given number of preconfigured colors (in config.xml for demo)
                const numberOfPreconfiguredColors = 3;

                await LabelConfigurationsPage.navigateToPage();
                await step('show label configurations');

                await LabelConfigurationsPage.assertNumConfigurations(numberOfPreconfiguredColors);

                await LabelConfigurationsPage.addLabelConfiguration('added-label', 5);
                await step('add label configuration');

                await HomePage.goToPage();
                await step('navigate away from the label config page to some other page');

                await LabelConfigurationsPage.navigateToPage();
                await LabelConfigurationsPage.assertNumConfigurations(numberOfPreconfiguredColors + 1);
                await step('go back to label config page, label is still there');

                await LabelConfigurationsPage.updateLabelConfiguration(0, 'updated-label', 4);
                await step('label configuration updated');

                await LabelConfigurationsPage.deleteLastLabelConfiguration(4);  // delete the just added one
                await step('label configuration deleted');

                await LabelConfigurationsPage.navigateToPage();
                await LabelConfigurationsPage.assertNumConfigurations(numberOfPreconfiguredColors);
                await step('expected number of label configs on revisit');

            });

    });
