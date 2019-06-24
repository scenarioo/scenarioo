import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-general-settings',
})
export class GeneralSettingsDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scGeneralSettings', elementRef, injector);
    }
}
