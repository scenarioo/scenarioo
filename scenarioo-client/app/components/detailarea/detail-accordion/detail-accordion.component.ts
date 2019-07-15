import {Component, Input} from '@angular/core';

@Component({
    selector: 'sc-detail-accordion',
    template: require('./detail-accordion.component.html'),
    styles: [require('./detail-accordion.component.css').toString()],
})

export class DetailAccordionComponent {

    isAccordionCollapsed: boolean = false;

    @Input()
    isFirstOpen: boolean;

    @Input()
    detailAccordionName: {};

    @Input()
    informationTree: {};

    constructor() {
    }

    ngOnInit(): void {
        if (this.isFirstOpen === false) {
            this.isAccordionCollapsed = true;
        }
    }
}
