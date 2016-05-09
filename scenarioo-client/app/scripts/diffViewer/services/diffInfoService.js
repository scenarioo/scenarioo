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


/**
 *  Service functionality to prepare diff info data to display.
 */
angular.module('scenarioo.services').factory('DiffInfoService', function () {

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

    function enrichStepsWithDiffInfos(pagesAndSteps, removedSteps, diffInfos) {
        angular.forEach(pagesAndSteps, function(pageAndStep) {
            angular.forEach(pageAndStep.steps, function(step) {
               step.diffInfo = getDiffInfo(diffInfos, step.index);
            });
        });

        angular.forEach(removedSteps, function(removedStep) {
            addRemovedStep(pagesAndSteps, removedStep);
        });
    }

    function addRemovedStep(pagesAndSteps, stepInfo) {
        var targetPageAndStep = null;
        angular.forEach(pagesAndSteps, function(pageAndStep) {
            if(stepInfo.stepLink.pageName === pageAndStep.page.name && stepInfo.stepLink.pageOccurrence === pageAndStep.page.pageOccurrence) {
                pageAndStep = pageAndStep;
            }
        });

        if(targetPageAndStep === null) {
            var removedPage = {
                name: stepInfo.stepLink.pageName,
                pageOccurrence: stepInfo.stepLink.pageOccurence
            };
            targetPageAndStep = {
                page: removedPage,
                steps: []
            };
            pagesAndSteps.push(targetPageAndStep);
        }

        stepInfo.stepDescription.title = stepInfo.stepDescription.title ? stepInfo.stepDescription.title : 'undefined';
        stepInfo.stepDescription.diffInfo = getRemovedDiffInfo();

        targetPageAndStep.steps.push(stepInfo.stepDescription);
    }

    function getDiffInfo(diffInfos, key) {
        var diffInfo = diffInfos[key];
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
        enrichStepsWithDiffInfos: enrichStepsWithDiffInfos
    };


});
