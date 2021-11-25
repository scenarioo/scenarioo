import {Component, OnInit} from '@angular/core';
import {ComparisonsResource} from '../../shared/services/comparisonsResource.service';
import {ApplicationStatusService} from '../../shared/services/applicationStatus.service';
import {IBuildDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {ComparisonRecalculateResource} from '../../shared/services/comparisonRecalculateResource.service';
import {ComparisonStatusMapperService} from '../../services/comparison-status-mapper.service';

@Component({
    selector: 'sc-comparison-list',
    template: require('./comparison-list.component.html'),
})
export class ComparisonListComponent implements OnInit {

    searchTerm: string;
    order: string = 'date';
    reverse: boolean = true;

    comparisons$: Observable<IBuildDiffInfo[]>;
    documentationVersion: string = '';

    constructor(private comparisonsResource: ComparisonsResource,
                private comparisonRecalculateResource: ComparisonRecalculateResource,
                private comparisonStatusMapperService: ComparisonStatusMapperService,
                private applicationStatusService: ApplicationStatusService) {
    }

    ngOnInit(): void {
        this.loadComparisons();

        this.applicationStatusService.getApplicationStatus().subscribe((status) => {
            this.documentationVersion = status.version.documentationVersion;
        });
    }

    private loadComparisons() {
        this.comparisons$ = this.comparisonsResource.query();
    }

    resetSearchField() {
        this.searchTerm = '';
    }

    getStyleClassForComparisonStatus(status: string) {
        return this.comparisonStatusMapperService.getStyleClassForComparisonStatus(status);
    }

    setOrder(value: string): void {
        if (this.order === value) {
            this.reverse = !this.reverse;
        } else {
            this.reverse = false;
        }
        this.order = value;
    }

    recalculateComparison(comparison) {
        this.comparisonRecalculateResource
            .recalculate(comparison.name, {
                branchName: comparison.baseBuild.branchName,
                buildName: comparison.baseBuild.buildName,
            })
            .subscribe(() => this.loadComparisons());
    }

    refresh() {
        this.loadComparisons();
    }
}
