import {Component, OnInit, TemplateRef} from '@angular/core';
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
                private sharePageURL: SharePageURL ) {
    }

    ngOnInit(): void {

        this.currentBrowserLocation = window.location.href;

        this.configurationService.getConfiguration().subscribe((configuration: IConfiguration) => {
            this.tabs = configuration.customObjectTabs
                .map((customObjectTab: ICustomObjectTab) => {
                    return {title: customObjectTab.tabTitle, content: 'tbd'};
                });
            this.defineLastStaticTabs();
        });

        this.pageUrl = this.getPageUrl();

        this.imageUrl = this.sharePageURL.getImageUrl();

        this.eMailSubject = encodeURIComponent('Link to Scenarioo');
        this.eMailUrl = encodeURIComponent(this.pageUrl);
    }

    private getPageUrl() {
        if (this.sharePageURL.getPageUrl() !== undefined) {
            return this.sharePageURL.getPageUrl();
        } else {
            return this.currentBrowserLocation;
        }
    }

    defineLastStaticTabs() {
        const i = this.tabs.length;
        this.tabs.push({
            index: i,
            tabId: 'sketches',
            title: 'Sketches',
            contentViewUrl: 'build/sketchesTab.html',
        });
    }

    openShare(shareContent: TemplateRef<any>) {
        this.modalRef = this.modalService.show(shareContent);
    }
}

angular.module('scenarioo.directives')
    .directive('scMainpage',
        downgradeComponent({component: MainPageComponent}) as angular.IDirectiveFactory);
