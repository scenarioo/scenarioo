import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';

@Directive({
    selector: 'sc-metadata-tree',
})
export class MetaDataTreeDirective extends UpgradeComponent {

    @Input() metadataTree;

    isAccordionCollapsed: boolean = false;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scMetadataTree', elementRef, injector);
    }

}

