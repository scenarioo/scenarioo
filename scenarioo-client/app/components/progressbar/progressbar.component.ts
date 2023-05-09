import {Component, HostListener, Input} from '@angular/core';
import {LocationService} from '../../shared/location.service';
import {RouteParamsService} from '../../shared/route-params.service';
import {IStepNavigation, IStepStatistics} from '../../generated-types/backend-types';

@Component({
    selector: 'sc-progressbar',
    template: require('./progressbar.component.html'),
    styles: [require('./progressbar.component.css').toString()],
})

export class ProgressbarComponent {

    private _stepNavigation: any;
    private _stepStatistics: any;

    useCaseName: string;
    scenarioName: string;

    stepIndex: number;
    totalNumberOfSteps: number;
    pageIndex: number;
    totalNumberOfPages: number;
    pageVariantIndex: number;
    totalNumberOfPageVariants: number;

    @Input()
    set stepNavigation(stepNavigation: IStepNavigation) {
        this._stepNavigation = stepNavigation;
        if (this._stepNavigation) {
            this.bindStepNavigation();
        }
    }

    @Input()
    set stepStatistics(stepStatistics: IStepStatistics) {
        this._stepStatistics = stepStatistics;
        if (this._stepStatistics) {
            this.bindStepStatistics();
        }
    }

    constructor(private locationService: LocationService,
                private routeParamsService: RouteParamsService) {
    }

    ngOnInit(): void {
        this.useCaseName = this.routeParamsService.useCaseName;
        this.scenarioName = this.routeParamsService.scenarioName;
    }

    @HostListener('window:keyup', ['$event'])
    keyEvent(event: KeyboardEvent) {
        switch (event.code) {
            case 'ArrowRight':
                this.goStepForward();
                break;
            case 'ArrowLeft':
                this.goStepBack();
                break;
            case 'Control' && 'ArrowRight':
                this.goPageForward();
                break;
            case 'Control' && 'ArrowLeft':
                this.goPageBack();
                break;
            case 'Control' && 'ArrowUp':
                this.goPageVariantBack();
                break;
            case 'Control' && 'ArrowDown':
                this.goPageVariantForward();
                break;
            case 'Control' && 'Home':
                this.goToFirstStep();
                break;
            case 'Control' && 'End':
                this.goToLastStep();
                break;
        }
    }

    go(data) {
        this.locationService.path('/step/' + (data.useCaseName || this.useCaseName) + '/' + (data.scenarioName || this.scenarioName) + '/' + data.pageName + '/' + data.pageOccurrence + '/' + data.stepInPageOccurrence);
    }

    bindStepNavigation() {
        if (this._stepNavigation === undefined) {
            return '?';
        } else {
            this.stepIndex = this._stepNavigation.stepIndex + 1;
            this.pageIndex = this._stepNavigation.pageIndex + 1;
            this.pageVariantIndex = this._stepNavigation.pageVariantIndex + 1;
            this.totalNumberOfPageVariants = this._stepNavigation.pageVariantsCount;
        }
    }

    bindStepStatistics() {
        if (this._stepStatistics === undefined) {
            return '?';
        } else {
            this.totalNumberOfSteps = this._stepStatistics.totalNumberOfStepsInScenario;
            this.totalNumberOfPages = this._stepStatistics.totalNumberOfPagesInScenario;
        }
    }

    goStepBack() {
        if (!this._stepNavigation || !this._stepNavigation.previousStep) {
            return;
        }
        this.go(this._stepNavigation.previousStep);
    }

    goStepForward() {
        if (!this._stepNavigation || !this._stepNavigation.nextStep) {
            return;
        }
        this.go(this._stepNavigation.nextStep);
    }

    goPageBack() {
        if (!this._stepNavigation || !this._stepNavigation.previousPage) {
            return;
        }
        this.go(this._stepNavigation.previousPage);
    }

    goPageForward() {
        if (!this._stepNavigation || !this._stepNavigation.nextPage) {
            return;
        }
        this.go(this._stepNavigation.nextPage);
    }

    goPageVariants(pageName) {
        this.locationService.path('/object/page/' + pageName);
    }

    goPageVariantBack() {
        if (!this._stepNavigation || !this._stepNavigation.previousStepVariant) {
            return;
        }
        this.go(this._stepNavigation.previousStepVariant);
    }

    goPageVariantForward() {
        if (!this._stepNavigation || !this._stepNavigation.nextStepVariant) {
            return;
        }
        this.go(this._stepNavigation.nextStepVariant);
    }

    goToFirstStep() {
        if (!this._stepNavigation || !this._stepNavigation.firstStep) {
            return;
        }
        this.go(this._stepNavigation.firstStep);
    }

    goToLastStep() {
        if (!this._stepNavigation || !this._stepNavigation.lastStep) {
            return;
        }
        this.go(this._stepNavigation.lastStep);
    }

}
