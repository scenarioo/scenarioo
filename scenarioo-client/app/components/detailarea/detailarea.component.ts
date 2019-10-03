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

import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ICustomObjectTabTree, ILabelConfiguration, IUseCaseSummary} from '../../generated-types/backend-types';
import {RelatedIssueSummary} from '../../shared/services/relatedIssueResource.service';
import {LocalStorageService} from '../../services/localStorage.service';

const COLLAPSED_STATE_KEY_PREFIX = 'scenarioo-metadataVisible-';

@Component({
    selector: 'sc-detailarea',
    template: require('./detailarea.component.html'),
    styles: [require('./detailarea.component.css').toString()],
})
export class DetailareaComponent {

    /**
     * A key to define under which key the detail area stores its state (like what is/was collapsed or expanded)
     */
    @Input()
    key: string;

    @Input()
    branchInformationTree: any;

    @Input()
    buildInformationTree: any;

    @Input()
    usecaseInformationTree: any;

    @Input()
    metadataInformationTree: ICustomObjectTabTree;

    @Input()
    relatedIssues: RelatedIssueSummary[];

    @Input()
    useCaseLabels: string[];

    @Input()
    labelConfigurations: ILabelConfiguration;

    @Output('togglePannelCollapsedValue')
    panelCollapsed: EventEmitter<boolean> = new EventEmitter<boolean>();

    isPanelCollapsed: boolean = true;

    constructor(private localStorageService: LocalStorageService) {

    }

    ngOnInit(): void {
        this.isPanelCollapsed = this.localStorageService.getBoolean(this.getLocalStorageKey(), false);
    }

    togglePannelCollapsedValue() {
        this.isPanelCollapsed = !this.isPanelCollapsed;
        this.localStorageService.setBoolean(this.getLocalStorageKey(), this.isPanelCollapsed);
        this.panelCollapsed.emit(this.isPanelCollapsed);
    }

    getLocalStorageKey() {
        return COLLAPSED_STATE_KEY_PREFIX + this.key;
    }

    isEmptyObject(obj) {
        return (obj && (Object.keys(obj).length === 0));
    }
}
