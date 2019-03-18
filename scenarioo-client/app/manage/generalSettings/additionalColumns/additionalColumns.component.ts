import {Component, OnInit, Output, EventEmitter, SimpleChanges, Input, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Configuration} from '../../../shared/services/applicationStatus.service';

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
        this.initForm();
        this.listenToFormChanges();
        this.formReady.emit(this.additionalColumnsForm);
    }

    ngOnChanges(changes: SimpleChanges) {
        this.initForm();
        if (changes.configuration) {
            this.additionalColumnsForm.patchValue({scenarioPropertiesInOverview: this.configuration.scenarioPropertiesInOverview});
        }
    }

    listenToFormChanges() {
        const valueChanges$ = this.additionalColumnsForm.valueChanges;
        valueChanges$.subscribe( (val) => {
            this.configuration.scenarioPropertiesInOverview = val.scenarioPropertiesInOverview;
        });
    }

    initForm(): void {
        if (!this.additionalColumnsForm) {
            this.additionalColumnsForm = this.fb.group({
                scenarioPropertiesInOverview: null,
            });
        }
    }

}
