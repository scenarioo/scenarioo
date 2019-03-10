import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonRecalculateResource {

    constructor(private httpClient: HttpClient) {
    }

    recalculate(comparisonName: string, branchInfo: BuildInfo): Promise<void> {

        return this.httpClient
            .post<void>(`rest/builds/${branchInfo.branchName}/${branchInfo.buildName}/comparisons/${comparisonName}/recalculate`,
                {
                    responseType: 'text',
                })
            .toPromise();

    }
}

angular.module('scenarioo.services')
    .factory('ComparisonRecalculateResource', downgradeInjectable(ComparisonRecalculateResource));
