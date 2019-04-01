import {Component, Input, Output, EventEmitter} from "@angular/core";

@Component({
    selector: 'modal-view',
    template: require('./modalView.component.html'),
    styles: [require('./modalView.component.css').toString()],
})

export class ModalViewComponent{

    @Input() title;
    @Input() actions: any[];

    @Output() modalClosed = new EventEmitter<void>();

    onActionClick(action) {
        switch(action) {
            case 'close':
                this.modalClosed.emit();
                break;
            default:
                throw new Error('Action is not defined');
        }
    }
}
