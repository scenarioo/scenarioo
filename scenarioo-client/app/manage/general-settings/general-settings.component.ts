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
import {map} from 'rxjs/operators';

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

    constructor(private applicationStatusService: ApplicationStatusService, private branchesResource: BranchesResource) {

    }

    ngOnInit(): void {
        this.applicationStatus$ = this.applicationStatusService.getApplicationStatus();
        this.applicationStatus$.subscribe((status) => {
            this.configuration = status.configuration;
        });

        this.branchesWithBuilds$ = this.branchesResource.query();
        this.branchesWithBuilds$.subscribe((branchesWithBuilds) => {
            this.builds = branchesWithBuilds.find((branchWithBuilds) => {
                return branchWithBuilds.branch.name === this.configuration.defaultBranchName;
            }).builds;
        });
        this.branches$ = this.branchesWithBuilds$.pipe(map((branchBuilds) => {
            return branchBuilds.map((branchBuild) => branchBuild.branch);
        }));
    }

}
