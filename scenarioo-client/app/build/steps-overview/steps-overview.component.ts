import {Component, HostListener, Input} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {ScenarioResource} from '../../shared/services/scenarioResource.service';
import {ConfigurationService} from '../../services/configuration.service';
import {IScenario, IScenarioStatistics, IStepDescription, IUseCase} from '../../generated-types/backend-types';
import {LocationService} from '../../shared/location.service';
import {LabelConfigurationMap, LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';
import {RouteParamsService} from '../../shared/route-params.service';
import {HumanReadablePipe} from '../../pipes/humanReadable.pipe';
import {MetadataTreeCreatorPipe} from '../../pipes/metadataTreeCreator.pipe';
import {RelatedIssueSummary} from '../../shared/services/relatedIssueResource.service';
import {RelatedIssueResource} from '../../shared/services/relatedIssueResource.service';
import {MetadataTreeListCreatorPipe} from '../../pipes/metadataTreeListCreator.pipe';
import {SelectedComparison} from '../../diffViewer/selectedComparison.service';
import {forkJoin} from 'rxjs';
import {BuildDiffInfoService} from '../../diffViewer/services/build-diff-info.service';
import {UseCaseDiffInfosService} from '../../diffViewer/services/use-case-diff-infos.service';
import {DiffInfoService} from '../../diffViewer/diffInfo.service';
import {OrderPipe} from 'ngx-order-pipe';
import {UseCaseDiffInfoService} from '../../diffViewer/services/use-case-diff-info.service';
import {ScenarioDiffInfoService} from '../../diffViewer/services/scenario-diff-info.service';
import {StepDiffInfosService} from '../../diffViewer/services/step-diff-infos.service';
import {FilterPipe} from '../../pipes/filter.pipe';

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

    scenarios;

    searchTerm: string;

    order: string = 'name';
    sortedSteps: any[];
    reverse: boolean = false;

    arrowkeyLocation = 0;

    selectedBranchAndBuild;

    labelConfigurations: LabelConfigurationMap = undefined;
    labelConfig = undefined;

    comparisonExisting = undefined;
    comparisonBranchName;
    comparisonBuildName;

    getStatusStyleClass = undefined;

    propertiesToShow: any[];

    isPanelCollapsed: boolean;

    scenario: IScenario;
    useCase: IUseCase;
    pagesAndSteps;
    steps: IStepDescription[];
    scenarioStatistics: IScenarioStatistics;

    scenarioInformationTree = {};
    metadataInformationTree = [];
    relatedIssues;
    labels = {};

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private scenarioResource: ScenarioResource,
                private configurationService: ConfigurationService,
                private locationService: LocationService,
                private labelConfigurationsResource: LabelConfigurationsResource,
                private routeParamsService: RouteParamsService,
                private humanReadablePipe: HumanReadablePipe,
                private metadataTreeCreatorPipe: MetadataTreeCreatorPipe,
                private relatedIssueResource: RelatedIssueResource,
                private metadataTreeListCreatorPipe: MetadataTreeListCreatorPipe,
                private selectedComparison: SelectedComparison,
                private buildDiffInfoService: BuildDiffInfoService,
                private useCaseDiffInfosService: UseCaseDiffInfosService,
                private useCaseDiffInfoService: UseCaseDiffInfoService,
                private diffInfoService: DiffInfoService,
                private orderPipe: OrderPipe,
                private scenarioDiffInfoService: ScenarioDiffInfoService,
                private stepDiffInfosService: StepDiffInfosService,
                private filterPipe: FilterPipe) {
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
                console.log(this.pagesAndSteps);
                this.useCase = result.useCase;
                this.scenarioStatistics = result.scenarioStatistics;

                this.scenarioInformationTree = this.createScenarioInformationTree(this.scenario, result.scenarioStatistics, this.useCase);
                this.metadataInformationTree = this.metadataTreeListCreatorPipe.transform(result.scenario.details);
                this.labels = this.useCase.labels.labels;

                this.relatedIssueResource.getForScenariosOverview({
                        branchName: selection.branch,
                        buildName: selection.build,
                    },
                    this.useCase.name,
                ).subscribe((relatedIssueSummary: RelatedIssueSummary[]) => {
                    this.relatedIssues = relatedIssueSummary;
                });

                if (this.comparisonExisting) {
                    this.loadDiffInfoData(result.pagesAndSteps, selection.branch, selection.build, this.selectedComparison.selected());
                } else {
                    this.pagesAndSteps = result.pagesAndSteps;
                }

            });

            this.configurationService.scenarioPropertiesInOverview().subscribe((value) => {
                this.propertiesToShow = value;
            });

            this.labelConfigurationsResource.query()
                .subscribe(((labelConfigurations) => {
                    this.labelConfigurations = labelConfigurations;
                }));

            this.getStatusStyleClass = (state) => this.configurationService.getStatusStyleClass(state);

            this.sortedSteps = this.orderPipe.transform(this.pagesAndSteps, this.order);

            this.comparisonExisting = this.selectedComparison.isDefined();
        });
    }

    loadDiffInfoData(pagesAndSteps, baseBranchName: string, baseBuildName: string, comparisonName: string) {
        if (pagesAndSteps && baseBranchName && baseBuildName && this.useCaseName && this.scenarioName) {
            this.buildDiffInfoService.get(baseBranchName, baseBuildName, comparisonName)
                .subscribe((buildDiffInfo) => {
                    this.comparisonBranchName = buildDiffInfo.compareBuild.branchName;
                    this.comparisonBuildName = buildDiffInfo.compareBuild.buildName;

                    if (this.isAddedUseCase(buildDiffInfo)) {
                        this.markPagesAndStepsAsAdded(pagesAndSteps);
                    } else {
                        this.loadUseCaseDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps);
                    }
                });
        }
    }

    isAddedUseCase(buildDiffInfo) {
        return buildDiffInfo.addedElements.find((addedElement) => {
            return addedElement === this.useCaseName;
        });
    }

    isAddedScenario(useCaseDiffInfo) {
        return useCaseDiffInfo.addedElements.find((addedElement) => {
            return addedElement === this.scenarioName;
        });
    }

    markPagesAndStepsAsAdded(pagesAndSteps) {
        pagesAndSteps.forEach((pageAndStep) => {
            pageAndStep.page.diffInfo = {isAdded: true};
            pageAndStep.steps.forEach((step) => {
                step.diffInfo = {isAdded: true};
            });
        });
    }

    loadUseCaseDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps) {
        this.useCaseDiffInfoService.get(baseBranchName, baseBuildName, comparisonName, this.useCaseName)
            .subscribe((useCaseDiffInfo) => {
                if (this.isAddedScenario(useCaseDiffInfo)) {
                    this.markPagesAndStepsAsAdded(pagesAndSteps);
                } else {
                    this.loadStepDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps);
                }
            }, (error) => {
                throw error;
            });

    }

    loadStepDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps) {
        forkJoin([
            this.scenarioDiffInfoService.get(baseBranchName, baseBuildName, comparisonName, this.useCaseName, this.scenarioName),
            this.stepDiffInfosService.get(baseBranchName, baseBuildName, comparisonName, this.useCaseName, this.scenarioName),
        ])
            .subscribe(([scenarioDiffInfo, stepDiffInfos]) => {
                this.diffInfoService.enrichPagesAndStepsWithDiffInfos(pagesAndSteps, scenarioDiffInfo.removedElements, stepDiffInfos);
            });
    }

    createScenarioInformationTree(scenario, statistics, useCase) {
        const stepInformation: any = {};
        stepInformation['Use Case'] = useCase.name;
        if (useCase.description) {
            stepInformation['Use Case Description'] = useCase.description;
        }
        stepInformation.Scenario = this.humanReadablePipe.transform(scenario.name);
        if (scenario.description) {
            stepInformation['Scenario Description'] = scenario.description;
        }

        stepInformation['Number of Pages'] = statistics.numberOfPages;
        stepInformation['Number of Steps'] = statistics.numberOfSteps;
        stepInformation.Status = scenario.status;
        return this.metadataTreeCreatorPipe.transform(stepInformation);
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
                /* TODO: set restriction for keyboard navigation (++) on filtered list */
                /*
                const filteredPages = this.filterPipe.transform(this.pagesAndSteps, this.searchTerm);
                const filteredSteps = this.filterPipe.transform(this.pagesAndSteps.steps, this.searchTerm);
                if (this.arrowkeyLocation < ((filteredPages.length * filteredSteps.length) - 1)) {
                    this.arrowkeyLocation++;
                }
                */
                if (this.arrowkeyLocation < this.scenarioStatistics.numberOfSteps - 1){
                    this.arrowkeyLocation++;
                }
                break;
            case 'ArrowUp':
                if (this.arrowkeyLocation > 0) {
                    this.arrowkeyLocation--;
                }
                break;
            case 'Enter':
                this.getLinkToStep(this.pagesAndSteps[this.arrowkeyLocation].page.name, this.pagesAndSteps[this.arrowkeyLocation].page.pageOccurrence, 0);
                break;
        }
    }

    getLinkToStep(pageName, pageOccurrence, stepInPageOccurrence) {

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
