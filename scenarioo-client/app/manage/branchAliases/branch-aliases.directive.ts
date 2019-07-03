import {UpgradeComponent} from '@angular/upgrade/static';
import {Directive, ElementRef, Injector} from '@angular/core';

@Directive({
    selector: 'sc-branch-aliases',
})
export class BranchAliasesDirective extends UpgradeComponent {
    constructor(elementRef: ElementRef, injector: Injector) {
        super('scBranchAliases', elementRef, injector);
    }
}
