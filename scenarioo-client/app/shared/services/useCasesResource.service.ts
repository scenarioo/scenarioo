import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {ILabels, IUseCaseSummary} from '../../generated-types/backend-types';
import encodeUri from '../utils/httpUriEncoder';

declare var angular: angular.IAngularStatic;

@Injectable()
export class UseCasesResource {

    constructor(private httpClient: HttpClient) {
    }

    query(build: BuildInfo): Observable<IUseCaseSummary[]> {
        return this.httpClient.get<IUseCaseSummary[]>(encodeUri(['rest', 'branch', build.branchName, 'build', build.buildName, 'usecase']));
    }
}

angular.module('scenarioo.services')
    .factory('UseCasesResource', downgradeInjectable(UseCasesResource));
