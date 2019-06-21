import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {TabDirective, TabsetComponent} from 'ngx-bootstrap';
import {LocationService} from '../../shared/location.service';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-manage-tabs',
    template: require('./manage-tabs.component.html'),
    styles: [require('./manage-tabs.component.css').toString()],
})
export class ManageTabsComponent implements OnInit {
    @ViewChild('manageTabs') staticTabs: TabsetComponent;

    tabs: ScTabMapping[] = [
        {name: 'Builds', urlParam: 'builds'},
        {name: 'Comparisons', urlParam: 'comparisons'},
        {name: 'Branch Aliases', urlParam: 'branchAliases'},
        {name: 'Label Colors', urlParam: 'labelConfigurations'},
        {name: 'General Settings', urlParam: 'configuration'},
    ];

    constructor(private locationService: LocationService) {
    }

    ngOnInit(): void {
        const tabId = this.tabs.map((tab) => tab.urlParam).indexOf(this.getSelectedTabFromUrl());
        if (tabId !== -1) {
            this.staticTabs.tabs[tabId].active = true;
        }
    }

    isActiveTab(tabId: number): boolean {
        return this.staticTabs.tabs[tabId].active;
    }

    onSelect(data: TabDirective): void {
        if (data.heading) {
            const urlParam = this.tabs.filter((tab) => tab.name === data.heading)[0].urlParam;
            this.updateUrlWithTabParam(urlParam);
        }
    }

    private getSelectedTabFromUrl(): string | undefined {
        const params = this.locationService.search();
        if (params.tab) {
            return params.tab;
        }
        return undefined;
    }

    private updateUrlWithTabParam(tab: string) {
        if (this.locationService.search('tab') !== tab) {
            this.locationService.search('tab', tab);
        }
    }
}

interface ScTabMapping {
    name: string;
    urlParam: string;
}

angular.module('scenarioo.directives')
    .directive('scManageTabs',
        downgradeComponent({component: ManageTabsComponent}) as angular.IDirectiveFactory);
