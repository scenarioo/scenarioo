import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';

@Directive({
    selector: 'sc-custom-tab',
})
export class CustomTabDirective extends UpgradeComponent {

    @Input() tabId: string;
    @Input() tabColumns: string;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scCustomTab', elementRef, injector);
    }
}
