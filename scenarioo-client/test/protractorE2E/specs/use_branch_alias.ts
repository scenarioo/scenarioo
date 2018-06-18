'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import HomePage from '../webPages/homePage';
import BranchAliasesPage from '../webPages/branchAliasesPage';
import UsecasePage from '../webPages/usecasePage';
import ScenarioPage from '../webPages/scenarioPage';
import StepPage from '../webPages/stepPage';
import NavigationPage from '../webPages/navigationPage';

const BRANCH_WIKI = 'Production';
const NUMBER_OF_ALIASES_IN_CONFIG = 2;
const FIRST_TEST_ALIAS_INDEX = NUMBER_OF_ALIASES_IN_CONFIG;

useCase('Use branch aliases')
    .description('Select a branch by using an alias')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Select branch by alias')
            .description('Create an alias and assert browsing through steps works')
            .it(async () => {
                await BranchAliasesPage.goToPage();
                await BranchAliasesPage.enterAlias('Latest dev', 'wikipedia-docu-example', 'alias to latest development release');
                await BranchAliasesPage.save();
                await step('Create new branch alias');

                await NavigationPage.chooseBranch('Latest dev');
                await step('choose branch alias');

                await HomePage.goToPage();
                await HomePage.selectUseCase(1);
                await UsecasePage.selectScenario(0);
                await ScenarioPage.openStepByName('Step 1: Wikipedia Suche');
                await StepPage.assertPreviousStepIsDisabled();
                await step('browse step using branch alias');

                // Restore initial state for other tests
                await BranchAliasesPage.goToPage();
                await BranchAliasesPage.deleteAlias(FIRST_TEST_ALIAS_INDEX);
                await BranchAliasesPage.save();
                await NavigationPage.chooseBranch(BRANCH_WIKI);
            });

    });
