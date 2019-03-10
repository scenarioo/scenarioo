import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

export interface BuildInfo {
    branchName: string;
    buildName: string;
}

@Injectable()
export class ComparisonCreateResource {

    constructor(private httpClient: HttpClient) {
    }

    createComparision(comparisonName: string, baseBranch: BuildInfo, compareBranch: BuildInfo): Promise<void> {
        return this.httpClient
            .post<void>(`rest/builds/${baseBranch.branchName}/${baseBranch.buildName}/comparisons/${comparisonName}/calculate`, compareBranch)
            .toPromise<void>();
    }
}

angular.module('scenarioo.services')
    .factory('ComparisonCreateResource', downgradeInjectable(ComparisonCreateResource));
