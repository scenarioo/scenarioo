import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

@Injectable()
export class StepResource {
    constructor(private httpClient: HttpClient) {

    }

    get(build: BuildInfo, usecaseName: string, scenarioName: string, pageName: string, pageOccurrence: string, stepInPageOccurrence: string, labels?: string): Observable<any> {
        return this.httpClient.get<any>(`rest/branch/${build.branchName}/build/${build.buildName}/usecase/${usecaseName}/scenario/${scenarioName}/pageName/${pageName}/pageOccurrence/${pageOccurrence}/stepInPageOccurrence/${stepInPageOccurrence}`,
            {
                params: {labels},
            });
    }
}

angular.module('scenarioo.services')
    .factory('StepResource', downgradeInjectable(StepResource));
