import {Component, Input, OnInit} from '@angular/core';
import {FormGroup, FormBuilder} from '@angular/forms';
import {BranchBuild, BranchesResource} from '../../shared/services/branchesResource.service';
import {ApplicationStatus, ApplicationStatusService} from '../../shared/services/applicationStatus.service';
import {NewConfigService} from '../new-config.service';

@Component({
    selector: 'general-settings',
    template: require('./generalSettings.component.html'),
    styles: [require('./generalSettings.component.css').toString()],
})
export class GeneralSettingsComponent implements OnInit {

    settingsForm: FormGroup;

    applicationStatus: ApplicationStatus;
    branchBuilds: BranchBuild[];
    configuredBranch: BranchBuild;

    successfullyUpdatedConfiguration: boolean;

    constructor(
        private formBuilder: FormBuilder,
        private branchesResource: BranchesResource,
        private configService: NewConfigService,
        private appStatusService: ApplicationStatusService,
    ) { }

    ngOnInit() {
        this.branchesResource.query().subscribe((branches) => {
            this.branchBuilds = branches;
            this.calculateConfiguredBranch();
        });

        this.appStatusService.getApplicationStatus().subscribe((status: ApplicationStatus) => {
            this.applicationStatus = status;
            this.calculateConfiguredBranch();
            this.populateForm();
        });

        this.settingsForm = this.formBuilder.group({
            applicationName: null,
            applicationInformation: null,
        });
    }

    populateForm(): void {
        this.settingsForm.patchValue({
           applicationName: this.applicationStatus.configuration.applicationName,
           applicationInformation: this.applicationStatus.configuration.applicationInformation,
        });
    }

    calculateConfiguredBranch(): void {
        if (!this.branchBuilds || !this.applicationStatus || !this.applicationStatus.configuration) {
            return;
        }

        for (const branch of this.branchBuilds) {
            if (branch.branch.name !== this.applicationStatus.configuration.defaultBranchName) {
                this.configuredBranch = branch;
            }
        }
    }

    formInitialized(name: string, form: FormGroup): void {
        this.settingsForm.setControl(name, form);
    }

    resetConfiguration(): void {
        this.applicationStatus.configuration = this.configService.getRawConfigDataCopy();
        this.calculateConfiguredBranch();
    }

    updateConfiguration(): void {
        this.successfullyUpdatedConfiguration = false;

        this.configService.updateConfiguration(this.applicationStatus.configuration, () => {
            this.successfullyUpdatedConfiguration = true;
        });
    }

}
