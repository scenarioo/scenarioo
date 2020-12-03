import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Observable} from 'rxjs';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonRecalculateResource {

    constructor(private httpClient: HttpClient) {
    }

    recalculate(comparisonName: string, branchInfo: BuildInfo): Observable<void> {
        const encodedBranch = encodeURIComponent(branchInfo.branchName);
        const encodedBuild = encodeURIComponent(branchInfo.buildName);
        const encodedComparison = encodeURIComponent(comparisonName);

        return this.httpClient
            .post<void>(`rest/builds/${encodedBranch}/${encodedBuild}/comparisons/${encodedComparison}/recalculate`,
                {
                    responseType: 'text',
                });
    }
}

angular.module('scenarioo.services')
    .factory('ComparisonRecalculateResource', downgradeInjectable(ComparisonRecalculateResource));
