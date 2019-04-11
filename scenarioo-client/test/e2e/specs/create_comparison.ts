'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import CreateComparisonDialog from '../pages/createComparisonDialog';
import ComparisonsPage from '../pages/comparisonsPage';

const NUMBER_OF_COMPARISONS_IN_TEST = 15;

useCase('Create comparison')
    .description('Create new comparison from last successful to specific build.')
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
                await step('enter comparison name');

                await CreateComparisonDialog.assertComparisonAlreadyExistsError();
                await step('error displayed that comparison with that name already exists');
            });

        scenario('Creating comparison without comparison branch')
            .description('A comparison without a comparison branch cannot be created')
            .it(async () => {
                await CreateComparisonDialog.clickCreateComparisonLink();
                await step('display the create comparison dialog');

                await CreateComparisonDialog.enterComparisonName('Comparison');
                await step('enter comparison name');

                await CreateComparisonDialog.createBranch();
                await step('create branch');

                await CreateComparisonDialog.assertComparisonBranchNeededError();
                await step('error displayed that a comparison branch is needed');
            });

        scenario('Creating comparison with comparison branch')
            .description('A comparison with a comparison branch can be created')
            .it(async () => {

                await ComparisonsPage.goToPage();
                await step('comparisons page');
                await ComparisonsPage.assertNumberOfComparisons(NUMBER_OF_COMPARISONS_IN_TEST);

                await CreateComparisonDialog.clickCreateComparisonLink();
                await step('create comparison dialog opened');

                await CreateComparisonDialog.enterComparisonName('Comparison');
                await step('comparison name entered');

                await CreateComparisonDialog.openComparisonBranchSelectionDropdown();
                await step('comparison branch dropdown opened');

                await CreateComparisonDialog.chooseComparisonBranch('Production');
                await step('production branch selected');

                await CreateComparisonDialog.openComparisonBuildSelectionDropdown();
                await step('comparison build dropdown opened');

                await CreateComparisonDialog.chooseComparisonBuild('2014-02-21');
                await step('build selected');

                await CreateComparisonDialog.createBranch();
                await step('branch created');

                await ComparisonsPage.clickRefreshLink();
                await step('comparisons refreshed');

                await ComparisonsPage.assertNumberOfComparisons(NUMBER_OF_COMPARISONS_IN_TEST + 1);
                await step('comparisons asserted');
            });
    });
