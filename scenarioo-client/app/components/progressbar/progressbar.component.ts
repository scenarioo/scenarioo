import {Component, Input} from '@angular/core';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {RouteParamsService} from '../../shared/route-params.service';

@Component({
    selector: 'sc-progressbar',
    template: require('./progressbar.component.html'),
    styles: [require('./progressbar.component.css').toString()],
})

export class ProgressbarComponent {

    max: number = 5;
    dynamic: number = 2;

    currentPage: number = 1;
    totalNumberOfPageInScenario: number = 5;

    currentPageVariant: number = 3;
    totalNumberOfPageVariant: number = 13;

    // @Input()
    stepNavigation;

    @Input()
    scenarioName: string;

    stepBack(){}

    stepForward(){}

    page(){}

    pageBack(){}

    pageForward(){}

    pageVariant(){}

    pageVariantUp(){}

    pageVariantDown(){}

    constructor(private routeParamsService: RouteParamsService,
                private selectedBranchAndBuildService: SelectedBranchAndBuildService){

    }

    ngOnInit(): void {
        console.log('progressbar.component, ngOnInit', this.stepNavigation);
        this.stepNavigation = this.routeParamsService.stepNavigation;
        console.log('progressbar.component, ngOnInit, RouteParamsService', this.stepNavigation);
        // this.selectedBranchAndBuildService.callOnSelectionChange(this.bindStepNavigation());
    }

    bindStepNavigation() {
        this.getCurrentStepIndexForDisplay();
    }

    getCurrentStepIndexForDisplay(){
        if (this.stepNavigation === undefined) {
            return '?';
        }
        this.dynamic = this.stepNavigation.stepIndex + 1;
    }

}
