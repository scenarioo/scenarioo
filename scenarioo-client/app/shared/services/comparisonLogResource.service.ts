import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Observable} from "rxjs";

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonLogResource {

    constructor(private httpClient: HttpClient) {
    }

    logComparision(comparisonName: string, branchInfo: BuildInfo): Observable<string> {

        return this.httpClient
            .get(`rest/builds/${branchInfo.branchName}/${branchInfo.buildName}/comparisons/${comparisonName}/log`,
                {
                    responseType: 'text',
                });

    }
}

angular.module('scenarioo.services')
    .factory('ComparisonLogResource', downgradeInjectable(ComparisonLogResource));
