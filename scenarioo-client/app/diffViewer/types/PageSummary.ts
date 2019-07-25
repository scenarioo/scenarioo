import {IPageSummary} from '../../generated-types/backend-types';

export class PageSummary {
    diffInfo: { isRemoved: boolean; removed: number; added: number; isAdded: boolean; changeRate: number; changed: number };
    name: string;
    pageOccurrence: number;

    constructor(source: IPageSummary) {
        this.name = source.name;
        this.pageOccurrence = source.pageOccurrence;
    }

}
