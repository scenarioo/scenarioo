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

import {Component, ElementRef, HostListener, Input, ViewChild} from '@angular/core';
import {LocalStorageService} from '../../services/localStorage.service';
import {IMainDetailsSection} from './IMainDetailsSection';
import {IDetailsSections} from './IDetailsSections';

const LOCALSTORAGE_KEY_PREFIX_DETAILS_VISIBLE = 'scenarioo-metadataVisible-';

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
    mainDetailsSections: IMainDetailsSection[];

    /**
     * Additional generic details sections, usually derived from an object's details field,
     * by using the MetadataTreeListCreatorPipe.
     */
    @Input()
    additionalDetailsSections: IDetailsSections;

    /**
     * The element of the collapsable details panel (that can be opened and closed).
     */
    @ViewChild('detailsPanel')
    detailsPanel: ElementRef;

    isPanelCollapsed: boolean = true;

    constructor(private localStorageService: LocalStorageService) {

    }

    ngOnInit(): void {
        this.isPanelCollapsed = this.localStorageService.getBoolean(this.getLocalStorageKey(), false);
    }

    ngAfterViewInit(): void {
        this.setHeightOfDetailarea();
    }

    @HostListener('window:resize', ['$event'])
    onResize() {
        this.setHeightOfDetailarea();
    }

    private setHeightOfDetailarea() {
        const detailsPanel = this.detailsPanel.nativeElement;
        const headerHeight = detailsPanel.offsetTop + 2;

        // let the details area expand to minimum of rest of visible area!
        // (but still let it expand to full page size if it is even higher)
        detailsPanel.style.minHeight = 'calc(100vh - ' + headerHeight + 'px)';
    }

    togglePannelCollapsedValue() {
        this.isPanelCollapsed = !this.isPanelCollapsed;
        this.localStorageService.setBoolean(this.getLocalStorageKey(), this.isPanelCollapsed);
    }

    getLocalStorageKey() {
        return LOCALSTORAGE_KEY_PREFIX_DETAILS_VISIBLE + this.key;
    }

    isEmptyObject(obj) {
        return obj && Object.keys(obj).length === 0;
    }
}
