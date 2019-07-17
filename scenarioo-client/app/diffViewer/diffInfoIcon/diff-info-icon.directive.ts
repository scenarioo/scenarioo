import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';

@Directive({
    selector: 'sc-diff-info-icon',
})
export class DiffInfoIconDirective extends UpgradeComponent {

    @Input() childElementType: string;
    @Input() elementType: string;
    @Input() diffInfo;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scDiffInfoIcon', elementRef, injector);
    }
}
