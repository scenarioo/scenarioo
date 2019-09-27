import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

export interface RelatedIssueSummary {
    id: number;
    name: string;
    firstScenarioSketchId: number;
}

@Injectable()
export class RelatedIssueResource {

    constructor(private httpClient: HttpClient) {
    }

    get(build: BuildInfo, useCaseName: string, scenarioName: string, pageName: string, pageOccurence: number, stepInPageOccurrence: number): Observable<RelatedIssueSummary[]> {
        return this.httpClient.get<RelatedIssueSummary[]>(`rest/branch/${build.branchName}/issue/related/${build.buildName}/${useCaseName}/${scenarioName}/${pageName}/${pageOccurence}/${stepInPageOccurrence}`);
    }

    getForStepsOverview(build: BuildInfo, useCaseName: string, scenarioName: string): Observable<RelatedIssueSummary[]> {
        return this.httpClient.get<RelatedIssueSummary[]>(`rest/branch/${build.branchName}/issue/related/${build.buildName}/${useCaseName}/${scenarioName}`);
    }

    getForScenariosOverview(build: BuildInfo, useCaseName: string): Observable<RelatedIssueSummary[]> {
        return this.httpClient.get<RelatedIssueSummary[]>(`rest/branch/${build.branchName}/issue/related/${build.buildName}/${useCaseName}`);
    }
}

angular.module('scenarioo.services')
    .factory('RelatedIssueResource', downgradeInjectable(RelatedIssueResource));
