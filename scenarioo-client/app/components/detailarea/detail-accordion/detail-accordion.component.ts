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

import {Component, Input} from '@angular/core';
import {LocationService} from '../../../shared/location.service';
import {SelectedBranchAndBuildService} from '../../../shared/navigation/selectedBranchAndBuild.service';
import {SketchIdsResource} from '../../../shared/services/sketchIdsResource.service';
import {RelatedIssueSummary} from '../../../shared/services/relatedIssueResource.service';
import {LabelConfigurationMap} from '../../../shared/services/labelConfigurationsResource.service';
import {ISketchIds} from '../../../generated-types/backend-types';
import {LocalStorageService} from '../../../services/localStorage.service';

const MAIN_METADATA_SECTION_EXPANDED = 'scenarioo-panelExpanded-';

@Component({
    selector: 'sc-detail-accordion',
    template: require('./detail-accordion.component.html'),
    styles: [require('./detail-accordion.component.css').toString()],
})
export class DetailAccordionComponent {

    @Input()
    key: string;

    @Input()
    isFirstOpen: boolean;

    @Input()
    isTreeComponent: boolean;

    @Input()
    isSketchesComponent: boolean;

    @Input()
    isLabelComponent: boolean;

    @Input()
    detailAccordionName: string;

    @Input()
    informationTree: any;

    @Input()
    relatedIssues: RelatedIssueSummary;

    @Input()
    labelConfigurations: LabelConfigurationMap;

    isAccordionCollapsed: boolean = false;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private locationService: LocationService,
                private sketchIdsResource: SketchIdsResource,
                private localStorageService: LocalStorageService) {
    }

    ngOnInit(): void {
        this.isAccordionCollapsed = this.localStorageService.getBoolean(MAIN_METADATA_SECTION_EXPANDED + this.key, this.isFirstOpen);
        console.log(this.key, this.isAccordionCollapsed);
    }

    toggleAccordionCollapsedValue() {
        this.isAccordionCollapsed = this.isAccordionCollapsed !== true;
        console.log(this.key, this.isAccordionCollapsed);
        this.localStorageService.setBoolean(MAIN_METADATA_SECTION_EXPANDED + this.key, this.isAccordionCollapsed);
    }

    goToIssue(issue: RelatedIssueSummary) {
        this.selectedBranchAndBuildService.callOnSelectionChange((selection) => {
            this.sketchIdsResource.get(
                selection.branch,
                issue.id,
            ).subscribe((result: ISketchIds) => {
                this.locationService.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
            });
        });
    }
}
