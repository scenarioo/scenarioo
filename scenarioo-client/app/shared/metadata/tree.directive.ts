import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';

@Directive({
    selector: 'sc-tree',
})
export class TreeDirective extends UpgradeComponent {

    @Input() informationTree;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scTree', elementRef, injector);
    }
}
