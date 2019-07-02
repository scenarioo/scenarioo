import {Component, HostListener, Input} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {ScenarioResource} from '../../shared/services/scenarioResource.service';
import {ConfigurationService} from '../../services/configuration.service';
import {IPageWithSteps, IScenario, IScenarioStatistics, IStepDescription} from '../../generated-types/backend-types';
import {LocationService} from '../../shared/location.service';
import {LabelConfigurationMap, LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';


@Component({
    selector: 'sc-scenario-overview',
    template: require('./scenario-overview.component.html'),
    styles: [require('./scenario-overview.component.css').toString()],
})

export class ScenarioComponent {

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

    scenario: IScenario;
    pagesAndSteps: IPageWithSteps[];
    steps: IStepDescription[];
    scenarioStatistics: IScenarioStatistics;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private scenarioResource: ScenarioResource,
                private configurationService: ConfigurationService,
                private locationService: LocationService,
                private labelConfigurationsResource: LabelConfigurationsResource,) {
    }

    ngOnInit(): void {
        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {

            this.selectedBranchAndBuild = selection;

            /* To Delete if Name of Scenario and Use Case is available*/
            this.useCaseName = "Donate";
            this.scenarioName = "find_donate_page";

            this.scenarioResource.get({
                    branchName: selection.branch,
                    buildName: selection.build,
                },
                this.useCaseName,
                this.scenarioName,
            ).subscribe((result) => {
                console.log(result);
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
            case "ArrowDown":
                this.arrowkeyLocation++;
                break;
            case "ArrowUp":
                this.arrowkeyLocation--;
                break;
            case "Enter":
                console.log("enter is working ");
                //this.goToUseCase(this.useCaseName);
                break;
        }
    }

    handleEvent(event) {
        console.log("newEnterKeyIsWorking" + event);
    }

    getLinkToStep(pageName, pageOccurrence, stepInPageOccurrence) {

        this.locationService.path('/step/'
            + this.useCaseName + '/'
            + this.scenarioName + '/'
            + pageName + '/'
            + pageOccurrence + '/' + stepInPageOccurrence);
    }

    getScreenShotUrl(imgName) {
        let branch = this.selectedBranchAndBuild.branch;
        let build = this.selectedBranchAndBuild.build;

        return 'rest/branch/' + branch + '/build/' + build +
            '/usecase/' + this.useCaseName + '/scenario/' + this.scenarioName + '/image/' + imgName;
    }
}

angular.module('scenarioo.directives')
    .directive('scScenarioOverview',
        downgradeComponent({component: ScenarioComponent}) as angular.IDirectiveFactory);
