/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import {Component, HostListener, Input} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {ScenarioResource} from '../../shared/services/scenarioResource.service';
import {ConfigurationService} from '../../services/configuration.service';
import {
    ILabelConfiguration,
    IScenario,
    IScenarioStatistics,
    IScenarioSummary,
    IStepDescription,
    IUseCase,
} from '../../generated-types/backend-types';
import {LocationService} from '../../shared/location.service';
import {LabelConfigurationMap, LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';
import {RouteParamsService} from '../../shared/route-params.service';
import {HumanReadablePipe} from '../../pipes/humanReadable.pipe';
import {MetadataTreeCreatorPipe} from '../../pipes/metadata/metadataTreeCreator.pipe';
import {RelatedIssueResource, RelatedIssueSummary} from '../../shared/services/relatedIssueResource.service';
import {MetadataTreeListCreatorPipe} from '../../pipes/metadata/metadataTreeListCreator.pipe';
import {SelectedComparison} from '../../diffViewer/selectedComparison.service';
import {forkJoin} from 'rxjs';
import {BuildDiffInfoService} from '../../diffViewer/services/build-diff-info.service';
import {UseCaseDiffInfosService} from '../../diffViewer/services/use-case-diff-infos.service';
import {DiffInfoService} from '../../diffViewer/diffInfo.service';
import {OrderPipe} from 'ngx-order-pipe';
import {UseCaseDiffInfoService} from '../../diffViewer/services/use-case-diff-info.service';
import {ScenarioDiffInfoService} from '../../diffViewer/services/scenario-diff-info.service';
import {StepDiffInfosService} from '../../diffViewer/services/step-diff-infos.service';
import {PageWithSteps} from '../../diffViewer/types/PageWithSteps';
import {IDetailsSections} from '../../components/detailarea/IDetailsSections';
import {IMainDetailsSection} from '../../components/detailarea/IMainDetailsSection';
import {ScSearchFilterPipe} from '../../pipes/searchFilter.pipe';

declare var angular: angular.IAngularStatic;

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

    scenarios: IScenarioSummary[] = [];
    scenario: IScenario;

    searchTerm: string;

    order: string = 'name';
    sortedSteps: any[];
    reverse: boolean = false;

    arrowkeyLocation: number = 0;

    selectedBranchAndBuild;

    labelConfigurations: LabelConfigurationMap = undefined;
    labelConfig: ILabelConfiguration = undefined;

    isComparisonExisting: boolean;
    comparisonBranchName;
    comparisonBuildName;

    getStatusStyleClass = undefined;

    propertiesToShow: any[];

    useCase: IUseCase;
    pagesAndSteps: PageWithSteps[];
    pagesAndStepsOrder: PageWithSteps[];
    steps: IStepDescription[];
    scenarioStatistics: IScenarioStatistics;

    mainDetailsSections: IMainDetailsSection[] = [];

    additionalDetailsSections: IDetailsSections;

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
                private searchFilterPipe: ScSearchFilterPipe) {
    }

    ngOnInit(): void {

        this.useCaseName = this.routeParamsService.useCaseName;
        this.scenarioName = this.routeParamsService.scenarioName;

        this.selectedBranchAndBuildService.callOnSelectionChange((selection) =>  this.loadSteps(selection));

        this.labelConfigurationsResource.query()
            .subscribe(((labelConfigurations) => {
                this.labelConfigurations = labelConfigurations;
            }));

        this.configurationService.scenarioPropertiesInOverview().subscribe((value) => {
            this.propertiesToShow = value;
        });

        this.getStatusStyleClass = (state) => this.configurationService.getStatusStyleClass(state);

        this.sortedSteps = this.orderPipe.transform(this.pagesAndSteps, this.order);
    }

    private loadSteps(selection) {
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
            this.pagesAndStepsOrder = this.pagesAndSteps;
            this.useCase = result.useCase;
            this.scenarioStatistics = result.scenarioStatistics;

            this.isComparisonExisting = this.selectedComparison.isDefined();

            if (this.isComparisonExisting) {
                this.loadDiffInfoData(result.pagesAndSteps, selection.branch, selection.build, this.selectedComparison.selected());
            } else {
                this.pagesAndSteps = result.pagesAndSteps;
            }

            this.additionalDetailsSections = this.metadataTreeListCreatorPipe.transform(result.scenario.details);

            this.relatedIssueResource.getForScenariosOverview({
                    branchName: selection.branch,
                    buildName: selection.build,
                },
                this.useCase.name,
            ).subscribe((relatedIssueSummary: RelatedIssueSummary[]) => {
                this.createInformationTreeArray(this.scenario, result.scenarioStatistics, this.useCase, this.useCase.labels.labels, relatedIssueSummary);
            });
        });
    }

    private loadDiffInfoData(pagesAndSteps, baseBranchName: string, baseBuildName: string, comparisonName: string) {
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

    private isAddedUseCase(buildDiffInfo) {
        return buildDiffInfo.addedElements.find((addedElement) => {
            return addedElement === this.useCaseName;
        });
    }

    private isAddedScenario(useCaseDiffInfo) {
        return useCaseDiffInfo.addedElements.find((addedElement) => {
            return addedElement === this.scenarioName;
        });
    }

    private markPagesAndStepsAsAdded(pagesAndSteps) {
        pagesAndSteps.forEach((pageAndStep) => {
            pageAndStep.page.diffInfo = {isAdded: true};
            pageAndStep.steps.forEach((step) => {
                step.diffInfo = {isAdded: true};
            });
        });
    }

    private loadUseCaseDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps) {
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

    private loadStepDiffInfos(baseBranchName, baseBuildName, comparisonName, pagesAndSteps) {
        forkJoin([
            this.scenarioDiffInfoService.get(baseBranchName, baseBuildName, comparisonName, this.useCaseName, this.scenarioName),
            this.stepDiffInfosService.get(baseBranchName, baseBuildName, comparisonName, this.useCaseName, this.scenarioName),
        ])
            .subscribe(([scenarioDiffInfo, stepDiffInfos]) => {
                this.diffInfoService.enrichPagesAndStepsWithDiffInfos(pagesAndSteps, scenarioDiffInfo.removedElements, stepDiffInfos);
            });
    }

    private resetSearchField() {
        this.searchTerm = '';
        if (this.reverse) {
            this.pagesAndStepsOrder = this.pagesAndSteps.slice().reverse();
        } else {
            this.pagesAndStepsOrder = this.pagesAndSteps;
        }
    }

    private setOrder(value: string) {
        if (this.order === value) {
            this.reverse = !this.reverse;
            this.pagesAndStepsOrder = this.pagesAndSteps.slice().reverse();
        }
        this.order = value;
    }
    @HostListener('window:keyup', ['$event'])
    private keyEvent(event: KeyboardEvent) {
        this.pagesAndStepsOrder = this.searchFilterPipe.transform(this.pagesAndStepsOrder, this.searchTerm);

        let pageIndex = Math.floor(this.arrowkeyLocation);
        const stepIndex = Math.floor((this.arrowkeyLocation - pageIndex) * 10);
        let stepsPerPage = this.pagesAndStepsOrder[pageIndex].steps.length;

        switch (event.code) {
            case 'ArrowDown':
                if (pageIndex < this.pagesAndStepsOrder.length && stepIndex < stepsPerPage) {
                    if (stepIndex < stepsPerPage - 1) {
                        this.arrowkeyLocation = this.arrowkeyLocation + 0.1;
                    } else if (pageIndex < this.pagesAndStepsOrder.length - 1) {
                        this.arrowkeyLocation = pageIndex + 1.0;
                    }
                }
                break;
            case 'ArrowUp':
                if (stepIndex === 0 && pageIndex !== 0) {
                    stepsPerPage = this.pagesAndStepsOrder[--pageIndex].steps.length;
                    this.arrowkeyLocation = parseFloat(pageIndex + '.' + (--stepsPerPage));
                } else if (stepIndex > 0 && pageIndex >= 0) {
                    this.arrowkeyLocation = this.arrowkeyLocation - 0.1;
                }
                break;
            case 'Enter':
                this.getLinkToStep(this.pagesAndStepsOrder[this.arrowkeyLocation].page.name, this.pagesAndSteps[this.arrowkeyLocation].page.pageOccurrence, 0);
                break;
        }
    }

    private getLinkToStep(pageName, pageOccurrence, stepInPageOccurrence) {

        this.locationService.path('/step/'
            + this.useCaseName + '/'
            + this.scenarioName + '/'
            + pageName + '/'
            + pageOccurrence + '/' + stepInPageOccurrence);
    }

    private getScreenShotUrl(imgName) {
        const branch = this.selectedBranchAndBuild.branch;
        const build = this.selectedBranchAndBuild.build;

        return 'rest/branch/' + branch + '/build/' + build +
            '/usecase/' + this.useCaseName + '/scenario/' + this.scenarioName + '/image/' + imgName;
    }

    private createInformationTreeArray(scenario, statistics, useCase, usecaseLabels, relatedIssues) {
        this.mainDetailsSections = [
            {
                name: 'Scenario',
                key: 'scenario',
                dataTree: this.createScenarioInformationTree(scenario, statistics, useCase),
                isFirstOpen: true,
                detailSectionType: 'treeComponent',
            },
            {
                name: 'Labels',
                key: 'labels',
                dataTree: {nodeLabel: 'label', childNodes: [this.createLabelInformationTree(usecaseLabels)]},
                isFirstOpen: false,
                detailSectionType: 'treeComponent',
                labelConfigurations: this.labelConfigurations,
            },
            {
                name: 'Related Sketches',
                key: '-relatedSketches',
                values: relatedIssues,
                isFirstOpen: false,
                detailSectionType: 'sketchesComponent',
            },
        ];
    }

    private createScenarioInformationTree(scenario, statistics, useCase) {
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

    private createLabelInformationTree(usecaseLabels) {
        const labelInformationTree: any = {};
        labelInformationTree['Use Case:'] = usecaseLabels;
        return this.metadataTreeCreatorPipe.transform(labelInformationTree);
    }
}

angular.module('scenarioo.directives')
    .directive('scStepsOverview',
        downgradeComponent({component: StepsOverviewComponent}) as angular.IDirectiveFactory);
