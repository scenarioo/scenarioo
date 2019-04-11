'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import CreateComparisonDialog from '../pages/createComparisonDialog';
import ComparisonsPage from '../pages/comparisonsPage';

const NUMBER_OF_COMPARISONS_IN_TEST = 15;

useCase('Create comparison')
    .description('Create a new comparison from last successful to a specific build.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Create new comparison with existing name')
            .description('A comparison with an existing name cannot be created')
            .it(async () => {
                await CreateComparisonDialog.clickCreateComparisonLink();
                await step('display the create comparison dialog');

                await CreateComparisonDialog.enterComparisonName('To Projectstart');
                await CreateComparisonDialog.assertComparisonAlreadyExistsError();
                await step('enter existing comparison name and  error displayed that comparison with that name already exists');
            });

        scenario('Creating comparison without comparison branch')
            .description('A comparison without a comparison branch cannot be created')
            .it(async () => {
                await CreateComparisonDialog.clickCreateComparisonLink();
                await step('display the create comparison dialog');

                await CreateComparisonDialog.enterComparisonName('Comparison');
                await step('enter comparison name');

                await CreateComparisonDialog.createBranch();
                await step('create branch pressed and error displayed that a comparison branch is needed');

                await CreateComparisonDialog.assertComparisonBranchNeededError();
            });

        scenario('Creating comparison with comparison branch')
            .description('A comparison with a comparison branch can be created')
            .it(async () => {

                await ComparisonsPage.goToPage();
                await ComparisonsPage.assertNumberOfComparisons(NUMBER_OF_COMPARISONS_IN_TEST);
                await step('comparisons page');

                await CreateComparisonDialog.clickCreateComparisonLink();
                await step('create comparison dialog opened');

                await CreateComparisonDialog.enterComparisonName('Comparison');
                await step('comparison name entered');

                await CreateComparisonDialog.openTargetBranchSelectionDropdown();
                await step('target branch dropdown opened');

                await CreateComparisonDialog.chooseTargetBranch('Development');
                await step('development branch selected');

                await CreateComparisonDialog.openTargetBuildSelectionDropdown();
                await step('target build dropdown opened');

                await CreateComparisonDialog.chooseTargetBuild('last successful');
                await step('target build selected');

                await CreateComparisonDialog.openComparisonBranchSelectionDropdown();
                await step('comparison branch dropdown opened');

                await CreateComparisonDialog.chooseComparisonBranch('Production');
                await step('production branch selected');

                await CreateComparisonDialog.openComparisonBuildSelectionDropdown();
                await step('comparison build dropdown opened');

                await CreateComparisonDialog.chooseComparisonBuild('2014-02-21');
                await step('comparison build selected');

                await CreateComparisonDialog.createBranch();
                await step('branch created');

                await ComparisonsPage.clickRefreshLink();
                await ComparisonsPage.assertNumberOfComparisons(NUMBER_OF_COMPARISONS_IN_TEST + 1);
                await step('comparisons refreshed and asserted');
            });
    });
