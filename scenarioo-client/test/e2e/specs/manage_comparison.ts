'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util';
import ComparisonDetailDialog from '../pages/comparisonDetailLogDialog';
import ComparisonsPage from '../pages/comparisonsPage';

useCase('Manage comparisons')
    .description('View and manage comparisons')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Filter comparisons')
            .description('Comparisons can be filtered')
            .it(async () => {
                await ComparisonsPage.goToPage();
                const comparisonsCount = ComparisonsPage.getNumberOfComparisons();
                await step('comparisons page');

                await ComparisonsPage.filterComparisons('To Projectstart');
                await ComparisonsPage.assertNumberOfComparisons(5);
                await step('filtered Comparisons for text \'To Projectstart\'');

                await ComparisonsPage.clickResetButton();
                await ComparisonsPage.assertNumberOfComparisons(await comparisonsCount);
                await step('reset comparisons page');
            });

        scenario('Recalculate comparison')
            .description('Comparisons can be recalculated')
            .it(async () => {
                await ComparisonsPage.goToPage();
                await ComparisonsPage.assertComparisonStatus(0, 'SUCCESS');
                await step('comparison page with successful comparison');

                await ComparisonsPage.recalculateComparison(0);
                await ComparisonsPage.assertComparisonStatus(0, 'PROCESSING');
                await step('comparison in status PROCESSING');
            });

        scenario('Comparison details and log')
            .description('Comparisons details and log can be viewed')
            .fit(async () => {
                await ComparisonsPage.goToPage();
                await ComparisonsPage.openComparisonDetails(1);
                await step('Comparison Details and Log opened');

                await ComparisonDetailDialog.assertTitle('Comparison To most recent (dev) on Build wikipedia-docu-example-dev/2014-05-19');
                await ComparisonDetailDialog.assertBuildDetails('wikipedia-docu-example-dev', '2014-05-19');
                await ComparisonDetailDialog.assertComparedBuildDetails('wikipedia-docu-example', '2014-04-19');

                await ComparisonDetailDialog.assertLogContains('=== START OF BUILD COMPARISON ===');
                await ComparisonDetailDialog.assertLogContains('Comparison To most recent (dev) on target build wikipedia-docu-example-dev/2014-05-19 for comparing with build Development/most recent');
                await ComparisonDetailDialog.assertLogContains('Comparison finished in');
                await ComparisonDetailDialog.assertLogContains('=== END OF BUILD COMPARISON (success) ===');
            });
    });
