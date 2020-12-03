import {Component, Input, TemplateRef} from '@angular/core';
import {IBuildDiffInfo} from '../../generated-types/backend-types';
import {ComparisonStatusMapperService} from '../../services/comparison-status-mapper.service';
import {ComparisonLogResource} from '../../shared/services/comparisonLogResource.service';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';

@Component({
    selector: 'sc-comparison-detail',
    template: require('./comparison-detail.component.html'),
})
export class ComparisonDetailComponent {

    @Input()
    comparison: IBuildDiffInfo;

    log: string;
    modalRef: BsModalRef;

    constructor(private comparisonLogResource: ComparisonLogResource,
                private comparisonStatusMapperService: ComparisonStatusMapperService,
                private modalService: BsModalService) {
    }

    openComparison(shareContent: TemplateRef<string>): void {
        this.comparisonLogResource.logComparision(this.comparison.name, this.comparison.baseBuild)
            .subscribe((log) => {
                this.log = log;
                this.modalRef = this.modalService.show(shareContent, {class: 'modal-lg'});
            });
    }

    getStyleClassForComparisonStatus(status: string) {
        return this.comparisonStatusMapperService.getStyleClassForComparisonStatus(status);
    }
}
