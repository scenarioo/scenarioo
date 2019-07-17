import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class SharePageURL {

    constructor() {
        this.shareLinks = {
            pageUrl : undefined,
            imageUrl : undefined,
        };
    }

    shareLinks: {
        pageUrl: string,
        imageUrl: string,
    };

    setPageUrl(pageUrl: string) {
        this.shareLinks.pageUrl = pageUrl;
    }

    setImageUrl(imageUrl: string) {
        this.shareLinks.imageUrl = imageUrl;
    }

    getPageUrl(): string {
        return this.shareLinks.pageUrl;
    }

    getImageUrl(): string {
        return this.shareLinks.imageUrl;
    }

    invalidateUrls() {
        this.shareLinks.pageUrl = undefined;
        this.shareLinks.imageUrl = undefined;
    }
}

angular.module('scenarioo.services')
    .factory('SharePageURL', downgradeInjectable(SharePageURL));
