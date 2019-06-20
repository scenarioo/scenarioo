import {Component, OnInit} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {ConfigurationService} from '../../services/configuration.service';
import {IConfiguration, ICustomObjectTab} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;


@Component({
    selector: 'sc-mainpage',
    template: require('./mainpage.component.html'),
    styles: [require('./mainpage.component.css').toString()],
})
export class MainPageComponent implements OnInit {
    tabs: any[]

    constructor(private configurationService: ConfigurationService) {

    }

    ngOnInit(): void {
        this.configurationService.getConfiguration().subscribe((configuration: IConfiguration) => {
            this.tabs = configuration.customObjectTabs
                .map((customObjectTab: ICustomObjectTab) => {
                    return {title: customObjectTab.tabTitle, content: 'tbd'}
                });
        });

    }


}

angular.module('scenarioo.directives')
    .directive('scMainpage',
        downgradeComponent({component: MainPageComponent}) as angular.IDirectiveFactory);
