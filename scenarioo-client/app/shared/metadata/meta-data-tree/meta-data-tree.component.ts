import {Component, Input} from '@angular/core';

@Component({
    selector: 'sc-meta-data-tree',
    template: require('./meta-data-tree.component.html'),
    styles: [require('./meta-data-tree.component.css').toString()],
})

export class MetaDataTreeComponent {

    isAccordionCollapsed: boolean = false;

    @Input()
    isFirstOpen: boolean;

    @Input()
    informationTree;

    ngOnInit(): void {
        if (this.isFirstOpen === false) {
            this.isAccordionCollapsed = true;
        }
    }
}
