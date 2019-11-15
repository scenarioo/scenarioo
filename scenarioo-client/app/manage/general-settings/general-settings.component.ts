import {Component, OnInit} from '@angular/core';
import {ApplicationStatusService} from '../../shared/services/applicationStatus.service';
import {Observable} from 'rxjs';
import {IApplicationStatus, IConfiguration} from '../../generated-types/backend-types';

@Component({
    selector: 'sc-general-settings',
    template: require('./general-settings.component.html'),
    styles: [],
})
export class GeneralSettingsComponent implements OnInit {

    applicationStatus$: Observable<IApplicationStatus>;
    configuration: IConfiguration;

    constructor(private applicationStatusService: ApplicationStatusService) {

    }

    ngOnInit(): void {
        this.applicationStatus$ = this.applicationStatusService.getApplicationStatus();
        this.applicationStatus$.subscribe((status) => {
            this.configuration = status.configuration;
        });
    }

}
