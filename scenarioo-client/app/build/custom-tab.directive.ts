import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';

@Directive({
    selector: 'sc-custom-tab',
})
export class CustomTabDirective extends UpgradeComponent {

    @Input() tabTitle;
    @Input() tabColumns;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scCustomTab', elementRef, injector);
    }
}
