import {IStepDiffInfo} from '../../generated-types/backend-types';

export class StepDiffInfo {
    added: number;
    changeRate: number;
    changed: number;
    comparisonScreenshotName: string;
    index: number;
    pageName: string;
    pageOccurrence: number;
    removed: number;
    stepInPageOccurrence: number;

    constructor(source: IStepDiffInfo) {
        this.changeRate = source.changeRate;
        this.comparisonScreenshotName = source.comparisonScreenshotName;
        this.index = source.index;
        this.pageName = source.pageName;
        this.pageOccurrence = source.pageOccurrence;
        this.stepInPageOccurrence = source.stepInPageOccurrence;
    }

}
