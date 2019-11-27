import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';

@Directive({
    selector: 'sc-screen-annotations-button',
})
export class ScreenAnnotationsButtonDirective extends UpgradeComponent {

    @Input() screenAnnotations;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scScreenAnnotationsButton', elementRef, injector);
    }
}
