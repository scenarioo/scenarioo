import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Configuration} from '../../../shared/services/applicationStatus.service';

@Component({
    selector: 'status-styling',
    template: require('./statusStyling.component.html'),
    styles: [require('./statusStyling.component.css').toString()],
})
export class StatusStylingComponent implements OnInit {

    @Input() configuration: Configuration;

    @Output() formReady = new EventEmitter<FormGroup>();

    statusStylingForm: FormGroup;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit() {
        this.statusStylingForm = this.fb.group({ });
        this.formReady.emit(this.statusStylingForm);
    }

    objectKeys(obj) {
        return Object.keys(obj);
    }

}
