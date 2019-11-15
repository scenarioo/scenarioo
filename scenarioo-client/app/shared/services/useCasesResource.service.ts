import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {ILabels} from '../../generated-types/backend-types';
import encodeUri from '../utils/httpUriEncoder';

declare var angular: angular.IAngularStatic;

interface UseCaseSummary {
    status: string;
    name: string;
    description: string;
    numberOfScenarios: number;
    labels: ILabels;
}

@Injectable()
export class UseCasesResource {
    constructor(private httpClient: HttpClient) {

    }

    query(build: BuildInfo): Observable<UseCaseSummary[]> {
        return this.httpClient.get<UseCaseSummary[]>(encodeUri(['rest', 'branch', build.branchName, 'build', build.buildName, 'usecase']));
    }
}

angular.module('scenarioo.services')
    .factory('UseCasesResource', downgradeInjectable(UseCasesResource));
