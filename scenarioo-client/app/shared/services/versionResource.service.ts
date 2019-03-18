import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';
import {Observable} from 'rxjs';

declare var angular: angular.IAngularStatic;

export interface Version {
    aggregatedDataFormatVersion: string;
    apiVersion: string;
    buildDate: string;
    documentationVersion: string;
    version: string;
}

@Injectable()
export class VersionResource {
    constructor(private httpClient: HttpClient) {

    }

    get(): Observable<Version> {
        return this.httpClient.get<Version>('rest/version');
    }
}

angular.module('scenarioo.services')
    .factory('VersionResource', downgradeInjectable(VersionResource));
