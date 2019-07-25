import {ILabels, IStepDescription} from '../../generated-types/backend-types';
import {StepDiffInfo} from './StepDiffInfo';

export class StepDescription {
    details: { [index: string]: any };
    diffInfo: StepDiffInfo;
    index: number;
    labels: ILabels;
    screenshotFileName: string;
    status: string;
    title: string;

    constructor(source: IStepDescription) {
        this.details = source.details;
        this.index = source.index;
        this.labels = source.labels;
        this.screenshotFileName = source.screenshotFileName;
        this.status = source.status;
        this.title = source.title;
    }
}
