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

@Component({
    selector: 'sc-detail-accordion',
    template: require('./detail-accordion.component.html'),
    styles: [require('./detail-accordion.component.css').toString()],
})

export class DetailAccordionComponent {

    isAccordionCollapsed: boolean = false;

    @Input()
    isFirstOpen: boolean;

    @Input()
    tree: boolean;

    @Input()
    sketches: boolean;

    @Input()
    label: boolean;

    @Input()
    metadataInformationTree: boolean;

    @Input()
    detailAccordionName: {};

    @Input()
    informationTree: any;

    @Input()
    metadataTree: boolean;

    @Input()
    relatedIssues;

    @Input()
    labelConfigurations: {};

    selectedBranch: any;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private locationService: LocationService,
                private sketchIdsResource: SketchIdsResource) {
    }

    ngOnInit(): void {
        this.selectedBranch = this.selectedBranchAndBuildService.selected();

        if (this.isFirstOpen === false) {
            this.isAccordionCollapsed = true;
        }
    }

    goToIssue(issue) {
        this.selectedBranch = this.selectedBranch.branch;
        this.sketchIdsResource.get(
            this.selectedBranch,
            issue.id,
        ).subscribe((result) => {
            const params = this.locationService.path('/stepsketch/' + issue.id + '/' + result.scenarioSketchId + '/' + result.stepSketchId);
        });
    }
}
