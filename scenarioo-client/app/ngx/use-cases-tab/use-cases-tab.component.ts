import {Component} from '@angular/core';
import * as angular from 'angular';
import {downgradeComponent} from '@angular/upgrade/static';
import {Input} from '@angular/compiler/src/core';

@Component({
    selector: 'sc-use-cases-tab',
    template: require('./use-cases-tab.component.html'),
    styles: [require('./use-cases-tab.component.css').toString()]
})
export class UseCasesTabComponent {



    // TODO hotkeys
}

angular.module('scenarioo.directives')
    .directive('scUseCasesTab',
        downgradeComponent({component: UseCasesTabComponent}) as angular.IDirectiveFactory);
