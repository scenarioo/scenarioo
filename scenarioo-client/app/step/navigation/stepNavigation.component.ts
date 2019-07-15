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
angular
    .module('scenarioo.directives')
    .component('scStepNavigationToolbar', {
        bindings: {
            hideToolbar: '<',
            pageName: '<',
            stepNavigation: '<',
            stepStatistics: '<',
            step: '<',
            linkingVariable: '=',
            showScreenAnnotations: '=',
        },
        template: require('./stepNavigation.html'),
        controller: StepNavigationController,
    });

function StepNavigationController($location, $routeParams, GlobalHotkeysService, SelectedBranchAndBuildService) {

    const useCaseName = $routeParams.useCaseName;
    const scenarioName = $routeParams.scenarioName;

    const ctrl = this;
    ctrl.bindStepNavigation = bindStepNavigation;

    activate();

    function activate() {
        SelectedBranchAndBuildService.callOnSelectionChange(bindStepNavigation);
    }

    ctrl.getPageNameUrlEncoded = () => encodeURIComponent(ctrl.pageName);

    ctrl.go = (step) => {
        $location.path('/step/' + (step.useCaseName || useCaseName) + '/' + (step.scenarioName || scenarioName) + '/' + step.pageName + '/' + step.pageOccurrence + '/' + step.stepInPageOccurrence);
    };

    function bindStepNavigation() {
        ctrl.isFirstStep = isFirstStep;
        ctrl.isFirstPage = isFirstPage;
        ctrl.isLastPage = isLastPage;
        ctrl.goToPreviousStep = goToPreviousStep;
        ctrl.goToNextStep = goToNextStep;
        ctrl.goToPreviousPage = goToPreviousPage;
        ctrl.goToNextPage = goToNextPage;
        ctrl.goToFirstStep = goToFirstStep;
        ctrl.goToLastStep = goToLastStep;
        ctrl.isFirstPageVariantStep = isFirstPageVariantStep;
        ctrl.goToPreviousVariant = goToPreviousVariant;
        ctrl.isLastPageVariantStep = isLastPageVariantStep;
        ctrl.goToNextVariant = goToNextVariant;
        ctrl.getCurrentStepIndexForDisplay = getCurrentStepIndexForDisplay;
        ctrl.getCurrentPageIndexForDisplay = getCurrentPageIndexForDisplay;
        ctrl.getStepIndexInCurrentPageForDisplay = getStepIndexInCurrentPageForDisplay;
        ctrl.getNumberOfStepsInCurrentPageForDisplay = getNumberOfStepsInCurrentPageForDisplay;
        ctrl.isLastStep = isLastStep;

        GlobalHotkeysService.registerPageHotkeyCode(37, goToPreviousStep); // left arrow
        GlobalHotkeysService.registerPageHotkeyCode(39, goToNextStep); // right arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+37', goToPreviousPage); // control + left arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+39', goToNextPage); // control + right arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+36', goToFirstStep); // control + HOME
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+35', goToLastStep); // control + END
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+38', goToPreviousVariant); // control + up arrow
        GlobalHotkeysService.registerPageHotkeyCode('ctrl+40', goToNextVariant); // control + down arrow

        function isFirstStep() {
            return ctrl.stepNavigation && ctrl.stepNavigation.stepIndex === 0;
        }

        function isLastStep() {
            return ctrl.stepNavigation && ctrl.stepNavigation.stepIndex === ctrl.stepStatistics.totalNumberOfStepsInScenario - 1;
        }

        function isFirstPage() {
            return ctrl.stepNavigation && ctrl.stepNavigation.pageIndex === 0;
        }

        function isLastPage() {
            return ctrl.stepNavigation && ctrl.stepNavigation.pageIndex === ctrl.stepStatistics.totalNumberOfPagesInScenario - 1;
        }

        function goToPreviousStep() {
            if (!ctrl.stepNavigation || !ctrl.stepNavigation.previousStep) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.previousStep);
        }

        function goToNextStep() {
            if (!ctrl.stepNavigation || !ctrl.stepNavigation.nextStep) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.nextStep);
        }

        function goToPreviousPage() {
            if (!ctrl.stepNavigation || !ctrl.stepNavigation.previousPage) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.previousPage);
        }

        function goToNextPage() {
            if (!ctrl.stepNavigation || !ctrl.stepNavigation.nextPage) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.nextPage);
        }

        function goToFirstStep() {
            if (!ctrl.stepNavigation || !ctrl.stepNavigation.firstStep) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.firstStep);
        }

        function goToLastStep() {
            if (!ctrl.stepNavigation || !ctrl.stepNavigation.lastStep) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.lastStep);
        }

        function isFirstPageVariantStep() {
            return angular.isUndefined(ctrl.stepNavigation) || ctrl.stepNavigation.previousStepVariant === null;
        }

        function goToPreviousVariant() {
            if (ctrl.isFirstPageVariantStep()) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.previousStepVariant);
        }

        function isLastPageVariantStep() {
            return angular.isUndefined(ctrl.stepNavigation) || ctrl.stepNavigation.nextStepVariant === null;
        }

        function goToNextVariant() {
            if (ctrl.isLastPageVariantStep()) {
                return;
            }
            ctrl.go(ctrl.stepNavigation.nextStepVariant);
        }

        function getCurrentStepIndexForDisplay() {
            if (angular.isUndefined(ctrl.stepNavigation)) {
                return '?';
            }
            return ctrl.stepNavigation.stepIndex + 1;
        }

        function getCurrentPageIndexForDisplay() {
            if (angular.isUndefined(ctrl.stepNavigation)) {
                return '?';
            }
            return ctrl.stepNavigation.pageIndex + 1;
        }

        function getStepIndexInCurrentPageForDisplay() {
            if (angular.isUndefined(ctrl.stepNavigation)) {
                return '?';
            }
            return ctrl.stepNavigation.stepInPageOccurrence + 1;
        }

        function getNumberOfStepsInCurrentPageForDisplay() {
            if (angular.isUndefined(ctrl.stepStatistics)) {
                return '?';
            }
            return ctrl.stepStatistics.totalNumberOfStepsInPageOccurrence;
        }
    }
}
