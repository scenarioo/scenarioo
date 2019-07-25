import {IPageWithSteps} from '../../generated-types/backend-types';
import {StepDescription} from './StepDescription';
import {PageSummary} from './PageSummary';

export class PageWithSteps {
    page: PageSummary;
    steps: StepDescription[];

    constructor(source: IPageWithSteps) {
        this.page = new PageSummary(source.page);
        this.steps = source.steps.map(value => new StepDescription(value));
    }
}
