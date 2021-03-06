import {Component, OnInit} from '@angular/core';
import {ApplicationStatusService} from '../../shared/services/applicationStatus.service';
import {Observable} from 'rxjs';
import {
    IApplicationStatus,
    IBranch,
    IBranchBuilds,
    IBuildLink,
    IConfiguration,
} from '../../generated-types/backend-types';
import {BranchesResource} from '../../shared/services/branchesResource.service';
import {combineLatest} from 'rxjs';
import {map} from 'rxjs/operators';
import {ConfigurationService} from '../../services/configuration.service';

@Component({
    selector: 'sc-general-settings',
    template: require('./general-settings.component.html'),
    styles: [],
})
export class GeneralSettingsComponent implements OnInit {

    applicationStatus$: Observable<IApplicationStatus>;
    branchesWithBuilds$: Observable<IBranchBuilds[]>;
    branches$: Observable<IBranch[]>;

    configuration: IConfiguration;
    builds: IBuildLink[] = [];
    successfullyUpdatedConfiguration: boolean = false;

    constructor(private applicationStatusService: ApplicationStatusService,
                private branchesResource: BranchesResource,
                private configurationService: ConfigurationService) {
    }

    ngOnInit(): void {
        this.applicationStatus$ = this.applicationStatusService.getApplicationStatus();
        this.applicationStatus$.subscribe((status) => {
            this.configuration = status.configuration;
        });

        this.branchesWithBuilds$ = this.branchesResource.query();

        combineLatest([this.branchesWithBuilds$, this.applicationStatus$])
            .subscribe(([branchesWithBuilds, applicationStatus]) => {
                this.builds = branchesWithBuilds.find((branchWithBuilds) => {
                    return branchWithBuilds.branch.name === applicationStatus.configuration.defaultBranchName;
                }).builds;
            });
        this.branches$ = this.branchesWithBuilds$.pipe(map((branchBuilds) => {
            return branchBuilds.map((branchBuild) => branchBuild.branch);
        }));
    }

    resetConfiguration() {
        this.configuration = this.configurationService.getRawCopy();
    }

    updateConfiguration() {
        this.successfullyUpdatedConfiguration = false;
        this.configurationService.updateConfiguration(this.configuration).subscribe(() => {
            this.successfullyUpdatedConfiguration = true;
        });
    }
}
