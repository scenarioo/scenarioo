import {Directive, ElementRef, Injector, Input} from '@angular/core';
import {UpgradeComponent} from '@angular/upgrade/static';

@Directive({
    selector: 'sc-filterable-table-tree',
})
export class FilterableTableTreeDirective extends UpgradeComponent {

    @Input()
    treeData: any;

    @Input()
    rootIsCollapsed: boolean;

    @Input()
    filter: string;

    @Input()
    columns: any;

    @Input()
    treemodel: any; // TODO 2-way binding

    @Input()
    clickAction: () => void;

    @Input()
    firstColumnTitle: string;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scFilterableTableTree', elementRef, injector);
    }
}
