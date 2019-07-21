import {Component, HostListener, Input} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {ScenarioResource} from '../../shared/services/scenarioResource.service';
import {ConfigurationService} from '../../services/configuration.service';
import {IPageWithSteps, IScenario, IScenarioStatistics, IStepDescription} from '../../generated-types/backend-types';
import {LocationService} from '../../shared/location.service';
import {LabelConfigurationMap, LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';
import {RouteParamsService} from '../../shared/route-params.service';

@Component({
    selector: 'sc-steps-overview',
    template: require('./steps-overview.component.html'),
    styles: [require('./steps-overview.component.css').toString()],
})

export class StepsOverviewComponent {

    @Input()
    useCaseName: string;

    @Input()
    scenarioName: string;

    searchTerm: string;

    order: string = 'name';
    sortedScenarios: any[];
    reverse: boolean = false;

    arrowkeyLocation = 0;

    selectedBranchAndBuild;

    labelConfigurations: LabelConfigurationMap = undefined;
    labelConfig = undefined;

    getStatusStyleClass = undefined;

    propertiesToShow: any[];

    isPanelCollapsed: boolean;

    scenario: IScenario;
    pagesAndSteps: IPageWithSteps[];
    steps: IStepDescription[];
    scenarioStatistics: IScenarioStatistics;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private scenarioResource: ScenarioResource,
                private configurationService: ConfigurationService,
                private locationService: LocationService,
                private labelConfigurationsResource: LabelConfigurationsResource,
                private routeParamsService: RouteParamsService) {
    }

    ngOnInit(): void {

        this.useCaseName = this.routeParamsService.useCaseName;
        this.scenarioName = this.routeParamsService.scenarioName;

        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {

            this.selectedBranchAndBuild = selection;

            this.scenarioResource.get({
                    branchName: selection.branch,
                    buildName: selection.build,
                },
                this.useCaseName,
                this.scenarioName,
            ).subscribe((result) => {
                this.scenario = result.scenario;
                this.pagesAndSteps = result.pagesAndSteps;
                this.scenarioStatistics = result.scenarioStatistics;

            });

            this.configurationService.scenarioPropertiesInOverview().subscribe((value) => {
                this.propertiesToShow = value;
            });

            this.labelConfigurationsResource.query()
                .subscribe(((labelConfigurations) => {
                    this.labelConfigurations = labelConfigurations;
                }));

            this.getStatusStyleClass = (state) => this.configurationService.getStatusStyleClass(state);
        });
    }

    getLabelStyle(labelName) {
        if (this.labelConfigurations) {
            this.labelConfig = this.labelConfigurations[labelName];
            if (this.labelConfig) {
                return {'background-color': this.labelConfig.backgroundColor, 'color': this.labelConfig.foregroundColor};
            }
        }
    }

    collapsePanel(event) {
        this.isPanelCollapsed = event;
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
                this.getLinkToStep(this.pagesAndSteps[this.arrowkeyLocation].page.name, this.pagesAndSteps[this.arrowkeyLocation].page.pageOccurrence, 0);
                break;
        }
    }

    getLinkToStep(pageName, pageOccurrence, stepInPageOccurrence) {

        console.log('steps', stepInPageOccurrence);

        this.locationService.path('/step/'
            + this.useCaseName + '/'
            + this.scenarioName + '/'
            + pageName + '/'
            + pageOccurrence + '/' + stepInPageOccurrence);
    }

    getScreenShotUrl(imgName) {
        const branch = this.selectedBranchAndBuild.branch;
        const build = this.selectedBranchAndBuild.build;

        return 'rest/branch/' + branch + '/build/' + build +
            '/usecase/' + this.useCaseName + '/scenario/' + this.scenarioName + '/image/' + imgName;
    }
}

angular.module('scenarioo.directives')
    .directive('scStepsOverview',
        downgradeComponent({component: StepsOverviewComponent}) as angular.IDirectiveFactory);
