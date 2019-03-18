import {Component, Input, OnInit} from '@angular/core';
import {FormGroup, FormBuilder} from '@angular/forms';
import {BranchBuild, BranchesResource} from '../../shared/services/branchesResource.service';
import {ApplicationStatus, ApplicationStatusService, Configuration} from '../../shared/services/applicationStatus.service';
import {MigratedConfigService} from '../migrated-config.service';

@Component({
    selector: 'general-settings',
    template: require('./generalSettings.component.html'),
    styles: [require('./generalSettings.component.css').toString()],
})
export class GeneralSettingsComponent implements OnInit {

    settingsForm: FormGroup;

    applicationStatus: ApplicationStatus;
    configuration: Configuration;
    branchBuilds: BranchBuild[];
    configuredBranch: BranchBuild;

    successfullyUpdatedConfiguration: boolean;

    constructor(
        private formBuilder: FormBuilder,
        private branchesResource: BranchesResource,
        private configService: MigratedConfigService,
        private appStatusService: ApplicationStatusService,
    ) {
    }

    ngOnInit() {
        this.branchesResource.query().subscribe((branches) => {
            this.branchBuilds = branches;
            this.calculateConfiguredBranch();
        });

        this.appStatusService.getApplicationStatus().subscribe((status: ApplicationStatus) => {
            this.applicationStatus = status;
            this.configuration = status.configuration;
            this.calculateConfiguredBranch();
            this.populateForm();
        });

        this.settingsForm = this.formBuilder.group({
            applicationName: null,
            applicationInformation: null,
        });

        this.listenToFormChanges();
    }

    listenToFormChanges() {
        const valueChanges$ = this.settingsForm.valueChanges;
        valueChanges$.subscribe((val) => {
            this.configuration.applicationName = val.applicationName;
            this.configuration.applicationInformation = val.applicationInformation;
        });
    }

    populateForm(): void {
        this.settingsForm.patchValue({
            applicationName: this.configuration.applicationName,
            applicationInformation: this.configuration.applicationInformation,
        });
    }

    calculateConfiguredBranch(): void {
        if (!this.branchBuilds || !this.applicationStatus || !this.configuration) {
            return;
        }

        for (const branch of this.branchBuilds) {
            if (branch.branch.name !== this.configuration.defaultBranchName) {
                this.configuredBranch = branch;
            }
        }
    }

    formInitialized(name: string, form: FormGroup): void {
        this.settingsForm.setControl(name, form);
    }

    resetConfiguration(): void {
        this.configuration = this.configService.getRawConfigDataCopy();
        this.calculateConfiguredBranch();
        this.populateForm();
    }

    updateConfiguration(): void {
        this.successfullyUpdatedConfiguration = false;

        this.configService.updateConfiguration(this.configuration, () => {
            this.successfullyUpdatedConfiguration = true;
        });
    }

}
