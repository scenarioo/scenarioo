import {Component, HostListener} from '@angular/core';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {UseCasesResource, UseCaseSummary} from '../../shared/services/useCasesResource.service';
import {LabelConfigurationMap, LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';
import {ConfigurationService} from '../../services/configuration.service';
import {SelectedComparison} from '../../diffViewer/selectedComparison.service';
import {OrderPipe} from 'ngx-order-pipe';
import {LocationService} from '../../shared/location.service';
import {catchError} from 'rxjs/operators';

@Component({
    selector: 'sc-usecases-overview',
    template: require('./usecases-overview.component.html'),
    styles: [require('./usecases-overview.component.css').toString()],
})

export class UseCasesComponent {

    usecases: UseCaseSummary[] = [];

    searchTerm: string;

    order: string = 'name';
    sortedUsecases: any[];
    reverse: boolean = false;

    arrowkeyLocation = 0;

    labelConfigurations: LabelConfigurationMap = undefined;
    labelConfig = undefined;

    getStatusStyleClass = undefined;
    comparisonInfo = undefined;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private useCasesResource: UseCasesResource,
                private labelConfigurationsResource: LabelConfigurationsResource,
                private configurationService: ConfigurationService,
                private orderPipe: OrderPipe,
                private locationService: LocationService,
                private selectedComparison: SelectedComparison ) {

    }

    ngOnInit(): void {
        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {

            this.branchesAndBuildsService.getBranchesAndBuilds().then((branchesAndBuilds) => {
                // console.log(branchesAndBuilds);
                this.useCasesResource.query({
                    branchName: selection.branch,
                    buildName: selection.build,
                }).subscribe((useCaseSummaries: UseCaseSummary[]) => {
                    this.usecases = useCaseSummaries;
                    // console.log(useCaseSummaries);
                });
            }).catch((error: any) => console.warn(error));
        });

        this.labelConfigurationsResource.query()
            .subscribe(((labelConfigurations) => {
                this.labelConfigurations = labelConfigurations;
            }));

        this.getStatusStyleClass = (state) => this.configurationService.getStatusStyleClass(state);

        this.sortedUsecases = this.orderPipe.transform(this.usecases, this.order);
        console.log(this.sortedUsecases);

        /*
        this.selectedComparison.callOnSelectionChange((info) => {
            this.comparisonInfo = info;
        });
        console.log(this.comparisonInfo);
        */
    }

    resetSearchField() {
        this.searchTerm = '';
    }

    setOrder(value: string) {
        if (this.order === value) {
            this.reverse = !this.reverse;
        }
        this.order = value;
    }

    @HostListener('window:keyup', ['$event'])
    keyEvent(event: KeyboardEvent) {
        switch (event.code) {
            case 'ArrowDown':
                this.arrowkeyLocation++;
                break;
            case 'ArrowUp':
                this.arrowkeyLocation--;
                break;
            case 'Enter':
                console.log('enter is working');
                // this.goToUseCase(this.useCaseName);
                break;
        }
    }

    goToUseCase(useCase) {
        const params = this.locationService.path('/usecase/' + useCase);
    }

    getLabelStyle(labelName) {
        if (this.labelConfigurations) {
            this.labelConfig = this.labelConfigurations[labelName];
            if (this.labelConfig) {
                return {'background-color': this.labelConfig.backgroundColor, 'color': this.labelConfig.foregroundColor};
            }
        }
    }
}
