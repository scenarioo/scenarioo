import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Observable} from 'rxjs';
import {Url} from '../utils/url';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonLogResource {

    constructor(private httpClient: HttpClient) {
    }

    logComparision(comparisonName: string, branchInfo: BuildInfo): Observable<string> {
        const url = Url.encodeComponents`rest/builds/${branchInfo.branchName}/${branchInfo.buildName}/comparisons/${comparisonName}/log`;
        return this.httpClient
            .get(url,
                {
                    responseType: 'text',
                });

    }
}

angular.module('scenarioo.services')
    .factory('ComparisonLogResource', downgradeInjectable(ComparisonLogResource));
