import {Component} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Component({
    selector: 'sc-manage-tabs',
    template: require('./manage-tabs.component.html'),
    styles: [require('./manage-tabs.component.css').toString()],
})
export class ManageTabsComponent {

}

angular.module('scenarioo.directives')
    .directive('scManageTabs',
        downgradeComponent({component: ManageTabsComponent}) as angular.IDirectiveFactory);
