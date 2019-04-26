import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Observable} from "rxjs";

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonRecalculateResource {

    constructor(private httpClient: HttpClient) {
    }

    recalculate(comparisonName: string, branchInfo: BuildInfo): Observable<void> {

        return this.httpClient
            .post<void>(`rest/builds/${branchInfo.branchName}/${branchInfo.buildName}/comparisons/${comparisonName}/recalculate`,
                {
                    responseType: 'text',
                });
    }
}

angular.module('scenarioo.services')
    .factory('ComparisonRecalculateResource', downgradeInjectable(ComparisonRecalculateResource));
