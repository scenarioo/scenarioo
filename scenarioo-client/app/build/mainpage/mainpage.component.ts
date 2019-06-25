import {Component, OnInit, TemplateRef} from '@angular/core';
//import {Location} from '@angular/common';
import {downgradeComponent} from '@angular/upgrade/static';
import {ConfigurationService} from '../../services/configuration.service';
import {IConfiguration, ICustomObjectTab} from '../../generated-types/backend-types';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';

declare var angular: angular.IAngularStatic;


@Component({
    selector: 'sc-mainpage',
    template: require('./mainpage.component.html'),
    styles: [require('./mainpage.component.css').toString()],
})
export class MainPageComponent implements OnInit {

    tabs: any[];
    modalRef: BsModalRef;

    eMailSubject = undefined;
    eMailUrl: string;
    pageUrl: string = 'test';
    imageUrl: string;

    constructor(private configurationService: ConfigurationService,
                private modalService: BsModalService,) {

    }

    ngOnInit(): void {
        this.configurationService.getConfiguration().subscribe((configuration: IConfiguration) => {
            this.tabs = configuration.customObjectTabs
                .map((customObjectTab: ICustomObjectTab) => {
                    return {title: customObjectTab.tabTitle, content: 'tbd'}
                });
        });

        //this.pageUrl = this.location.path();

        /*
        this.pageUrl = (function () {
            if (angular.isDefined(SharePageService.getPageUrl())) {
                return SharePageService.getPageUrl();
            } else {
                return currentBrowserLocation;
            }
        }());

        this.imageUrl = SharePageService.getImageUrl();
        */

        this.eMailSubject = encodeURIComponent('Link to Scenarioo');
        this.eMailUrl = encodeURIComponent(this.pageUrl);
    }

    openShare(shareContent: TemplateRef<any>) {
        this.modalRef = this.modalService.show(shareContent);
    }
}

angular.module('scenarioo.directives')
    .directive('scMainpage',
        downgradeComponent({component: MainPageComponent}) as angular.IDirectiveFactory);
