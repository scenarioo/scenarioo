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

    stepNotFound: boolean = false;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private routeParams: RouteParamsService,
                private stepResource: StepResource) {
    }

    ngOnInit(): void {
        this.useCaseName = this.routeParams.useCaseName;
        this.scenarioName = this.routeParams.scenarioName;

        this.pageName = this.routeParams.pageName;
        this.pageOccurrence = parseInt(this.routeParams.pageOccurrence, 10);
        this.stepInPageOccurrence = parseInt(this.routeParams.stepInPageOccurrence, 10);

        this.labels = null;
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
            /*const stepIdentifier = result.stepIdentifier;
            const fallback = result.fallback;
            const step = result.step;
            const metadataTree = transformMetadataToTreeArray(result.step.metadata.details);
            const stepInformationTree = createStepInformationTree(result.step);
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
}

angular.module('scenarioo.directives')
    .directive('scStepView',
        downgradeComponent({component: StepViewComponent}) as angular.IDirectiveFactory);
