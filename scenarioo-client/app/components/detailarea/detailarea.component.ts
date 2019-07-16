import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'sc-detailarea',
    template: require('./detailarea.component.html'),
    styles: [require('./detailarea.component.css').toString()],
})

export class DetailareaComponent {

    isPanelCollapsed: boolean = false;

    /* Inputs of usecases-overview */
    @Input()
    branchInformationTree: {};

    @Input()
    buildInformationTree: {};

    /*Inputs of scenarios-overview*/
    @Input()
    usecaseInformationTree: {};

    @Input()
    metadataInformationTree: {};

    @Input()
    relatedIssues: {};

    @Input()
    useCaseLabels: {};

    @Input()
    labelConfigurations: {};

    @Output('valueChange')
    panelCollapsed: EventEmitter<boolean> = new EventEmitter<boolean>();

    valueChange() {
        if (this.isPanelCollapsed === false) {
            this.isPanelCollapsed = true;
        } else {
            this.isPanelCollapsed = false;
        }
        this.panelCollapsed.emit(this.isPanelCollapsed);
    }

    isEmptyObject(obj) {
        return (obj && (Object.keys(obj).length === 0));
    }

}
