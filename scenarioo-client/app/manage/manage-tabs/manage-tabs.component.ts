import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {TabDirective, TabsetComponent} from 'ngx-bootstrap';

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

    @Output()
    onSelectTab: EventEmitter<string> = new EventEmitter();

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
        const tabId = this.tabs
            .map((tab) => tab.urlParam)
            .indexOf(this.selectedTab);
        if (tabId !== -1) {
            this.staticTabs.tabs[tabId].active = true;
        }
    }

    isActiveTab(tabId: number): boolean {
        return this.staticTabs.tabs[tabId].active;
    }

    onSelect(data: TabDirective): void {
        if (data.heading) {
            const urlParam = this.tabs
                .filter((tab) => tab.name === data.heading)[0].urlParam;
            this.onSelectTab.emit(urlParam);
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
