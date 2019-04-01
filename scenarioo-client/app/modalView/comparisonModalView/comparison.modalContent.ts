import {Component, Input, SimpleChanges, OnChanges} from "@angular/core";
import {ComparisonStatusMapperService} from "../../manage/comparisons/comparisonStatusMapper.service";
import {ComparisonLogResource} from "../../shared/services/comparisonLogResource.service";

@Component({
    selector: 'comparison-modal',
    template: require('./comparison.modalView.html'),
    styles: [require('./comparison.modalView.css').toString()],
})

export class ComparisonModalContent implements OnChanges{

    @Input() comparison: {};
    comparisonLog;
    isActive: boolean;
    constructor(private comparisonStatusMapperService: ComparisonStatusMapperService, private comparisonLogResource: ComparisonLogResource){
    }

    ngOnChanges(changes: SimpleChanges): void {
        if(changes.comparison != this.comparison){
            this.getLog(this.comparison);
            console.log('Called ComparisonModalContent.ngOnChanges');
        }
    }

    private getLog(comparison){
        this.comparisonLogResource.logComparision(comparison.name, comparison.baseBuild)
            .then((log)=> {
                this.comparisonLog = log;
            }).catch(error => {
            console.log(error);
        });
    }

    private toggleActive(){
        this.isActive = !this.isActive;
    }

}
