import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-label-colors',
})
export class LabelColorsDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scLabelColors', elementRef, injector);
    }
}
