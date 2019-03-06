import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {Build} from './branchesResource.service';

declare var angular: angular.IAngularStatic;

export interface BuildIdentifier {
    branchName: string;
    buildName: string;
}

export interface BuildImportSummary {
    identifier: BuildIdentifier;
    buildDescription: Build;
    buildStatistics: {
        numberOfFailedScenarios: number,
        numberOfFailedUseCases: number,
        numberOfSuccessfulScenarios: number,
        numberOfSuccessfulUseCases: number,
    };
    importDate: string;
    status: string;
    statusMessage: string;
}

@Injectable()
export class BuildImportStatesResource {
    url = 'rest/builds/buildImportSummaries';

    constructor(private httpClient: HttpClient) {
    }

    get(): Observable<BuildImportSummary[]> {
        return this.httpClient.get<BuildImportSummary[]>(this.url);
    }
}

angular.module('scenarioo.services')
    .factory('BuildImportStatesResource', downgradeInjectable(BuildImportStatesResource));
