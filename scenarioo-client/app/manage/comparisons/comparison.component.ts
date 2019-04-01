import {Component, OnInit} from "@angular/core";
import {ComparisonsResource} from "../../shared/services/comparisonsResource.service";
import {ComparisonRecalculateResource} from "../../shared/services/comparisonRecalculateResource.service";
import {ApplicationStatusService} from "../../shared/services/applicationStatus.service";
import {ComparisonStatusMapperService} from "./comparisonStatusMapper.service";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";

@Component({
    selector: 'comparison',
    template: require('./comparison.component.html'),
    styles: [require('./comparison.component.css').toString()],
})

export class ComparisonComponent implements OnInit {
    comparisons = [];
    table = {search: {searchTerm: ''}, sort: {column: 'date', reverse: true}, filtering: false};
    //SCOPE
    //getSytyleClassForComparisonStatus;
    version = {};

    constructor(private comparisonResource: ComparisonsResource,
                private comparisonRecalculateResource: ComparisonRecalculateResource,
                private comparisonStatusMapperService: ComparisonStatusMapperService,
                private applicationStatusService: ApplicationStatusService ){
    }

    ngOnInit(): void {
        this.comparisonResource.query().subscribe((comparisons) => {
            this.comparisons = comparisons;
            console.log(this.comparisons);
        });
        this.applicationStatusService.getApplicationStatus().subscribe((status) => {this.version = status.version});
    }

    resetSearchField() {
        this.table.search = {searchTerm: ''};
    }

    recalculateComparison(comparison) {
         this.comparisonRecalculateResource.recalculate(comparison.name, {
             branchName: comparison.baseBuild,
             buildName: comparison.baseBuild,
         })
             .catch(this.refresh)
             .then(this.refresh);
     }

    refresh(): void {
        location.reload();
     }
}

