import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import encodeUri from '../utils/httpUriEncoder';
import {IStepDetails} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

@Injectable()
export class StepResource {

    constructor(private httpClient: HttpClient) {
    }

    get(build: BuildInfo, usecaseName: string, scenarioName: string, pageName: string, pageOccurrence: number, stepInPageOccurrence: number, labels?: string): Observable<IStepDetails> {
        return this.httpClient.get<any>(encodeUri(['rest', 'branch', build.branchName, 'build', build.buildName, 'usecase', usecaseName, 'scenario', scenarioName, 'pageName', pageName, 'pageOccurrence', pageOccurrence.toString(), 'stepInPageOccurrence', stepInPageOccurrence.toString()]),
            {
                params: {labels},
            });
    }
}

angular.module('scenarioo.services')
    .factory('StepResource', downgradeInjectable(StepResource));
