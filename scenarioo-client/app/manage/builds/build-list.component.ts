import {Component, OnInit} from '@angular/core';
import {tap} from 'rxjs/operators';
import {BuildImportStatesResource} from '../../shared/services/buildImportStatesResource.service';
import {IBuildImportSummary} from '../../generated-types/backend-types';
import {ConfigurationService} from '../../services/configuration.service';
import {BuildReimportResource} from '../../shared/services/buildReimportResource.service';
import {BuildImportService} from '../../shared/services/buildImport.service';

@Component({
    selector: 'sc-build-list',
    template: require('./build-list.component.html'),
})
export class BuildListComponent implements OnInit {
    buildImportStates: IBuildImportSummary[];
    updatingBuildsInProgress: boolean = false;
    searchTerm: string;
    order: string = 'identifier.branchName';
    reverse: boolean = false;
    styleClassesForBuildImportStatus = {
        SUCCESS: 'label-success',
        FAILED: 'label-danger',
        UNPROCESSED: 'label-default',
        QUEUED_FOR_PROCESSING: 'label-info',
        PROCESSING: 'label-primary',
        OUTDATED: 'label-warning',
    };

    constructor(private buildImportStatesResource: BuildImportStatesResource,
                private buildReimportResource: BuildReimportResource,
                private buildImportService: BuildImportService,
                private configurationService: ConfigurationService) {
    }

    ngOnInit(): void {
        this.buildImportStatesResource.get()
            .subscribe((buildImportStates) => {
                this.buildImportStates = buildImportStates;
            });
    }

    resetSearchField() {
        this.searchTerm = '';
    }

    getStyleClassForBuildImportStatus(status) {
        const styleClassFromMapping = this.styleClassesForBuildImportStatus[status];
        return styleClassFromMapping ? styleClassFromMapping : 'label-warning';
    }

    getStatusStyleClass(state) {
        return this.configurationService.getStatusStyleClass(state);
    }

    setOrder(value: string) {
        if (this.order === value) {
            this.reverse = !this.reverse;
        } else {
            this.reverse = false;
        }
        this.order = value;
    }

    reimportBuild(build: IBuildImportSummary) {
        this.updatingBuildsInProgress = true;
        this.buildReimportResource.get(build.identifier.branchName, build.identifier.buildName)
            .pipe(tap(() => this.updatingBuildsInProgress = false))
            .subscribe(() => this.buildImportFinished());
    }

    importAndUpdateBuilds() {
        this.updatingBuildsInProgress = true;
        this.buildImportService.updateData()
            .pipe(tap(() => this.updatingBuildsInProgress = false))
            .subscribe(() => this.buildImportFinished());
    }

    private buildImportFinished() {
        this.updatingBuildsInProgress = false;
        this.ngOnInit();
    }
}
