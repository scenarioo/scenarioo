import {Component, OnInit, TemplateRef} from '@angular/core';
import {Location} from '@angular/common';
import {downgradeComponent} from '@angular/upgrade/static';
import {ConfigurationService} from '../../services/configuration.service';
import {IConfiguration, ICustomObjectTab} from '../../generated-types/backend-types';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {SharePageURL} from '../../shared/navigation/sharePage/sharePageUrl.service';

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
    pageUrl: string;
    imageUrl: string;

    currentBrowserLocation: string;

    constructor(private configurationService: ConfigurationService,
                private modalService: BsModalService,
                private sharePageUrl: SharePageURL,
                private location: Location,
                ) {
    }

    ngOnInit(): void {

        this.currentBrowserLocation = this.location.path();

        this.configurationService.getConfiguration().subscribe((configuration: IConfiguration) => {
            this.tabs = configuration.customObjectTabs
                .map((customObjectTab: ICustomObjectTab) => {
                    return {title: customObjectTab.tabTitle, content: 'tbd'}
                });
        });

        this.pageUrl = (function () {
            if (this.sharePageURL.getPageUrl() !== undefined) {
                return this.sharePageURL.getPageUrl();
            } else {
                return this.currentBrowserLocation;
            }
        }());

        this.imageUrl = this.sharePageUrl.getImageUrl();

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
