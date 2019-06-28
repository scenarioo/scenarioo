import {Component, Input} from '@angular/core';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {ScenarioResource} from '../../shared/services/scenarioResource.service';
import {LabelConfigurationMap} from '../../shared/services/labelConfigurationsResource.service';
import {SelectedComparison} from '../../diffViewer/selectedComparison.service';
import {LocationService} from '../../shared/location.service';
import {LabelConfigurationService} from '../../services/label-configuration.service';
import {IScenario, IScenarioSummary, IUseCaseScenarios} from '../../generated-types/backend-types';
import {ConfigurationService} from '../../services/configuration.service';
import {downgradeComponent} from '@angular/upgrade/static';
import {OrderPipe} from 'ngx-order-pipe';

@Component({
    selector: 'sc-scenarios-overview',
    template: require('./scenarios-overview.component.html'),
    styles: [require('./scenarios-overview.component.css').toString()],
})

export class ScenariosComponent {

    @Input()
    useCaseName: string;

    scenarios: IScenarioSummary[];
    scenario: IScenario[];
    propertiesToShow: any[];

    comparisonInfo : {};

    private sub: any;

    searchTerm: string;

    order: string = 'name';
    sortedScenarios: any[];
    reverse: boolean = false;

    arrowkeyLocation = 0;

    labelConfigurations: LabelConfigurationMap = undefined;
    labelConfig = undefined;

    getStatusStyleClass = undefined;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private scenarioResource: ScenarioResource,
                private labelConfigurationService: LabelConfigurationService,
                private selectedComparison: SelectedComparison,
                private locationService: LocationService,
                private configurationService: ConfigurationService,
                private orderPipe: OrderPipe,) {
    }


    ngOnInit(): void {


        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {

            /* To Delete if Name of Use Case is available*/
            this.useCaseName = "Donate";

            this.scenarioResource.getUseCaseScenarios({
                    branchName: selection.branch,
                    buildName: selection.build,
                },
                this.useCaseName,
            ).subscribe((useCaseScenarios: IUseCaseScenarios) => {
                this.scenarios = useCaseScenarios.scenarios
                    .map((scenarios: IScenarioSummary) => {
                        return {scenario: scenarios.scenario, numberOfSteps: scenarios.numberOfSteps}
                });
                //console.log(this.scenarios);
            });

            this.configurationService.scenarioPropertiesInOverview().subscribe((value) => {
                this.propertiesToShow = value;
            });
        });

        this.labelConfigurationService.get().subscribe((loadedLabelConfigurations) => {
            this.labelConfigurations = loadedLabelConfigurations;
        });
        //this.comparisonInfo = this.selectedComparison;

        this.getStatusStyleClass = (state) => this.configurationService.getStatusStyleClass(state);

        this.sortedScenarios = this.orderPipe.transform(this.scenarios, this.order);
    }


    goToScenario(useCaseName, scenarioName) {
        const params = this.locationService.path('/scenario/' + useCaseName + '/' + scenarioName);
    }

    getLabelStyle(labelName) {
        if (this.labelConfigurations) {
            this.labelConfig = this.labelConfigurations[labelName];
            if (this.labelConfig) {
                return {'background-color': this.labelConfig.backgroundColor, 'color': this.labelConfig.foregroundColor};
            }
        }
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

    handleEvent(event) {
        console.log("newEnterKeyIsWorking" + event);
    }
}

angular.module('scenarioo.directives')
    .directive('scScenariosOverview',
        downgradeComponent({component: ScenariosComponent}) as angular.IDirectiveFactory);
