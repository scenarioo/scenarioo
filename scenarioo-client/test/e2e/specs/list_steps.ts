'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import ScenarioPage from '../pages/scenarioPage';
import NavigationPage from '../pages/navigationPage';

useCase('List steps of scenario')
    .description('Gives an overview of all steps in a scenario.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('ScenarioPage with comparisons')
            .description('Displaying diff info icons.')
            .labels(['diff-viewer'])
            .it(async () => {
                await Utils.navigateToRoute('/scenario/Donate/find_donate_page?branch=wikipedia-docu-example&build=2014-03-19');
                await NavigationPage.chooseComparison('To Projectstart');
                await ScenarioPage.expandAllPages();
                await ScenarioPage.assertFirstChangedPageDiffIconHasValue();
                await ScenarioPage.assertFirstChangedStepDiffIconHasValue();
                await ScenarioPage.assertAddedStepDiffIconTextEqualsAdded();
                await ScenarioPage.assertRemovedStepDiffIconTextEqualsRemoved();
                await ScenarioPage.assertAddedPageDiffIconTextEqualsAdded();
                await ScenarioPage.assertRemovedPageDiffIconTextEqualsRemoved();
                await step('Display one scenario');
                await NavigationPage.disableComparison();
            });
    });
