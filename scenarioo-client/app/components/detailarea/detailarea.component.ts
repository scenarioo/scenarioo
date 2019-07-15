import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'sc-detailarea',
    template: require('./detailarea.component.html'),
    styles: [require('./detailarea.component.css').toString()],
})

export class DetailareaComponent {

    isPanelCollapsed: boolean = false;

    @Input()
    branchInformationTree: {};

    @Input()
    buildInformationTree: {};

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

}
