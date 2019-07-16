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
    tree: boolean;

    @Input()
    sketches: boolean;

    @Input()
    label: boolean;

    @Input()
    metadataTree: boolean;

    @Input()
    detailAccordionName: {};

    @Input()
    informationTree: any;

    @Input()
    labelConfigurations: {};

    constructor() {
    }

    ngOnInit(): void {
        if (this.isFirstOpen === false) {
            this.isAccordionCollapsed = true;
        }
    }

    goToIssue(issue) {

    }
}
