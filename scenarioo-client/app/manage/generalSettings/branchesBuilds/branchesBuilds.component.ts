import {Component, OnInit, Output, EventEmitter, SimpleChanges, Input, AfterContentInit, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Configuration} from "../../../shared/services/applicationStatus.service";
import {BranchBuild} from "../../../shared/services/branchesResource.service";


@Component({
    selector: 'branches-builds',
    template: require('./branchesBuilds.component.html'),
    styles: [require('./branchesBuilds.component.css').toString()],
})
export class BranchesBuildsComponent implements OnChanges, OnInit {
    @Input() configuration: Configuration;
    @Input() branchBuilds: BranchBuild;
    @Input() configuredBranch: BranchBuild;
    @Output() formReady = new EventEmitter<FormGroup>();
    branchesBuildsForm: FormGroup;

    constructor(private fb: FormBuilder) {
    }

    ngOnInit() {
        this.initForm();
        this.formReady.emit(this.branchesBuildsForm);
    }

    ngOnChanges(changes: SimpleChanges) {
        this.initForm();
        if (changes.configuration) {
            this.branchesBuildsForm.patchValue({
                defaultBranchName: this.configuration.defaultBranchName,
                defaultBuildName: this.configuration.defaultBuildName,
                aliasForMostRecentBuild: "TODO", // TODO: where does this value come from?!
                aliasForLastSuccessfulBuild: "TODO", // TODO: where does this value come from?!
                createLastSuccessfulScenarioBuild: false // TODO: where does this value come from?!
            });
        }
    }

    initForm(): void {
        if (!this.branchesBuildsForm) {
            this.branchesBuildsForm = this.fb.group({
                defaultBranchName: null,
                defaultBuildName: null,
                aliasForMostRecentBuild: null,
                aliasForLastSuccessfulBuild: null,
                createLastSuccessfulScenarioBuild: null
            });
        }
    }

}
