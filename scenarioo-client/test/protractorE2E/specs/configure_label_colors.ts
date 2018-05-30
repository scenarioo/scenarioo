'use strict';

import { scenario, step, useCase } from "scenarioo-js";
import * as Utils from "../util/util";
import LabelConfigurationsPage from "../webPages/labelConfigurationsPage";

useCase('Configure label colors')
    .description('Each label string can be configured to be displayed in a certain color.')
    .describe(function () {

        beforeEach(async function () {
            await Utils.startScenariooRevisited();
        });

        scenario('Create, edit and delete label configurations')
            .it(async function () {
                await LabelConfigurationsPage.navigateToPage();
                step('show label configurations');

                await LabelConfigurationsPage.assertNumConfigurations(0);

                await LabelConfigurationsPage.addLabelConfiguration('corner-case', 5);
                step('add label configuration');

                await Utils.navigateToRoute("/");
                step('navigate away from the label config page to some other page');

                await LabelConfigurationsPage.navigateToPage();
                await LabelConfigurationsPage.assertNumConfigurations(1);
                step('go back to label config page, label is still there');

                await LabelConfigurationsPage.updateLabelConfiguration(0, 'updated', 4);
                step('update label configuration');

                await LabelConfigurationsPage.deleteLabelConfiguration(0);

                await LabelConfigurationsPage.navigateToPage();
                await LabelConfigurationsPage.assertNumConfigurations(0);
            });

    });
