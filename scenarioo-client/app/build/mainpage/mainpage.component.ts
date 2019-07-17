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

import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {ConfigurationService} from '../../services/configuration.service';
import {IConfiguration, ICustomObjectTab} from '../../generated-types/backend-types';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {SharePageURL} from '../../shared/navigation/sharePage/sharePageUrl.service';
import {LocationService} from '../../shared/location.service';
import {TabDirective, TabsetComponent} from 'ngx-bootstrap';
import {Location} from '@angular/common';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-mainpage',
    template: require('./mainpage.component.html'),
    styles: [require('./mainpage.component.css').toString()],
})
export class MainPageComponent implements OnInit {
    @ViewChild('homeTabs') allTabs: TabsetComponent;

    customTabs: any[] = [];
    tabs: any[];
    modalRef: BsModalRef;

    eMailSubject: string = undefined;
    eMailUrl: string;
    pageUrl: string;

    imageUrl: string;

    currentBrowserLocation: string;

    constructor(private configurationService: ConfigurationService,
                private modalService: BsModalService,
                private sharePageURL: SharePageURL,
                private locationService: LocationService,
                private location: Location) {
    }

    ngOnInit(): void {

        // TODO: Workaround until the full migration, when the Angular router is available
        this.currentBrowserLocation = this.location.prepareExternalUrl(this.location.path());

        this.configurationService.getConfiguration().subscribe((configuration: IConfiguration) => {
            this.customTabs = configuration.customObjectTabs
                .map((customObjectTab: ICustomObjectTab, index) => {
                    return {
                        title: customObjectTab.tabTitle,
                        id: customObjectTab.id,
                        columns: customObjectTab.customObjectDetailColumns,
                        index: index + 1,
                    };
                });
            this.tabs = configuration.customObjectTabs
                .map((customObjectTab: ICustomObjectTab) => {
                    return {
                        title: customObjectTab.tabTitle,
                        id: customObjectTab.id,
                    };
                });
            this.defineStaticTabs();
            // TODO: We should find a better solution for this.
            // The problem is, that the customTabs which are added to allTabs are not available at this time, waiting for a bit solves this.
            setTimeout(() => this.setActiveTab(this.getSelectedTabFromUrl()), 0);
        });
        this.pageUrl = this.getPageUrl();

        this.imageUrl = this.sharePageURL.getImageUrl();

        this.eMailSubject = encodeURIComponent('Link to Scenarioo');

        this.eMailUrl = encodeURIComponent(this.pageUrl);
    }

    private setActiveTab(tabHeading: string) {
        this.allTabs.tabs.filter((tab) => tab.heading === tabHeading)[0].active = true;
    }

    private onSelect(data: TabDirective): void {
        const activeTab = this.tabs.filter((tab) => tab.title === data.heading)[0];
        this.locationService.search('tab', activeTab.id);
    }

    isActiveTab(tabId: string): boolean {
        return this.allTabs.tabs.find((tab) => tab.heading === tabId).active;
    }

    private getSelectedTabFromUrl(): string {
        const params = this.locationService.search();
        let selectedTab;
        if (params.tab) {
            selectedTab = params.tab;
        } else {
            selectedTab = 'useCases';
        }
        return this.tabs.filter((tab) => tab.id === selectedTab)[0].title;
    }

    private getPageUrl() {
        if (this.sharePageURL.getPageUrl() !== undefined) {
            return this.sharePageURL.getPageUrl();
        } else {
            return this.currentBrowserLocation;
        }
    }

    private defineStaticTabs() {
        this.tabs.push({
            id: 'useCases',
            title: 'Use Cases',
        });
        this.tabs.push({
            id: 'sketches',
            title: 'Sketches',
        });
    }

    openShare(shareContent: TemplateRef<any>) {
        this.modalRef = this.modalService.show(shareContent);
    }
}

angular.module('scenarioo.directives')
    .directive('scMainpage',
        downgradeComponent({component: MainPageComponent}) as angular.IDirectiveFactory);
