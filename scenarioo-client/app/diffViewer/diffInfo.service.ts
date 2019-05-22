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

angular.module('scenarioo.services').service('DiffInfoService', DiffInfoService);

function DiffInfoService() {

    function getElementsWithDiffInfos(elements, removedElements, diffInfos, pathToName) {
        const elementsWithDiffInfo = [];

        angular.forEach(elements, (element) => {
            element.diffInfo = getDiffInfo(diffInfos, resolvePathValue(element, pathToName));
            elementsWithDiffInfo.push(element);
        });

        angular.forEach(removedElements, (removedElement) => {
            removedElement.diffInfo = getRemovedDiffInfo();
            elementsWithDiffInfo.push(removedElement);
        });

        return elementsWithDiffInfo;
    }

    function enrichPagesAndStepsWithDiffInfos(pagesAndSteps, removedSteps, diffInfos) {
        let stepIndex = 0;
        angular.forEach(pagesAndSteps, (pageAndStep) => {
            angular.forEach(pageAndStep.steps, (step) => {
                step.diffInfo = getDiffInfo(diffInfos, stepIndex++);
                step.diffInfo.changed = 1;
                step.diffInfo.added = 0;
                step.diffInfo.removed = 0;
            });
        });

        angular.forEach(removedSteps, (removedStep) => {
            addRemovedStep(pagesAndSteps, removedStep);
        });

        angular.forEach(pagesAndSteps, (pageAndStep) => {
            pageAndStep.page.diffInfo = getPageDiffInfo(pageAndStep);
        });
    }

    function enrichChangedStepWithDiffInfo(step, diffInfo) {
        if (diffInfo) {
            diffInfo.changed = 1;
            diffInfo.added = 0;
            diffInfo.removed = 0;
        }
        step.diffInfo = enrichDiffInfo(diffInfo);
    }

    function addRemovedStep(pagesAndSteps, stepInfo) {
        let targetPageAndStep = null;
        angular.forEach(pagesAndSteps, (pageAndStep) => {
            if (stepInfo.stepLink.pageName === pageAndStep.page.name && stepInfo.stepLink.pageOccurrence === pageAndStep.page.pageOccurrence) {
                targetPageAndStep = pageAndStep;
            }
        });

        if (targetPageAndStep === null) {
            const removedPage = {
                name: stepInfo.stepLink.pageName,
                pageOccurrence: stepInfo.stepLink.pageOccurrence,
            };
            targetPageAndStep = {
                page: removedPage,
                steps: [],
            };
            const insertIndex = getInsertPosition(pagesAndSteps, stepInfo.stepDescription.index);
            pagesAndSteps.splice(insertIndex, 0, targetPageAndStep);
        }

        stepInfo.stepDescription.index = stepInfo.stepLink.stepInPageOccurrence;
        stepInfo.stepDescription.diffInfo = getRemovedDiffInfo();

        targetPageAndStep.steps.push(stepInfo.stepDescription);
    }

    function getInsertPosition(pagesAndSteps, stepIndex) {
        for (let i = 0; i < pagesAndSteps; i++) {
            const steps = pagesAndSteps[i].steps;
            const lastStepInPage = steps[steps.length - 1];
            if (lastStepInPage >= stepIndex) {
                return i + 1;
            }
        }
        return pagesAndSteps.length;
    }

    function getDiffInfo(diffInfos, key) {
        return enrichDiffInfo(diffInfos[key]);
    }

    function enrichDiffInfo(diffInfo) {
        if (diffInfo) {
            diffInfo.isAdded = false;
            diffInfo.isRemoved = false;
        } else {
            diffInfo = {};
            diffInfo.changeRate = 100;
            diffInfo.isAdded = true;
            diffInfo.isRemoved = false;
        }
        return diffInfo;
    }

    function getRemovedDiffInfo() {
        const diffInfo: any = {};
        diffInfo.changeRate = 100;
        diffInfo.isAdded = false;
        diffInfo.isRemoved = true;
        return diffInfo;
    }

    function getPageDiffInfo(pageAndStep) {
        const diffInfo = {
            changeRate: 0,
            added: 0,
            changed: 0,
            removed: 0,
            isAdded: false,
            isRemoved: false,
        };
        let stepChangeRateSum = 0;
        angular.forEach(pageAndStep.steps, (step) => {
            stepChangeRateSum += step.diffInfo.changeRate;
            if (step.diffInfo.isAdded) {
                diffInfo.added++;
            } else if (step.diffInfo.isRemoved) {
                diffInfo.removed++;
            } else if (step.diffInfo.changeRate > 0) {
                diffInfo.changed++;
            }
        });
        if (diffInfo.added === pageAndStep.steps.length) {
            diffInfo.isAdded = true;
        }
        if (diffInfo.removed === pageAndStep.steps.length) {
            diffInfo.isRemoved = true;
        }
        diffInfo.changeRate = stepChangeRateSum / pageAndStep.steps.length;
        return diffInfo;
    }

    function resolvePathValue(obj, pathConcatenated) {
        let current = obj;
        if (pathConcatenated) {
            const paths = pathConcatenated.split('.');
            for (const path of paths) {
                if (current[path] === undefined) {
                    return undefined;
                } else {
                    current = current[path];
                }
            }
        }
        return current;
    }

    return {
        getElementsWithDiffInfos,
        enrichChangedStepWithDiffInfo,
        enrichPagesAndStepsWithDiffInfos,
    };
}
