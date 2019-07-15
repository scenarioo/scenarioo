import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-sketches-tab',
})
export class SketchesTabDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scSketchesTab', elementRef, injector);
    }
}
