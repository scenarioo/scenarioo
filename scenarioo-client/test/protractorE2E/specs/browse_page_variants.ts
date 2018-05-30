'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import * as Utils from '../util/util';
import StepPage from '../webPages/stepPage';
import ObjectDetailsPage from '../webPages/objectDetailsPage';

useCase('Browse page variants')
    .description('On the step page the user can flip through all existing variants of the page.')
    .describe(() => {

        beforeEach(async () => {
            await Utils.startScenariooRevisited();
        });

        scenario('Browse variants')
            .description('Navigate to previous / next page variants.')
            .it(async () => {
                await Utils.navigateToRoute('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');
                await StepPage.assertPageVariantIndicatorValue('Page-Variant 8 of 10');
                step('A step of the contentPage.jsp page.');

                await StepPage.goToPreviousPageVariant();
                await StepPage.assertPageVariantIndicatorValue('Page-Variant 7 of 10');
                step('A different page variant.');

                await StepPage.goToNextPageVariant();
                await StepPage.goToNextPageVariant();
                await StepPage.goToNextPageVariant();
                await StepPage.assertPageVariantIndicatorValue('Page-Variant 10 of 10');
                await StepPage.assertNextPageVariantButtonIsDisabled();
                step('The last page variant. Button for next page variant is disabled.');
            });

        scenario('Variant Overview')
            .description('Go to page variants overview page to select a different page variant.')
            .it(async () => {
                await Utils.navigateToRoute('/step/Switch%20Language/search_article_in_german_and_switch_to_spanish/contentPage.jsp/0/0');
                step('A step of the contentPage.jsp page.');

                await StepPage.clickAllPageVariantsLink();
                await Utils.assertRoute('/object/page/contentPage.jsp');
                step('Overview of page variants in all use cases.');

                await ObjectDetailsPage.clickToExpand(3);
                step('Expanded scenario with the steps that contain the page.');

                await ObjectDetailsPage.clickNthTreeTableRow(6);
                await Utils.assertRoute('/step/Find%20Page/find_page_title_ambiguous_directly/contentPage.jsp/0/2');
                step('A different page variant.');
            });

    });
