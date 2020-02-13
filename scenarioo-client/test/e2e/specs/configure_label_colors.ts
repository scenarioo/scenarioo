'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import HomePage from '../pages/homePage';
import labelConfigurationsPage from '../pages/labelConfigurationsPage';

useCase('Configure label colors')
    .description('Each label string can be configured to be displayed in a certain color.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.clearLocalStorageAndSetPreviouslyVisited();
        });

        scenario('Create, edit and delete label configurations')
            .it(async () => {
                // Given number of preconfigured colors (in config.xml for demo)
                const numberOfPreconfiguredColors: number = 3;

                await labelConfigurationsPage.navigateToPage();
                await step('show label configurations');

                await labelConfigurationsPage.assertNumConfigurations(numberOfPreconfiguredColors);

                await labelConfigurationsPage.addLabelConfigurationWithPresetColor('added-label', 5);
                await step('add label configuration');

                await HomePage.goToPage();
                await step('navigate away from the label config page to some other page');

                await labelConfigurationsPage.navigateToPage();
                await labelConfigurationsPage.assertNumConfigurations(numberOfPreconfiguredColors + 1);
                await step('go back to label config page, label is still there');

                await labelConfigurationsPage.updateLabelConfigurationWithPresetColor(0, 'updated-label', 4);
                await step('label configuration updated');

                await labelConfigurationsPage.deleteLabelConfiguration(3, numberOfPreconfiguredColors + 1);  // delete the just added configuration
                await step('label configuration deleted');

                await labelConfigurationsPage.navigateToPage();
                await labelConfigurationsPage.assertNumConfigurations(numberOfPreconfiguredColors);
                await step('expected number of label configs on revisit');

            });

        scenario('Create a label configuration with a custom color')
            .it(async () => {
                const customLabelColor: string = '#00FF00';
                const numberOfPreconfiguredColors: number = 3;

                await labelConfigurationsPage.navigateToPage();
                await step('show label configurations');

                await labelConfigurationsPage.addLabelConfigurationWithCustomColor('added-label', customLabelColor);
                await step('add label configuration with a custom color');

                await HomePage.goToPage();
                await step('navigate away from the label config page to some other page');

                await labelConfigurationsPage.navigateToPage();
                await labelConfigurationsPage.assertNumConfigurations(numberOfPreconfiguredColors + 1);
                await labelConfigurationsPage.assertConfigurationColor(numberOfPreconfiguredColors, customLabelColor);
                await step('go back to label config page, label is still there and has the custom color');

            });

    });
