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

import {Component, ViewChild} from '@angular/core';
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
import {SelectedComparisonService} from '../../diffViewer/selectedComparison.service';
import {LocalStorageService} from '../../services/localStorage.service';
import {TabsetComponent} from 'ngx-bootstrap';
import {
    INeighborStep,
    IStepDetails,
    IStepIdentifier,
    IStepNavigation, IStepStatistics,
} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

const LOCAL_STORAGE_KEY_ACTIVE_STEP_TAB = 'stepView.activeTab';

@Component({
    selector: 'sc-step-view',
    template: require('./step-view.component.html'),
    styles: [require('./step-view.component.css').toString()],
})
export class StepViewComponent {

    @ViewChild('stepViewTabs')
    tabsetComponent: TabsetComponent;

    // TODO: remove all flat fields and instead rmemeber the whole step data structure (which is now typed :-) )
    // step: IStepDetails;

    useCaseName: string;
    scenarioName: string;
    pageName: string;
    pageOccurrence: number;
    stepInPageOccurrence: number;
    labels: string;

    // step: IStep ? -> diff-Info missing in type IStep
    step = null;
    comparisonInfo;
    stepIdentifier: IStepIdentifier;
    stepNavigation: IStepNavigation;
    stepStatistics: IStepStatistics;
    screenShotUrl: string;

    stepNotFound: boolean = false;

    isComparisonExisting: boolean;

    mainDetailsSections: IMainDetailsSection[] = [];
    additionalDetailsSections: IDetailsSections;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private routeParams: RouteParamsService,
                private stepResource: StepResource,
                private metadataTreeCreatorPipe: MetadataTreeCreatorPipe,
                private metadataTreeListCreatorPipe: MetadataTreeListCreatorPipe,
                private relatedIssueResource: RelatedIssueResource,
                private locationService: LocationService,
                private selectedComparisonService: SelectedComparisonService,
                private localStorageService: LocalStorageService) {
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
        ).subscribe((stepDetails: IStepDetails) => {
            this.step = stepDetails.step;
            this.stepNavigation = stepDetails.stepNavigation;
            this.stepIdentifier = stepDetails.stepIdentifier;
            this.stepStatistics = stepDetails.stepStatistics;

            this.isComparisonExisting = this.selectedComparisonService.isDefined();

            this.initScreenshotUrl(selection);

            this.additionalDetailsSections = this.metadataTreeListCreatorPipe.transform(stepDetails.step.metadata.details);

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
                this.createInformationTreeArray(this.step, relatedIssueSummary, stepDetails.useCaseLabels, stepDetails.scenarioLabels);
            });

            // init active tab (but only after tabs view is initialized)
            setTimeout(() => this.setActiveTab(this.getTabIdToActivate()), 0);

        }, () => {
            // TODO: get rid of this placebo flag -- see idea as TODO in html --> page-loading component
            this.stepNotFound = true;
        });
    }

    public setActiveTab(tabId: string) {
        const tab = this.tabsetComponent.tabs.find((tab) => tab.id === tabId);
        if (tab) {
            this.storeActiveTab(tabId);
            tab.active = true;
        } else {
            console.warn(`Could not find tab with id ${tabId}`);
        }
    }

    private storeActiveTab(activeTabId: string) {
        this.localStorageService.set(LOCAL_STORAGE_KEY_ACTIVE_STEP_TAB, activeTabId);
    }

    private getTabIdToActivate() {

        const defaultSelectedTabId = 'screenshot-tab';
        const tabIdFromLocalStorage = this.localStorageService.get(LOCAL_STORAGE_KEY_ACTIVE_STEP_TAB);

        if (tabIdFromLocalStorage == null) {
            // use default if not set
            return defaultSelectedTabId;
        }

        if (tabIdFromLocalStorage === 'comparison-tab' && !this.isComparisonExisting) {
            // comparison not available --> fallback to default tab
            return defaultSelectedTabId;
        }

        return tabIdFromLocalStorage;
    }

    private getCurrentStepIndexForDisplay() {
        if (angular.isUndefined(this.stepNavigation)) {
            return '?';
        }
        return this.stepNavigation.stepIndex + 1;
    }

    private createInformationTreeArray(step, relatedIssues, useCaseLabels, scenarioLabels) {
        this.mainDetailsSections = [
            {
                name: 'Step',
                key: 'step',
                dataTree: this.createStepInformationTree(step),
                isFirstOpen: true,
                detailSectionType: 'treeComponent',
            },
            {...(useCaseLabels.length > 0)  && {
                name: 'Labels',
                key: 'labels',
                dataTree: {
                    nodeLabel: 'label',
                    childNodes: [this.createLabelInformationTree(step, useCaseLabels, scenarioLabels)],
                },
                isFirstOpen: false,
                detailSectionType: 'treeComponent',
            }},
            {
                name: 'Related Sketches',
                key: 'relatedSketches',
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
            delete pageToRender.diffInfo;
            stepInformationTree['Page name'] = pageToRender;
        }

        if (stepDescription.details) {
            Object.keys(stepDescription.details).forEach((key) => {
                stepInformationTree[key] = stepDescription.details[key];
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

    // This URL is only used internally, not for sharing
    private initScreenshotUrl(selection) {
        if (this.step === undefined) {
            return undefined;
        }

        const imageName = this.step.stepDescription.screenshotFileName;

        if (imageName === undefined) {
            return undefined;
        }

        this.screenShotUrl = 'rest/branch/' + selection.branch + '/build/' + selection.build + '/usecase/' + this.stepIdentifier.usecaseName + '/scenario/' + this.stepIdentifier.scenarioName + '/image/' + imageName;
    }

    private goStepBack(): INeighborStep {
        if (!this.stepNavigation || !this.stepNavigation.previousStep) {
            console.log('goStepBack if');
            return;
        }
        this.goToStepInSameScenario(this.stepNavigation.previousStep);
    }

    private goStepForward(): INeighborStep {
        if (!this.stepNavigation || !this.stepNavigation.nextStep) {
            return;
        }
        this.goToStepInSameScenario(this.stepNavigation.nextStep);
    }

    private goToStepInSameScenario(sameScenarioStepIdentifier: INeighborStep) {
        this.goToStep({
            useCaseName: this.useCaseName,
            scenarioName: this.scenarioName,
            ...sameScenarioStepIdentifier,
        });
    }

    // TODO #838: refactor into a general step navigations ervice, cause not really needed additional complexity
    private goToStep(stepIdentifier: StepIdentifier) {
        this.locationService.path('/step/' + (stepIdentifier.useCaseName) + '/' + (stepIdentifier.scenarioName) + '/' + stepIdentifier.pageName + '/' + stepIdentifier.pageOccurrence + '/' + stepIdentifier.stepInPageOccurrence);
    }

}

/**
 * to be extracted later, if we use it in other places
 */
export interface StepIdentifier {

    useCaseName: string;

    scenarioName: string;

    pageName: string;

    pageOccurrence: number;

    stepInPageOccurrence: number;

}

angular.module('scenarioo.directives')
    .directive('scStepView',
        downgradeComponent({component: StepViewComponent}) as angular.IDirectiveFactory);
