import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonLogResource {

    constructor(private httpClient: HttpClient) {
    }

    logComparision(comparisonName: string, branchInfo: BuildInfo): Promise<string> {

        return this.httpClient
            .get(`rest/builds/${branchInfo.branchName}/${branchInfo.buildName}/comparisons/${comparisonName}/log`,
                {
                    responseType: 'text',
                })
            .toPromise();

    }
}

angular.module('scenarioo.services')
    .factory('ComparisonLogResource', downgradeInjectable(ComparisonLogResource));
