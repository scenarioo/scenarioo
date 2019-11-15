import {Component, OnInit} from '@angular/core';
import {BuildImportStatesResource} from '../../shared/services/buildImportStatesResource.service';
import {IBuildImportStatus, IBuildImportSummary} from '../../generated-types/backend-types';
import {ConfigurationService} from '../../services/configuration.service';
import {BuildReimportResource} from '../../shared/services/buildReimportResource.service';
import {BuildImportService} from '../../shared/services/buildImport.service';
import {BuildImportStatusService} from '../../services/build-import-status.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'sc-build-list',
    template: require('./build-list.component.html'),
})
export class BuildListComponent implements OnInit {
    buildImportStates$: Observable<IBuildImportSummary[]>;
    updatingBuildsInProgress: boolean = false;

    searchTerm: string;
    order: string = 'identifier.branchName';
    reverse: boolean = false;

    constructor(private buildImportStatesResource: BuildImportStatesResource,
                private buildReimportResource: BuildReimportResource,
                private buildImportService: BuildImportService,
                private configurationService: ConfigurationService,
                private buildImportStatusService: BuildImportStatusService) {
    }

    ngOnInit(): void {
        this.initializeBuildImportStates();
    }

    resetSearchField(): void {
        this.searchTerm = '';
    }

    getStyleClassForBuildImportStatus(status: IBuildImportStatus): string {
        return this.buildImportStatusService.getStyleClassForBuildImportStatus(status);
    }

    getStatusStyleClass(state: string): string {
        return this.configurationService.getStatusStyleClass(state);
    }

    setOrder(value: string): void {
        if (this.order === value) {
            this.reverse = !this.reverse;
        } else {
            this.reverse = false;
        }
        this.order = value;
    }

    reimportBuild(build: IBuildImportSummary): void {
        this.updatingBuildsInProgress = true;
        this.buildReimportResource.get(build.identifier.branchName, build.identifier.buildName)
            .subscribe(() => {
                this.updatingBuildsInProgress = false;
                this.buildImportFinished();
            });
    }

    importAndUpdateBuilds(): void {
        this.updatingBuildsInProgress = true;
        this.buildImportService.updateData()
            .subscribe(() => {
                this.updatingBuildsInProgress = false;
                this.buildImportFinished();
            });
    }

    private initializeBuildImportStates(): void {
        this.buildImportStates$ = this.buildImportStatesResource.get();
    }

    private buildImportFinished(): void {
        this.updatingBuildsInProgress = false;
        this.initializeBuildImportStates();
    }
}
