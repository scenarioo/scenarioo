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
import {ISketchIds} from '../../../generated-types/backend-types';
import {LocalStorageService} from '../../../services/localStorage.service';
import {IDetailsTreeNode} from '../IDetailsTreeNode';

const LOCALSTORAGE_KEY_PREFIX_SECTION_EXPANDED = 'scenarioo-panelExpanded-';

@Component({
    selector: 'sc-detail-accordion',
    template: require('./detail-accordion.component.html'),
    styles: [require('./detail-accordion.component.css').toString()],
})
export class DetailAccordionComponent {

    /**
     * A key to define under which key the accordion stores its state (like what is/was collapsed or expanded)
     */
    @Input()
    key: string;

    @Input()
    isFirstOpen: boolean;

    @Input()
    detailSectionType: string;

    @Input()
    detailAccordionName: string;

    @Input()
    dataTree: IDetailsTreeNode;

    /**
     * Only for special section types like issues
     */
    @Input()
    values: any[];

    isAccordionCollapsed: boolean = false;

    constructor(private selectedBranchAndBuildService: SelectedBranchAndBuildService,
                private locationService: LocationService,
                private sketchIdsResource: SketchIdsResource,
                private localStorageService: LocalStorageService) {
    }

    ngOnInit(): void {
        this.isAccordionCollapsed = this.localStorageService.getBoolean(this.getLocalStorageKey(), !this.isFirstOpen);
    }

    toggleAccordionCollapsedValue() {
        this.isAccordionCollapsed = !this.isAccordionCollapsed;
        this.localStorageService.setBoolean(this.getLocalStorageKey(), this.isAccordionCollapsed);
    }

    getLocalStorageKey() {
        return LOCALSTORAGE_KEY_PREFIX_SECTION_EXPANDED + this.key;
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
