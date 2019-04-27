import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

export interface Comparison {
    changeRate: number;
    name: string;
    added: number;
    changed: number;
    removed: number;
    addedElements: any[];
    removedElements: any[];
    baseBuild: BuildInfo;
    compareBuild: BuildInfo;
    status: 'QUEUED_FOR_PROCESSING' | 'PROCESSING' | 'SKIPPED' | 'SUCCESS' | 'FAILED';
    calculationDate: string;
    baseBuildDate: string;
}

@Injectable()
export class ComparisonsResource {
    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<Comparison[]> {
        return this.httpClient.get<Comparison[]>('rest/comparisons');
    }
}

angular.module('scenarioo.services')
    .factory('ComparisonsResource', downgradeInjectable(ComparisonsResource));
