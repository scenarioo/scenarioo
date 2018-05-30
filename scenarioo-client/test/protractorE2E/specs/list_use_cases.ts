'use strict';

import { scenario, step, useCase } from "scenarioo-js";
import * as Utils from "../util/util";
import NavigationPage from "../webPages/navigationPage";
import HomePage from "../webPages/homePage";

const NUMBER_OF_USE_CASES = 4;
const COMPARISON_PROJECTSTART = 'To Projectstart';
const USE_CASE_WITH_HIGHEST_DIFF = 'Donate';

useCase('List use cases')
    .description('As soon as a branch and a build are selected, a list of use cases is shown.')
    .describe(function () {

        beforeEach(async function () {
            await Utils.startScenariooRevisited();
        });

        scenario('Display and filter usecases')
            .it(async function () {
                await Utils.navigateToRoute("/");
                await HomePage.assertUseCasesShown(NUMBER_OF_USE_CASES);
                step('display usecases on homepage');
                await HomePage.assertPageIsDisplayed();
                await HomePage.filterUseCases('notinlist');
                await HomePage.assertUseCasesShown(0);
                step('filter applied: no use cases shown');
                await HomePage.filterUseCases('find page');
                await HomePage.assertUseCasesShown(1);
                step('filter applied: one use case found');
                await HomePage.filterUseCases('user wants find page');
                await HomePage.assertUseCasesShown(1);
                step('other filter applied: one use case found');
            });

        scenario('Show and hide metadata')
            .it(async function () {
                await Utils.navigateToRoute("/");
                step('display the homePage, metadata shown');
                await HomePage.assertPageIsDisplayed();
                await HomePage.assertMetaDataShown();
                await HomePage.hideMetaData();
                await HomePage.assertMetaDataHidden();
                step('metadata hidden');
                await HomePage.showMetaData();
                await HomePage.assertMetaDataShown();
                step('metadata shown');
            });

        scenario('Display Diff-Information')
            .labels(['diff-viewer'])
            .it(async function () {
                await Utils.navigateToRoute("/");
                step('display usecases on homepage');
                await NavigationPage.chooseComparison(COMPARISON_PROJECTSTART);
                await HomePage.assertPageIsDisplayed();
                step('To Projectstart comparison selected');

                await HomePage.assertNumberOfDiffInfos(NUMBER_OF_USE_CASES);

                await HomePage.sortByChanges();
                await HomePage.assertLastUseCase(USE_CASE_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted ascending');

                await HomePage.sortByChanges();
                await HomePage.assertFirstUseCase(USE_CASE_WITH_HIGHEST_DIFF);
                step('Diff Infos sorted descending');
                return NavigationPage.disableComparison();
            });
    });
