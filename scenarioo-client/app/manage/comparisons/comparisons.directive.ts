import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-comparisons',
})
export class ComparisonsDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scComparisons', elementRef, injector);
    }
}
