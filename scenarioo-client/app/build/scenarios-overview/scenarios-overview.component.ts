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

import {Component, HostListener} from '@angular/core';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {BranchesAndBuildsService} from '../../shared/navigation/branchesAndBuilds.service';
import {ScenarioResource} from '../../shared/services/scenarioResource.service';
import {LabelConfigurationMap, LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';
import {SelectedComparison} from '../../diffViewer/selectedComparison.service';
import {LocationService} from '../../shared/location.service';
import {ILabelConfiguration, IScenario, IScenarioDetails, IScenarioSummary, IUseCase, IUseCaseScenarios} from '../../generated-types/backend-types';
import {ConfigurationService} from '../../services/configuration.service';
import {OrderPipe} from 'ngx-order-pipe';
import {forkJoin} from 'rxjs';
import {UseCaseDiffInfoService} from '../../diffViewer/services/use-case-diff-info.service';
import {ScenarioDiffInfosService} from '../../diffViewer/services/scenario-diff-infos.service';
import {DiffInfoService} from '../../diffViewer/diffInfo.service';
import {MetadataTreeCreatorPipe} from '../../pipes/metadata/metadataTreeCreator.pipe';
import {RelatedIssueResource, RelatedIssueSummary} from '../../shared/services/relatedIssueResource.service';
import {RouteParamsService} from '../../shared/route-params.service';
import {MetadataTreeListCreatorPipe} from '../../pipes/metadata/metadataTreeListCreator.pipe';
import {ScSearchFilterPipe} from '../../pipes/searchFilter.pipe';
import {downgradeComponent} from '@angular/upgrade/static';
import {LocalStorageService} from '../../services/localStorage.service';
import {IMainDetailsSection} from '../IMainDetailsSection';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-scenarios-overview',
    template: require('./scenarios-overview.component.html'),
    styles: [require('./scenarios-overview.component.css').toString()],
})

export class ScenariosOverviewComponent {

    useCaseName: string;

    scenarios: IScenarioSummary[] = [];
    scenario: IScenario[] = [];

    searchTerm: string;

    order: string = 'name';
    sortedScenarios: any[];
    reverse: boolean = false;

    arrowkeyLocation: number = 0;

    labelConfigurations: LabelConfigurationMap = undefined;
    labelConfig: ILabelConfiguration = undefined;

    isPanelCollapsed: boolean;
    isComparisonExisting: boolean;

