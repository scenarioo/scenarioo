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

import {Component} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {SelectedBranchAndBuildService} from '../../shared/navigation/selectedBranchAndBuild.service';
import {StepResource} from '../../shared/services/stepResource.service';
import {RouteParamsService} from '../../shared/route-params.service';
import {IMainDetailsSection} from '../../components/detailarea/IMainDetailsSection';
import {MetadataTreeCreatorPipe} from '../../pipes/metadata/metadataTreeCreator.pipe';
import {RelatedIssueResource, RelatedIssueSummary} from '../../shared/services/relatedIssueResource.service';
import {LocationService} from '../../shared/location.service';
import {IDetailsSections} from '../../components/detailarea/IDetailsSections';
import {MetadataTreeListCreatorPipe} from '../../pipes/metadata/metadataTreeListCreator.pipe';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-step-view',
    template: require('./step-view.component.html'),
    styles: [require('./step-view.component.css').toString()],
})
export class StepViewComponent {

    useCaseName: string;
    scenarioName: string;

    pageName: string;
    pageOccurrence: number;
    stepInPageOccurrence: number;
    labels;

    stepNavigation;
    stepStatistics;
    stepInformationTree;

    scenarioLabels;
    useCaseLabels;

    stepNotFound: boolean = false;

    mainDetailsSections: IMainDetailsSection[] = [];
    additionalDetailsSections: IDetailsSections;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private routeParams: RouteParamsService,
                private stepResource: StepResource,
                private metadataTreeCreatorPipe: MetadataTreeCreatorPipe,
                private metadataTreeListCreatorPipe: MetadataTreeListCreatorPipe,
                private relatedIssueResource: RelatedIssueResource,
                private locationService: LocationService) {
    }

    ngOnInit(): void {
        this.useCaseName = this.routeParams.useCaseName;
        this.scenarioName = this.routeParams.scenarioName;
        this.pageName = this.routeParams.pageName;
        this.pageOccurrence = parseInt(this.routeParams.pageOccurrence, 10);
        this.stepInPageOccurrence = parseInt(this.routeParams.stepInPageOccurrence, 10);

        this.labels = this.locationService.search().labels;
        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => this.loadStep(selection));
    }

    private loadStep(selection) {
        this.stepResource.get(
            {
                branchName: selection.branch,
                buildName: selection.build,
            },
            this.useCaseName,
            this.scenarioName,
            this.pageName,
            this.pageOccurrence,
            this.stepInPageOccurrence,
            this.labels,
        ).subscribe((result) => {
            this.stepNavigation = result.stepNavigation;
            this.stepStatistics = result.stepStatistics;
            this.useCaseLabels = result.useCaseLabels;
            this.scenarioLabels = result.scenarioLabels;
            this.additionalDetailsSections = this.metadataTreeListCreatorPipe.transform(result.step.metadata.details);

            this.relatedIssueResource.get({
                branchName: selection.branch,
                buildName: selection.build,
            },
                this.useCaseName,
                this.scenarioName,
                this.pageName,
                this.pageOccurrence,
                this.stepInPageOccurrence,
            ).subscribe((relatedIssueSummary: RelatedIssueSummary[]) => {
                this.stepInformationTree = this.createInformationTreeArray(result.step, relatedIssueSummary, this.useCaseLabels, this.scenarioLabels);
            });
            /*
            const stepIdentifier = result.stepIdentifier;
            const fallback = result.fallback;
            const step = result.step;
            const pageTree = transformMetadataToTree(result.step.page);
            const stepIndex = result.stepNavigation.stepIndex;
            const useCaseLabels = result.useCaseLabels;
            const scenarioLabels = result.scenarioLabels;
            const selectedBuild = selected.buildName;
            const getCurrentStepIndexForDisplay = getCurrentStepIndexForDisplay;*/
        }, (error) => {
            this.stepNotFound = true;
        });
    }

    private getCurrentStepIndexForDisplay() {
        if (angular.isUndefined(this.stepNavigation)) {
            return '?';
        }
        return this.stepNavigation.stepIndex + 1;
    }

    private createInformationTreeArray(stepInformationTree, relatedIssues, useCaseLabels, scenarioLabels) {
        this.mainDetailsSections = [
            {
                name: 'Step',
                key: 'step',
                dataTree: this.createStepInformationTree(stepInformationTree),
                isFirstOpen: true,
                detailSectionType: 'treeComponent',
            },
            {
                name: 'Labels',
                key: 'labels',
                dataTree: {nodeLabel: 'label', childNodes: [this.createLabelInformationTree(stepInformationTree, useCaseLabels, scenarioLabels)]},
                isFirstOpen: false,
                detailSectionType: 'treeComponent',
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

    private createStepInformationTree(step) {
        const stepInformationTree: any = {};
        const stepDescription = step.stepDescription;

        if (stepDescription.title) {
            stepInformationTree['Step title'] = stepDescription.title;
        }

        if (step.page) {
            const pageToRender = Object.assign({}, step.page);
            // Will be displayed separately
            delete pageToRender.labels;
            stepInformationTree['Page name'] = pageToRender;
        }

        if (stepDescription.details) {
            Object.keys(stepDescription.details).forEach((key) => {
                const value = stepDescription.details[key];
                stepInformationTree[key] = value;
            });
        }

        if (stepDescription.status) {
            stepInformationTree['Build status'] = stepDescription.status;
        }

        return this.metadataTreeCreatorPipe.transform(stepInformationTree);
    }

    private createLabelInformationTree(step, useCaseLabels, scenarioLabels) {
        const labelInformationTree: any = {};

        labelInformationTree['Use case:'] = useCaseLabels.labels;
        labelInformationTree['Scenario:'] = scenarioLabels.labels;
        labelInformationTree['Step:'] = step.stepDescription.labels.labels;
        labelInformationTree['Page:'] = step.page.labels.labels;

        return this.metadataTreeCreatorPipe.transform(labelInformationTree);
    }
}

angular.module('scenarioo.directives')
    .directive('scStepView',
        downgradeComponent({component: StepViewComponent}) as angular.IDirectiveFactory);
