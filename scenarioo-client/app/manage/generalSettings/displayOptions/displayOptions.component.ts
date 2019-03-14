import {Component, OnInit, Output, EventEmitter, SimpleChanges, Input, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Configuration} from "../../../shared/services/applicationStatus.service";


@Component({
    selector: 'display-options',
    template: require('./displayOptions.component.html'),
    styles: [require('./displayOptions.component.css').toString()],
})
export class DisplayOptionsComponent implements OnInit, OnChanges {
    @Input() configuration: Configuration;
    @Output() formReady = new EventEmitter<FormGroup>();
    displayOptionsForm: FormGroup;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit() {
        this.initForm();
        this.formReady.emit(this.displayOptionsForm);
    }

    ngOnChanges(changes: SimpleChanges) {
        this.initForm();
        if(changes.configuration) {
            this.displayOptionsForm.patchValue({
                // TODO: where does the config value come from?!
                // expandPagesInScenarioOverview: this.configuration.???
                expandPagesInScenarioOverview: false
            });
        }
    }

    initForm(): void {
        if (!this.displayOptionsForm) {
            this.displayOptionsForm = this.fb.group({
                expandPagesInScenarioOverview: null
            });
        }
    }
}
