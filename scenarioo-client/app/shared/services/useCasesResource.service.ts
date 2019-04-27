import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Labels} from './scenarioResource.service';

declare var angular: angular.IAngularStatic;

interface UseCaseSummary {
    status: string;
    name: string;
    description: string;
    numberOfScenarios: number;
    labels: Labels;
}

@Injectable()
export class UseCasesResource {
    constructor(private httpClient: HttpClient) {

    }

    query(build: BuildInfo): Observable<UseCaseSummary[]> {
        return this.httpClient.get<UseCaseSummary[]>(`rest/branch/${build.branchName}/build/${build.buildName}/usecase`);
    }
}

angular.module('scenarioo.services')
    .factory('UseCasesResource', downgradeInjectable(UseCasesResource));