    mainDetailsSections: IMainDetailsSection[] = [];
    additionalDetailsSections: any[];

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private scenarioResource: ScenarioResource,
                private selectedComparison: SelectedComparison,
                private locationService: LocationService,
                private configurationService: ConfigurationService,
                private orderPipe: OrderPipe,
                private useCaseDiffInfoService: UseCaseDiffInfoService,
                private scenarioDiffInfosService: ScenarioDiffInfosService,
                private diffInfoService: DiffInfoService,
                private metadataTreeCreatorPipe: MetadataTreeCreatorPipe,
                private labelConfigurationsResource: LabelConfigurationsResource,
                private relatedIssueResource: RelatedIssueResource,
                private routeParams: RouteParamsService,
                private metadataTreeListCreatorPipe: MetadataTreeListCreatorPipe,
                private searchFilterPipe: ScSearchFilterPipe,
                private localStorageService: LocalStorageService) {
    }

    ngOnInit(): void {

        this.useCaseName = this.routeParams.useCaseName;

        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => this.loadScenario(selection));

        this.labelConfigurationsResource.query()
            .subscribe(((labelConfigurations: LabelConfigurationMap) => {
                this.labelConfigurations = labelConfigurations;
            }));

        this.isPanelCollapsed = this.localStorageService.getBoolean('scenarioo-metadataVisible-useCaseView', false);
    }

    private loadScenario(selection) {
        this.scenarioResource.getUseCaseScenarios({
                branchName: selection.branch,
                buildName: selection.build,
            },
            this.useCaseName,
        ).subscribe((useCaseScenarios: IUseCaseScenarios) => {

            this.isComparisonExisting = this.selectedComparison.isDefined();

            if (this.isComparisonExisting) {
                this.loadDiffInfoData(useCaseScenarios.scenarios, selection.branch, selection.build, this.selectedComparison.selected(), this.useCaseName);
            } else {
                this.scenarios = useCaseScenarios.scenarios;
            }

            this.additionalDetailsSections = this.metadataTreeListCreatorPipe.transform(useCaseScenarios.useCase.details);

            this.relatedIssueResource.getForScenariosOverview({
                branchName: selection.branch,
                buildName: selection.build,
                },
                useCaseScenarios.useCase.name,
            ).subscribe((relatedIssueSummary: RelatedIssueSummary[]) => {
                this.createInformationTreeArray(useCaseScenarios.useCase, useCaseScenarios.useCase.labels.labels, relatedIssueSummary);
            });
        });

        this.sortedScenarios = this.orderPipe.transform(this.scenarios, this.order);
    }

    getStatusStyleClass(state: string): string {
        return this.configurationService.getStatusStyleClass(state);
    }

    loadDiffInfoData(scenarios: IScenarioSummary[], baseBranchName: string, baseBuildName: string, comparisonName: string, useCaseName: string) {
        if (scenarios && baseBranchName && baseBuildName && useCaseName) {
            forkJoin([
                this.useCaseDiffInfoService.get(baseBranchName, baseBuildName, comparisonName, useCaseName),
                this.scenarioDiffInfosService.get(baseBranchName, baseBuildName, comparisonName, useCaseName),
            ])
                .subscribe(([useCaseDiffInfo, scenarioDiffInfos]) => {
                    this.scenarios = this.diffInfoService.getElementsWithDiffInfos(scenarios, useCaseDiffInfo.removedElements, scenarioDiffInfos, 'scenario.name');
                }, () => {
                    this.scenarios = this.diffInfoService.getElementsWithDiffInfos(scenarios, [], [], 'scenario.name');
                });
        }
    }

    resetSearchField() {
        this.searchTerm = '';
    }

    setOrder(value: string) {
        if (this.order === value) {
            this.reverse = !this.reverse;
        } else {
            this.reverse = false;
        }
        this.order = value;
    }

    @HostListener('window:keyup', ['$event'])
    keyEvent(event: KeyboardEvent) {
        switch (event.code) {
            case 'ArrowDown':
                const filteredScenarios = this.searchFilterPipe.transform(this.scenarios, this.searchTerm);
                if (this.arrowkeyLocation < (filteredScenarios.length - 1)) {
                    this.arrowkeyLocation++;
                }
                break;
            case 'ArrowUp':
                if (this.arrowkeyLocation > 0) {
                    this.arrowkeyLocation--;
                }
                break;
            case 'Enter':
                this.goToScenario(this.useCaseName, this.scenario[this.arrowkeyLocation].name);
                break;
        }
    }

    goToScenario(useCaseName: string, scenarioName: string) {
        this.locationService.path('/scenario/' + useCaseName + '/' + scenarioName);
    }

    goToStep(useCaseName: string, scenarioName: string) {
        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {
            // FIXME This could be improved, if the scenario service
            // for finding all scenarios would also retrieve the name of the first page
            this.scenarioResource.get(
                {
                    branchName: selection.branch,
                    buildName: selection.build,
                },
                useCaseName,
                scenarioName,
            ).subscribe(
                (scenarioResult: IScenarioDetails) => {
                    this.locationService.path('/step/' + useCaseName + '/' + scenarioName + '/' + scenarioResult.pagesAndSteps[0].page.name + '/0/0');
                },
            );
        });
    }

    getLabelStyle(labelName: string) {
        if (this.labelConfigurations) {
            this.labelConfig = this.labelConfigurations[labelName];
            if (this.labelConfig) {
                return {
                    'background-color': this.labelConfig.backgroundColor,
                    'color': this.labelConfig.foregroundColor,
                };
            }
        }
    }

    collapsePanel(isPanelCollapsed: boolean) {
        this.isPanelCollapsed = isPanelCollapsed;
    }

    createInformationTreeArray(usecaseInformationTree, labels, relatedIssues) {
        this.mainDetailsSections = [
            {
                name: 'Use Case',
                key: 'useCase',
                dataTree: this.createUseCaseInformationTree(usecaseInformationTree),
                isFirstOpen: true,
                detailSectionType: 'treeComponent',
            },
            {
                name: 'Labels',
                key: 'labels',
                dataTree: labels,
                isFirstOpen: false,
                detailSectionType: 'labelsComponent',
                config: this.labelConfigurations,
            },
            {
                name: 'Related Sketches',
                key: '-relatedSketches',
                dataTree: relatedIssues,
                isFirstOpen: false,
                detailSectionType: 'sketchesComponent',
            },
        ];
    }

    createUseCaseInformationTree(usecase: IUseCase) {
        const usecaseInformationTree: any = {};
        usecaseInformationTree['Use Case'] = usecase.name;
        if (usecase.description) {
            usecaseInformationTree.Description = usecase.description;
        }
        usecaseInformationTree.Status = usecase.status;
        return this.metadataTreeCreatorPipe.transform(usecaseInformationTree);
    }
}

angular.module('scenarioo.directives')
    .directive('scScenariosOverview',
        downgradeComponent({component: ScenariosOverviewComponent}) as angular.IDirectiveFactory);
