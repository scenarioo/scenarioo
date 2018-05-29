'use strict';

import { scenario, step, useCase } from "scenarioo-js";
import * as Utils from "../util/util";

var scenarioo = require('scenarioo-js');
var pages = require('./../webPages');

var BRANCH_WIKI = 'Production';
var NUMBER_OF_ALIASES_IN_CONFIG = 2;
var FIRST_TEST_ALIAS_INDEX = NUMBER_OF_ALIASES_IN_CONFIG;

useCase('Use branch aliases')
    .description('Select a branch by using an alias')
    .describe(function () {

        var homePage = new pages.homePage();
        var branchAliasesPage = new pages.branchAliasesPage();
        var usecasePage = new pages.usecasePage();
        var scenarioPage = new pages.scenarioPage();
        var stepPage = new pages.stepPage();
        var navigationPage = new pages.navigationPage();

        beforeEach(async function () {
            await Utils.startScenariooRevisited();
        });

        scenario('Select branch by alias')
            .description('Create an alias and assert browsing through steps works')
            .it(async function () {

                Utils.navigateToRoute();
                branchAliasesPage.enterAlias('Latest dev', 'wikipedia-docu-example', 'alias to latest development release');
                branchAliasesPage.save();
                step('Create new branch alias');

                navigationPage.chooseBranch('Latest dev');
                step('choose branch alias');

                Utils.navigateToRoute();
                homePage.selectUseCase(1);
                usecasePage.selectScenario(0);
                scenarioPage.openStepByName('Step 1: Wikipedia Suche');
                stepPage.assertPreviousStepIsDisabled();
                step('browse step using branch alias');

                // Restore initial state for other tests
                Utils.navigateToRoute();
                branchAliasesPage.deleteAlias(FIRST_TEST_ALIAS_INDEX);
                branchAliasesPage.save();
                navigationPage.chooseBranch(BRANCH_WIKI);
            });

    });
