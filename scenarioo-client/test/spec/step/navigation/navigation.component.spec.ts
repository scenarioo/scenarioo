import {of, ReplaySubject, throwError as observableThrowError} from 'rxjs';
import {IConfiguration} from '../../../../app/generated-types/backend-types';

/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

declare var angular: angular.IAngularStatic;

describe('NavigationController', () => {

    let ctrl, $routeParams, $location, SelectedBranchAndBuildService, $componentController, TestData;

    const SelectedBranchAndBuildServiceMock = {
        BRANCH_KEY: 'branch',
        BUILD_KEY: 'build',
        callback: undefined,
        selectedStep: undefined,
        selected: () => {
            return {
                branch: SelectedBranchAndBuildServiceMock.selectedStep['branchName'],
                build: SelectedBranchAndBuildServiceMock.selectedStep['buildName'],
            };
        },
        callOnSelectionChange: (callback) => {
            SelectedBranchAndBuildServiceMock.callback = callback
        },
        update: (newStep) => {
            SelectedBranchAndBuildServiceMock.selectedStep = newStep;
            SelectedBranchAndBuildServiceMock.callback(newStep);
        },
        isDefined: () => {
            return angular.isDefined(SelectedBranchAndBuildServiceMock.selectedStep)
        }
    };

    beforeEach(angular.mock.module('scenarioo.controllers'));
    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        $provide.value('SelectedBranchAndBuildService', SelectedBranchAndBuildServiceMock);
    }));

    beforeEach(inject((_$rootScope_, _$routeParams_, _$location_,
                       _SelectedBranchAndBuildService_, 
                       _$componentController_, _TestData_
                       ) => {
        $routeParams = _$routeParams_;
        $location = _$location_;
        SelectedBranchAndBuildService = _SelectedBranchAndBuildService_;
        $componentController = _$componentController_;
        TestData = _TestData_;

        $routeParams.useCaseName = 'uc';
        $routeParams.scenarioName = 'sc';
        $routeParams.pageName = 'pn';
        $routeParams.pageOccurrence = 0;
        $routeParams.stepInPageOccurrence = 1;
    }));

    describe('scenario is found', () => {

        beforeEach(() => {
            const bindings = {
                stepNavigation: TestData.STEP.stepNavigation,
                stepStatistics: TestData.STEP.stepStatistics,
                step: TestData.STEP.step,
            };
            ctrl = $componentController('scStepNavigationToolbar', null, bindings);
            ctrl.bindStepNavigation();
            $routeParams.stepInPageOccurrence = 1;
        });

        it('loads the step data', () => {
            expect(ctrl.step).toEqual(TestData.STEP.step);
        });

        it('loads the stepNavigation and the stepStatistics into scope', () => {
            expect(ctrl.stepNavigation).toEqual(TestData.STEP.stepNavigation);
            expect(ctrl.stepStatistics).toEqual(TestData.STEP.stepStatistics);

            expect(ctrl.getCurrentStepIndexForDisplay()).toBe(3);
            expect(ctrl.getCurrentPageIndexForDisplay()).toBe(2);
            expect(ctrl.getStepIndexInCurrentPageForDisplay()).toBe(1);
            expect(ctrl.getNumberOfStepsInCurrentPageForDisplay()).toBe(2);
        });

        it('isFirstStep()', () => {
            expect(ctrl.isFirstStep()).toBeFalsy();

            ctrl.stepNavigation.stepIndex = 0;
            expect(ctrl.isFirstStep()).toBeTruthy();
        });

        it('isLastStep()', () => {
            expect(ctrl.isLastStep()).toBeFalsy();

            ctrl.stepNavigation.stepIndex = ctrl.stepStatistics.totalNumberOfStepsInScenario - 1;
            expect(ctrl.isLastStep()).toBeTruthy();
        });

        it('isFirstPage()', () => {
            expect(ctrl.isFirstPage()).toBeFalsy();

            ctrl.stepNavigation.pageIndex = 0;
            expect(ctrl.isFirstPage()).toBeTruthy();
        });

        it('isLastPage()', () => {
            expect(ctrl.isLastPage()).toBeFalsy();

            ctrl.stepNavigation.pageIndex = ctrl.stepStatistics.totalNumberOfPagesInScenario - 1;
            expect(ctrl.isLastPage()).toBeTruthy();
        });


        it('goToPreviousStep()', () => {
            ctrl.goToPreviousStep();

            expect($location.path()).toBe('/step/uc/sc/startSearch.jsp/0/1');
        });

        it('goToNextStep()', () => {
            ctrl.goToNextStep();

            expect($location.path()).toBe('/step/uc/sc/searchResults.jsp/0/1');
        });

        it('goToPreviousPage()', () => {
            ctrl.goToPreviousPage();

            expect($location.path()).toBe('/step/uc/sc/startSearch.jsp/0/1');
        });

        it('goToNextPage()', () => {
            ctrl.goToNextPage();

            expect($location.path()).toBe('/step/uc/sc/contentPage.jsp/0/0');
        });

        it('goToPreviousVariant()', () => {
            ctrl.goToPreviousVariant();

            expect($location.path()).toBe('/step/Find Page/find_page_no_result/searchResults.jsp/0/0');
        });

        it('goToNextVariant()', () => {
            ctrl.goToNextVariant();

            expect($location.path()).toBe('/step/Find Page/find_page_with_text_on_page_from_multiple_results/searchResults.jsp/0/1');
        });
    });

});

