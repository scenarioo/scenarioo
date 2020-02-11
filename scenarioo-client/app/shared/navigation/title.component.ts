import * as angular from 'angular';
import {ConfigurationService} from '../../services/configuration.service';
import {Title} from '@angular/platform-browser';
import {Component, Directive} from '@angular/core';
import {downgradeComponent} from '@angular/upgrade/static';

@Component({
    selector: 'app-title',
    template: '',
})
export class AppTitleComponent {

    public constructor(private titleService: Title, private configService: ConfigurationService) {
        configService.applicationName().subscribe((name) => {
            this.titleService.setTitle(`Scenarioo ${name}`);
        });
    }
}

angular.module('scenarioo.directives')
    .directive('scAppTitle',
        downgradeComponent({component: AppTitleComponent}) as angular.IDirectiveFactory);
