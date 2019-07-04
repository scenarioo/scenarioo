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

    setPageUrl(pageUrl) {
        this.shareLinks.pageUrl = pageUrl;
    }

    setImageUrl(imageUrl) {
        this.shareLinks.imageUrl = imageUrl;
    }

    getPageUrl() {
        return this.shareLinks.pageUrl;
    }

    getImageUrl() {
        return this.shareLinks.imageUrl;
    }

    invalidateUrls() {
        this.shareLinks.pageUrl = undefined;
        this.shareLinks.imageUrl = undefined;
    }
}

angular.module('scenarioo.services')
    .factory('SharePageURL', downgradeInjectable(SharePageURL));
