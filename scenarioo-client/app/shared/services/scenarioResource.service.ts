import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {IScenarioDetails, IUseCaseScenarios} from '../../generated-types/backend-types';
import {map} from 'rxjs/operators';
import {ScenarioDetails} from '../../diffViewer/types/ScenarioDetails';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ScenarioResource {

    constructor(private httpClient: HttpClient) {
    }

    getUseCaseScenarios(build: BuildInfo, usecaseName: string): Observable<IUseCaseScenarios> {
        return this.httpClient.get<IUseCaseScenarios>(`rest/branch/${build.branchName}/build/${build.buildName}/usecase/${usecaseName}/scenario`);
    }

    get(build: BuildInfo, usecaseName: string, scenarioName?: string): Observable<ScenarioDetails> {
        return this.httpClient.get<IScenarioDetails>(`rest/branch/${build.branchName}/build/${build.buildName}/usecase/${usecaseName}/scenario/${scenarioName}`)
            .pipe(map((response: IScenarioDetails) => new ScenarioDetails(response)));
    }
}

angular.module('scenarioo.services')
    .factory('ScenarioResource', downgradeInjectable(ScenarioResource));
