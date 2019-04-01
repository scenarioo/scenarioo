import {ComparisonStatusMapperService} from "../comparisonStatusMapper.service";
import {Component, OnInit} from "@angular/core";
import {ComparisonLogResource} from "../../../shared/services/comparisonLogResource.service";

@Component({
    selector: 'componetDetail',
    template: require('./comparisonDetails.component.html'),
    styles: [require('./comparisonDetails.component.css').toString()]
})

export class ComparisonDetailsComponent implements OnInit{

    comparison;
    getStyleClassForComparisonStatus;
    log;

    constructor(private comparisonStatusMapperService: ComparisonStatusMapperService,
                private comparisonLogResource: ComparisonLogResource,
                comparison)
    {
        this.comparison=comparison;
    }

    ngOnInit(): void {
        this.comparisonLogResource.logComparision(this.comparison.name, this.comparison.baseBuild)
            .then((log) => {
                this.log = log;
            });
    }

    cancel(){
        // $uibModalInstance.dismiss('cancel');
    }
}

