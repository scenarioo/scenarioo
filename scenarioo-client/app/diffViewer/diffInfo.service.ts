import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class DiffInfoService {

    getElementsWithDiffInfos(elements, removedElements, diffInfos, pathToName) {
        const elementsWithDiffInfo = [];

        angular.forEach(elements, (element) => {
            element.diffInfo = this.getDiffInfo(diffInfos, this.resolvePathValue(element, pathToName));
            elementsWithDiffInfo.push(element);
        });

        angular.forEach(removedElements, (removedElement) => {
            removedElement.diffInfo = this.getRemovedDiffInfo();
            elementsWithDiffInfo.push(removedElement);
        });

        return elementsWithDiffInfo;
    }

     enrichPagesAndStepsWithDiffInfos(pagesAndSteps, removedSteps, diffInfos) {
        let stepIndex = 0;
        angular.forEach(pagesAndSteps, (pageAndStep) => {
            angular.forEach(pageAndStep.steps, (step) => {
                step.diffInfo = this.getDiffInfo(diffInfos, stepIndex++);
                step.diffInfo.changed = 1;
                step.diffInfo.added = 0;
                step.diffInfo.removed = 0;
            });
        });

        angular.forEach(removedSteps, (removedStep) => {
            this.addRemovedStep(pagesAndSteps, removedStep);
        });

        angular.forEach(pagesAndSteps, (pageAndStep) => {
            pageAndStep.page.diffInfo = this.getPageDiffInfo(pageAndStep);
        });
    }

     enrichChangedStepWithDiffInfo(step, diffInfo) {
        if (diffInfo) {
            diffInfo.changed = 1;
            diffInfo.added = 0;
            diffInfo.removed = 0;
        }
        step.diffInfo = this.enrichDiffInfo(diffInfo);
    }

     addRemovedStep(pagesAndSteps, stepInfo) {
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
            const insertIndex = this.getInsertPosition(pagesAndSteps, stepInfo.stepDescription.index);
            pagesAndSteps.splice(insertIndex, 0, targetPageAndStep);
        }

        stepInfo.stepDescription.index = stepInfo.stepLink.stepInPageOccurrence;
        stepInfo.stepDescription.diffInfo = this.getRemovedDiffInfo();

        targetPageAndStep.steps.push(stepInfo.stepDescription);
    }

     getInsertPosition(pagesAndSteps, stepIndex) {
        for (let i = 0; i < pagesAndSteps; i++) {
            const steps = pagesAndSteps[i].steps;
            const lastStepInPage = steps[steps.length - 1];
            if (lastStepInPage >= stepIndex) {
                return i + 1;
            }
        }
        return pagesAndSteps.length;
    }

     getDiffInfo(diffInfos, key) {
        return this.enrichDiffInfo(diffInfos[key]);
    }

     enrichDiffInfo(diffInfo) {
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

     getRemovedDiffInfo() {
        const diffInfo: any = {};
        diffInfo.changeRate = 100;
        diffInfo.isAdded = false;
        diffInfo.isRemoved = true;
        return diffInfo;
    }

     getPageDiffInfo(pageAndStep) {
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

     resolvePathValue(obj, pathConcatenated) {
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
}

angular.module('scenarioo.services')
    .factory('DiffInfoService', downgradeInjectable(DiffInfoService));
