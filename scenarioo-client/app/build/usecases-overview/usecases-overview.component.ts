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
                private metadataTreeCreater: MetadataTreeCreatorPipe,
                private buildDiffInfoService: BuildDiffInfoService,
                private useCaseDiffInfosService: UseCaseDiffInfosService,
                private diffInfoService: DiffInfoService) {

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

    loadDiffInfoData(useCases: UseCaseSummary[], baseBranchName: string, baseBuildName: string, comparisonName: any) {
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
                this.goToUseCase(this.usecases[this.arrowkeyLocation].name);
                break;
        }
    }

    goToUseCase(useCase: string) {
        const params = this.locationService.path('/usecase/' + useCase);
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
        return this.metadataTreeCreater.transform(branchInformationTree);
    }

    createBuildInformationTree(build) {
        const buildInformationTree: any = {};
        buildInformationTree.Date = this.dateTimePipe.transform(build.date);
        buildInformationTree.Revision = build.revision;
        buildInformationTree.Status = build.status;
        return this.metadataTreeCreater.transform(buildInformationTree)
    }
}
