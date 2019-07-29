import {IPageSummary} from '../../generated-types/backend-types';
import {PageDiffInfo} from './PageDiffInfo';

export class PageSummary {
    diffInfo: PageDiffInfo;
    name: string;
    pageOccurrence: number;

    constructor(source: IPageSummary) {
        this.name = source.name;
        this.pageOccurrence = source.pageOccurrence;
    }

}
