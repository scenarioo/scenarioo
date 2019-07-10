import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-custom-tab',
})
export class CustomTabDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scCustomTab', elementRef, injector);
    }
}
