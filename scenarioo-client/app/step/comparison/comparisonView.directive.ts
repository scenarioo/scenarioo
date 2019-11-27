import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector, Input} from '@angular/core';

@Directive({
    selector: 'sc-comparison-view',
})
export class ComparisonViewDirective extends UpgradeComponent {

    @Input() step;
    @Input() stepIdentifier;
    @Input() stepIndex;
    @Input() screenShotUrl;

    constructor(elementRef: ElementRef, injector: Injector) {
        super('scComparisonView', elementRef, injector);
    }
}
