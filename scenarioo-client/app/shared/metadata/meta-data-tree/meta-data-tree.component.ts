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
    detailAccordionName;

    @Input()
    informationTree;

    informationTreeValue;
    detailAccordionNameValue;

    ngOnInit(): void {
        if (this.isFirstOpen === false) {
            this.isAccordionCollapsed = true;
        }

        for(let key of this.informationTree) {
            this.informationTreeValue.push(this.informationTree[key])
        }

        for(let key of this.detailAccordionName) {
            this.detailAccordionNameValue.push(this.detailAccordionName[key])
        }

    }
}
