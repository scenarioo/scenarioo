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
import {UseCasesResource, UseCaseSummary} from '../../shared/services/useCasesResource.service';
import {LabelConfigurationMap, LabelConfigurationsResource} from '../../shared/services/labelConfigurationsResource.service';
import {ConfigurationService} from '../../services/configuration.service';
import {SelectedComparison} from '../../diffViewer/selectedComparison.service';
import {OrderPipe} from 'ngx-order-pipe';
import {LocationService} from '../../shared/location.service';
import {MetadataTreeCreatorPipe} from '../../pipes/metadataTreeCreator.pipe';
import {BuildDiffInfoService} from '../../diffViewer/services/build-diff-info.service';
import {UseCaseDiffInfosService} from '../../diffViewer/services/use-case-diff-infos.service';
import {forkJoin} from 'rxjs';
import {DiffInfoService} from '../../diffViewer/diffInfo.service';
import {DateTimePipe} from '../../pipes/dateTime.pipe';
import {FilterPipe} from '../../pipes/filter.pipe';

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

    arrowkeyLocation: number = 0;

    labelConfigurations: LabelConfigurationMap = undefined;
    labelConfig = undefined;

    getStatusStyleClass = undefined;
    comparisonExisting = undefined;

    isPanelCollapsed: boolean;

    branchesAndBuilds = [];
    branchInformationTree = {};
    buildInformationTree = {};

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private branchesAndBuildsService: BranchesAndBuildsService,
                private useCasesResource: UseCasesResource,
                private labelConfigurationsResource: LabelConfigurationsResource,
                private configurationService: ConfigurationService,
                private orderPipe: OrderPipe,
                private dateTimePipe: DateTimePipe,
                private locationService: LocationService,
                private selectedComparison: SelectedComparison,
                private metadataTreeCreaterPipe: MetadataTreeCreatorPipe,
                private buildDiffInfoService: BuildDiffInfoService,
                private useCaseDiffInfosService: UseCaseDiffInfosService,
                private diffInfoService: DiffInfoService,
                private filterPipe: FilterPipe) {

    }

    ngOnInit(): void {
        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {

            this.branchesAndBuildsService.getBranchesAndBuilds().then((branchesAndBuilds) => {

                this.branchesAndBuilds = branchesAndBuilds;

                this.useCasesResource.query({
                    branchName: selection.branch,
                    buildName: selection.build,
                }).subscribe((useCaseSummaries: UseCaseSummary[]) => {

                    if (this.comparisonExisting) {
                        this.loadDiffInfoData(useCaseSummaries, selection.branch, selection.build, this.selectedComparison.selected());
                    } else {
                        this.usecases = useCaseSummaries;
                    }

                    const branch = branchesAndBuilds.selectedBranch.branch;
                    this.branchInformationTree = this.createBranchInformationTree(branch);

                    const build = branchesAndBuilds.selectedBuild.build;
                    this.buildInformationTree = this.createBuildInformationTree(build);
                });
            }).catch((error: any) => console.warn(error));
        });

        this.labelConfigurationsResource.query()
            .subscribe(((labelConfigurations) => {
                this.labelConfigurations = labelConfigurations;
            }));

        this.getStatusStyleClass = (state) => this.configurationService.getStatusStyleClass(state);

        this.sortedUsecases = this.orderPipe.transform(this.usecases, this.order);

        this.comparisonExisting = this.selectedComparison.isDefined();
    }

    loadDiffInfoData(useCases: UseCaseSummary[], baseBranchName: string, baseBuildName: string, comparisonName: string) {
        if (useCases && baseBranchName && baseBuildName) {
            forkJoin([
                this.buildDiffInfoService.get(baseBranchName, baseBuildName, comparisonName),
                this.useCaseDiffInfosService.get(baseBranchName, baseBuildName, comparisonName),
            ]).subscribe(([buildDiffInfo, useCaseDiffInfos]) => {
                this.usecases = this.diffInfoService.getElementsWithDiffInfos(useCases, buildDiffInfo.removedElements, useCaseDiffInfos, 'name');
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
                const filteredUsecases = this.filterPipe.transform(this.usecases, this.searchTerm);
                if (this.arrowkeyLocation < (filteredUsecases.length - 1)) {
                    this.arrowkeyLocation++;
                }
                break;
            case 'ArrowUp':
                if (this.arrowkeyLocation > 0) {
                    this.arrowkeyLocation--;
                }
                break;
            case 'Enter':
                this.goToUseCase(this.usecases[this.arrowkeyLocation].name);
                break;
        }
    }

    goToUseCase(useCase: string) {
        this.locationService.path('/usecase/' + useCase);
    }

    getLabelStyle(labelName: string) {
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

    createBranchInformationTree(branch) {
        const branchInformationTree: any = {};
        branchInformationTree.description = branch.description;
        return this.metadataTreeCreaterPipe.transform(branchInformationTree);
    }

    createBuildInformationTree(build) {
        const buildInformationTree: any = {};
        buildInformationTree.Date = this.dateTimePipe.transform(build.date);
        buildInformationTree.Revision = build.revision;
        buildInformationTree.Status = build.status;
        return this.metadataTreeCreaterPipe.transform(buildInformationTree);
    }
}
