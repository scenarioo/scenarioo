import {Component, OnInit, Output, EventEmitter, SimpleChanges, Input, AfterContentInit, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Configuration} from "../../../shared/services/applicationStatus.service";


@Component({
    selector: 'additional-columns',
    template: require('./additionalColumns.component.html'),
    styles: [require('./additionalColumns.component.css').toString()],
})
export class AdditionalColumnsComponent implements OnChanges, OnInit {
    @Input() configuration: Configuration;
    @Output() formReady = new EventEmitter<FormGroup>();
    additionalColumnsForm: FormGroup;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit() {
        this.additionalColumnsForm = this.fb.group({
            scenarioPropertiesInOverview: null
        });
        this.formReady.emit(this.additionalColumnsForm);
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.configuration && !changes.configuration.isFirstChange()) {
            this.additionalColumnsForm.patchValue({scenarioPropertiesInOverview: this.configuration.scenarioPropertiesInOverview});
        }
    }

}
