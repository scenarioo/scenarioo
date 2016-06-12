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
        var elementsWithDiffInfo = [];

        angular.forEach(elements, function(element){
            element.diffInfo = getDiffInfo(diffInfos, resolvePathValue(element, pathToName));
            elementsWithDiffInfo.push(element);
        });

        angular.forEach(removedElements, function(removedElement){
            removedElement.diffInfo = getRemovedDiffInfo();
            elementsWithDiffInfo.push(removedElement);
        });

        return elementsWithDiffInfo;
    }

    function enrichPagesAndStepsWithDiffInfos(pagesAndSteps, removedSteps, diffInfos) {
        var stepIndex = 0;
        angular.forEach(pagesAndSteps, function(pageAndStep) {
            angular.forEach(pageAndStep.steps, function(step) {
                step.diffInfo = getDiffInfo(diffInfos, stepIndex++);
                step.diffInfo.changed = 1;
                step.diffInfo.added = 0;
                step.diffInfo.removed = 0;
            });
        });

        angular.forEach(removedSteps, function(removedStep) {
            addRemovedStep(pagesAndSteps, removedStep);
        });

        angular.forEach(pagesAndSteps, function(pageAndStep) {
            pageAndStep.page.diffInfo = getPageDiffInfo(pageAndStep);
        });
    }

    function enrichChangedStepWithDiffInfo(step, diffInfo) {
                if(diffInfo){
                    diffInfo.changed = 1;
                    diffInfo.added = 0;
                    diffInfo.removed = 0;
                }
                step.diffInfo = enrichDiffInfo(diffInfo);
    }

    function addRemovedStep(pagesAndSteps, stepInfo) {
        var targetPageAndStep = null;
        angular.forEach(pagesAndSteps, function(pageAndStep) {
            if(stepInfo.stepLink.pageName === pageAndStep.page.name && stepInfo.stepLink.pageOccurrence === pageAndStep.page.pageOccurrence) {
                targetPageAndStep = pageAndStep;
            }
        });

        if(targetPageAndStep === null) {
            var removedPage = {
                name: stepInfo.stepLink.pageName,
                pageOccurrence: stepInfo.stepLink.pageOccurrence
            };
            targetPageAndStep = {
                page: removedPage,
                steps: []
            };
            var insertIndex = getInsertPosition(pagesAndSteps, stepInfo.stepDescription.index);
            pagesAndSteps.splice(insertIndex, 0, targetPageAndStep);
        }

        stepInfo.stepDescription.title = stepInfo.stepDescription.title;
        stepInfo.stepDescription.index = stepInfo.stepLink.stepInPageOccurrence;
        stepInfo.stepDescription.diffInfo = getRemovedDiffInfo();

        targetPageAndStep.steps.push(stepInfo.stepDescription);
    }

    function getInsertPosition(pagesAndSteps, stepIndex) {
        for(var i = 0; i < pagesAndSteps; i++) {
            var steps = pagesAndSteps[i].steps;
            var lastStepInPage = steps[steps.length - 1];
            if(lastStepInPage >= stepIndex) {
                return i + 1;
            }
        }
        return pagesAndSteps.length;
    }

    function getDiffInfo(diffInfos, key) {
        var diffInfo = diffInfos[key];
        return enrichDiffInfo(diffInfo);
    }

    function enrichDiffInfo(diffInfo){
        if(diffInfo) {
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
        var diffInfo = {};
        diffInfo.changeRate = 100;
        diffInfo.isAdded = false;
        diffInfo.isRemoved = true;
        return diffInfo;
    }

    function getPageDiffInfo(pageAndStep) {
        var diffInfo = {
            changeRate: 0,
            added: 0,
            changed: 0,
            removed: 0,
            isAdded: false,
            isRemoved: false
        };
        var stepChangeRateSum = 0;
        angular.forEach(pageAndStep.steps, function(step) {
            stepChangeRateSum += step.diffInfo.changeRate;
            if(step.diffInfo.isAdded){
                diffInfo.added++;
            } else if(step.diffInfo.isRemoved){
                diffInfo.removed++;
            } else if(step.diffInfo.changeRate > 0) {
                diffInfo.changed++;
            }
        });
        if(diffInfo.added === pageAndStep.steps.length) {
            diffInfo.isAdded = true;
        }
        if(diffInfo.removed === pageAndStep.steps.length) {
            diffInfo.isRemoved = true;
        }
        diffInfo.changeRate = stepChangeRateSum / pageAndStep.steps.length;
        return diffInfo;
    }

    function resolvePathValue(obj, path) {
        var current = obj;
        if(path) {
            var paths = path.split('.');
            for(var i = 0; i < paths.length; i++) {
                if(current[paths[i]] === undefined){
                    return undefined;
                }else {
                    current = current[paths[i]];
                }
            }
        }
        return current;
    }

    return {
        getElementsWithDiffInfos: getElementsWithDiffInfos,
        enrichChangedStepWithDiffInfo: enrichChangedStepWithDiffInfo,
        enrichPagesAndStepsWithDiffInfos: enrichPagesAndStepsWithDiffInfos
    };
}
