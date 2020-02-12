import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';
import {Observable, ReplaySubject} from 'rxjs';

declare var angular: angular.IAngularStatic;

@Injectable()
export class SharePageService {

    private readonly pageUrl: ReplaySubject<string>;
    private readonly imageUrl: ReplaySubject<string>;

    constructor() {
        this.pageUrl = new ReplaySubject(1);
        this.imageUrl = new ReplaySubject(1);
        this.invalidateUrls();
    }

    setPageUrl(pageUrl: string) {
        this.pageUrl.next(pageUrl);
    }

    setImageUrl(imageUrl: string) {
        this.imageUrl.next(imageUrl);
    }

    getPageUrl(): Observable<string> {
        return this.pageUrl;
    }

    getImageUrl(): Observable<string> {
        return this.imageUrl;
    }

    invalidateUrls() {
        this.pageUrl.next(undefined);
        this.imageUrl.next(undefined);
    }
}

angular.module('scenarioo.services').factory('SharePageService', downgradeInjectable(SharePageService));
