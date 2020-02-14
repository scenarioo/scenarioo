import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';
import {Observable, ReplaySubject} from 'rxjs';

declare var angular: angular.IAngularStatic;

@Injectable()
export class SharePageService {

    private pageUrl: string;
    private imageUrl: string;

    constructor() {
        this.invalidateUrls();
    }

    setPageUrl(pageUrl: string) {
        this.pageUrl = pageUrl;
    }

    setImageUrl(imageUrl: string) {
        this.imageUrl = imageUrl;
    }

    getPageUrl(): string {
        return this.pageUrl;
    }

    getImageUrl(): string {
        return this.imageUrl;
    }

    invalidateUrls() {
        this.pageUrl = undefined;
        this.imageUrl =  undefined;
    }
}

angular.module('scenarioo.services').factory('SharePageService', downgradeInjectable(SharePageService));
