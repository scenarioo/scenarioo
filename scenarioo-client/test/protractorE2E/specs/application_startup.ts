'use strict';

import { scenario, step, useCase } from "scenarioo-js";
import * as Utils from "../util/util"
import HomePage from "../webPages/homePage"

useCase('Application Startup')
    .description('The user is presented with the about dialog on his first Scenarioo visit.')
    .describe(function () {

        scenario('First visit')
            .description('About dialog open on first access to Scenarioo to welcome new user.')
            .it(async function () {
                await Utils.startScenariooFirstTimeVisit();
                step('About dialog is displayed on first access of Scenarioo');
                await HomePage.assertPageIsDisplayed();
                await HomePage.assertScenariooInfoDialogShown();
                await HomePage.closeScenariooInfoDialogIfOpen();
                await HomePage.assertScenariooInfoDialogNotShown();
                step('About dialog is closed');
            });

        scenario('Later visits')
            .description('About dialog not open when previously visited.')
            .it(async function () {
                await Utils.startScenariooRevisited();
                step('About dialog not visible for previous visitors');
                await HomePage.assertPageIsDisplayed();
                await HomePage.assertScenariooInfoDialogNotShown();
            });

    });
