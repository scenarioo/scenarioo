import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-builds-list',
})
export class BuildsListDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scBuildsList', elementRef, injector);
    }
}
