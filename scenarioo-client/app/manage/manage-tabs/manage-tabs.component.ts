import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {TabsetComponent} from 'ngx-bootstrap';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-manage-tabs',
    template: require('./manage-tabs.component.html'),
    styles: [require('./manage-tabs.component.css').toString()],
})
export class ManageTabsComponent implements OnInit {
    @ViewChild('manageTabs') staticTabs: TabsetComponent;

    @Input()
    selectedTab: string;

    tabs: ScTabMapping[] = [
        {name: 'Builds', urlParam: 'builds'},
        {name: 'Comparisons', urlParam: 'comparisons'},
        {name: 'Branch Aliases', urlParam: 'branchAliases'},
        {name: 'Label Colors', urlParam: 'labelConfigurations'},
        {name: 'General Settings', urlParam: 'configuration'},
    ];

    constructor() {
    }

    ngOnInit(): void {
        const tabId = this.tabs.map((tab) => tab.urlParam).indexOf(this.selectedTab);
        if (tabId !== -1) {
            this.staticTabs.tabs[tabId].active = true;
        }
    }

    isActiveTab(tabId: number): boolean {
        return this.staticTabs.tabs[tabId].active;
    }

    // put selected tab into the url
}

interface ScTabMapping {
    name: string;
    urlParam: string;
}

angular.module('scenarioo.directives')
    .directive('scManageTabs',
        downgradeComponent({component: ManageTabsComponent}) as angular.IDirectiveFactory);
