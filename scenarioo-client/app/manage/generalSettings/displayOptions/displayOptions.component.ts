import {Component, OnInit, Output, EventEmitter, SimpleChanges, Input} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {ApplicationStatus, Configuration} from "../../../shared/services/applicationStatus.service";


@Component({
    selector: 'display-options',
    template: require('./displayOptions.component.html'),
    styles: [require('./displayOptions.component.css').toString()],
})
export class DisplayOptionsComponent implements OnInit {
    @Input() configuration: Configuration;
    @Output() formReady = new EventEmitter<FormGroup>();
    displayOptionsForm: FormGroup;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit() {
        this.displayOptionsForm = this.fb.group({ });
        this.formReady.emit(this.displayOptionsForm);
    }

}
