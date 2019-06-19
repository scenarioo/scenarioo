import {Component, OnInit} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';
import {ConfigurationService} from '../../services/configuration.service';
import {IConfiguration, ICustomObjectTab} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;


@Component({
    selector: 'sc-usecase-overview',
    template: require('./usecase-overview.component.html'),
    styles: [require('./usecase-overview.component.css').toString()],
})
export class UsecaseOverviewComponent implements OnInit {
    tabs: any[] = [
        {title: 'Pages', content: 'test'},
        {title: 'Sketcher', content: 'sketchertest'}
    ];

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
    .directive('scUsecaseOverview',
        downgradeComponent({component: UsecaseOverviewComponent}) as angular.IDirectiveFactory);
