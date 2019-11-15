import {Component, Input, TemplateRef} from '@angular/core';
import {IBuildImportStatus, IBuildImportSummary} from '../../generated-types/backend-types';
import {ConfigurationService} from '../../services/configuration.service';
import {BuildImportLogResource} from '../../shared/services/buildImportLogResource.service';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {BuildImportStatusService} from '../../services/build-import-status.service';

@Component({
    selector: 'sc-build-detail',
    template: require('./build-detail.component.html'),
})
export class BuildDetailComponent {

    @Input()
    build: IBuildImportSummary;

    log: string;
    modalRef: BsModalRef;

    constructor(private buildImportLogResource: BuildImportLogResource,
                private configurationService: ConfigurationService,
                private buildImportStatusService: BuildImportStatusService,
                private modalService: BsModalService) {
    }

    openBuild(shareContent: TemplateRef<string>): void {
        this.buildImportLogResource.get(this.build.identifier.branchName, this.build.identifier.buildName)
            .subscribe((log) => {
                this.log = log;
                this.modalRef = this.modalService.show(shareContent, {class: 'modal-lg'});
            });
    }

    hasImportMessage(): boolean {
        return this.build.statusMessage !== null;
    }

    getStyleClassForBuildImportStatus(status: IBuildImportStatus): string {
        return this.buildImportStatusService.getStyleClassForBuildImportStatus(status);
    }

    getStatusStyleClass(state: string): string {
        return this.configurationService.getStatusStyleClass(state);
    }
}
