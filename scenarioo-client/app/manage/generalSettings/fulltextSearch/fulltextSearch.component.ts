import {Component, OnInit, Output, EventEmitter, SimpleChanges, Input, AfterContentInit, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Configuration} from "../../../shared/services/applicationStatus.service";


@Component({
    selector: 'fulltext-search',
    template: require('./fulltextSearch.component.html'),
    styles: [require('./fulltextSearch.component.css').toString()],
})
export class FulltextSearchComponent implements OnChanges, OnInit {
    @Input() configuration: Configuration;
    @Output() formReady = new EventEmitter<FormGroup>();
    fulltextSearchForm: FormGroup;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit() {
        this.initForm();
        this.formReady.emit(this.fulltextSearchForm);
    }

    ngOnChanges(changes: SimpleChanges) {
        this.initForm();
        if (changes.configuration) {
            this.fulltextSearchForm.patchValue({});
        }
    }

    initForm(): void {
        if (!this.fulltextSearchForm) {
            this.fulltextSearchForm = this.fb.group({
            });
        }
    }

}
