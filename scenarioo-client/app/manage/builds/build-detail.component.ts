import {Component, Input, TemplateRef} from '@angular/core';
import {IBuildImportSummary} from '../../generated-types/backend-types';
import {ConfigurationService} from '../../services/configuration.service';
import {BuildImportLogResource} from '../../shared/services/buildImportLogResource.service';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';

@Component({
    selector: 'sc-build-detail',
    template: require('./build-detail.component.html'),
})
export class BuildDetailComponent {

    @Input()
    build: IBuildImportSummary;

    log: string;
    modalRef: BsModalRef;
    styleClassesForBuildImportStatus = {
        SUCCESS: 'label-success',
        FAILED: 'label-danger',
        UNPROCESSED: 'label-default',
        QUEUED_FOR_PROCESSING: 'label-info',
        PROCESSING: 'label-primary',
        OUTDATED: 'label-warning',
    };

    constructor(private buildImportLogResource: BuildImportLogResource,
                private configurationService: ConfigurationService,
                private modalService: BsModalService) {
    }

    goToBuild(shareContent: TemplateRef<string>) {
        this.buildImportLogResource.get(this.build.identifier.branchName, this.build.identifier.buildName)
            .subscribe((log) => {
                this.log = log;
                this.modalRef = this.modalService.show(shareContent, {class: 'modal-lg'});
            });
    }

    hasImportMessage(): boolean {
        return this.build.statusMessage != null;
    }

    getStyleClassForBuildImportStatus(status) {
        const styleClassFromMapping = this.styleClassesForBuildImportStatus[status];
        return styleClassFromMapping ? styleClassFromMapping : 'label-warning';
    }

    getStatusStyleClass(state) {
        return this.configurationService.getStatusStyleClass(state);
    }
}
