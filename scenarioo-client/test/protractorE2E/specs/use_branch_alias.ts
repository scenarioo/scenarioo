'use strict';

import { scenario, step, useCase } from "scenarioo-js";
import * as Utils from "../util/util";
import HomePage from "../webPages/homePage";
import BranchAliasesPage from "../webPages/branchAliasesPage";
import UsecasePage from "../webPages/usecasePage";
import ScenarioPage from "../webPages/scenarioPage";
import StepPage from "../webPages/stepPage";
import NavigationPage from "../webPages/navigationPage";

const BRANCH_WIKI = 'Production';
const NUMBER_OF_ALIASES_IN_CONFIG = 2;
const FIRST_TEST_ALIAS_INDEX = NUMBER_OF_ALIASES_IN_CONFIG;

useCase('Use branch aliases')
    .description('Select a branch by using an alias')
    .describe(function () {

        beforeEach(async function () {
            await Utils.startScenariooRevisited();
        });

        scenario('Select branch by alias')
            .description('Create an alias and assert browsing through steps works')
            .it(async function () {
                await BranchAliasesPage.goToPage();
                await BranchAliasesPage.enterAlias('Latest dev', 'wikipedia-docu-example', 'alias to latest development release');
                await BranchAliasesPage.save();
                step('Create new branch alias');

                await NavigationPage.chooseBranch('Latest dev');
                step('choose branch alias');

                await Utils.navigateToRoute("/");
                await HomePage.selectUseCase(1);
                await UsecasePage.selectScenario(0);
                await ScenarioPage.openStepByName('Step 1: Wikipedia Suche');
                await StepPage.assertPreviousStepIsDisabled();
                step('browse step using branch alias');

                // Restore initial state for other tests
                await BranchAliasesPage.goToPage();
                await BranchAliasesPage.deleteAlias(FIRST_TEST_ALIAS_INDEX);
                await BranchAliasesPage.save();
                await NavigationPage.chooseBranch(BRANCH_WIKI);
            });

    });
