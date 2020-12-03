import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Observable} from 'rxjs';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonLogResource {

    constructor(private httpClient: HttpClient) {
    }

    logComparision(comparisonName: string, branchInfo: BuildInfo): Observable<string> {
        const encodedBranch = encodeURIComponent(branchInfo.branchName);
        const encodedBuild = encodeURIComponent(branchInfo.buildName);
        const encodedComparison = encodeURIComponent(comparisonName);
        return this.httpClient
            .get(`rest/builds/${encodedBranch}/${encodedBuild}/comparisons/${encodedComparison}/log`,
                {
                    responseType: 'text',
                });

    }
}

angular.module('scenarioo.services')
    .factory('ComparisonLogResource', downgradeInjectable(ComparisonLogResource));
