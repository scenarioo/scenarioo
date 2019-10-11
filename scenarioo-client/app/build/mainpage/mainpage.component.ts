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
import {IConfiguration, ICustomObjectDetailColumn, ICustomObjectTab} from '../../generated-types/backend-types';
import {LocationService} from '../../shared/location.service';
import {TabDirective, TabsetComponent} from 'ngx-bootstrap';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-mainpage',
    template: require('./mainpage.component.html'),
    styles: [require('./mainpage.component.css').toString()],
})
export class MainpageComponent implements OnInit {

    @ViewChild('homeTabs') tabsetComponent: TabsetComponent;

    tabs: ITab[] = undefined;

    constructor(private configurationService: ConfigurationService,
                private locationService: LocationService) {
    }

    ngOnInit(): void {

        this.configurationService.getConfiguration().subscribe((configuration: IConfiguration) => {
            this.tabs = this.createTabs(configuration);

            // TODO: We should find a better solution for this.
            // The problem is, that the customTabs which are added to allTabs are not available at this time, waiting for a bit solves this.
            setTimeout(() => this.setActiveTab(this.getSelectedTabFromUrl()), 100);
        });
    }

    private setActiveTab(tabHeading: string) {
        this.tabsetComponent.tabs.filter((tab) => tab.heading === tabHeading)[0].active = true;
    }

    private onSelect(data: TabDirective): void {
        const activeTab = this.tabs.filter((tab) => tab.title === data.heading)[0];
        this.locationService.search('tab', activeTab.id);
    }

    isActiveTab(tab: ITab): boolean {
        return this.tabsetComponent && this.tabsetComponent.tabs.find((tabComp) => tabComp.heading === tab.title).active;
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

    private createTabs(configuration: IConfiguration): ITab[] {
        const tabs = [];
        tabs.push({
            id: 'useCases',
            title: 'Use Cases',
        });
        configuration.customObjectTabs
            .forEach((customObjectTab: ICustomObjectTab, index) => {
                tabs.push({
                    title: customObjectTab.tabTitle,
                    id: customObjectTab.id,
                    isCustom: true,
                    columns: customObjectTab.customObjectDetailColumns,
                });
            });
        tabs.push({
            id: 'sketches',
            title: 'Sketches',
        });
        return tabs;
    }

}

export interface ITab {

    id: string;

    title: string;

    isCustom?: boolean;

    columns?: ICustomObjectDetailColumn;

}

angular.module('scenarioo.directives')
    .directive('scMainpage',
        downgradeComponent({component: MainpageComponent}) as angular.IDirectiveFactory);
