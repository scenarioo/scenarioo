import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Url} from '../utils/url';

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
        const url = Url.encodeComponents `rest/branch/${build.branchName}/issue/related/${build.buildName}/${useCaseName}/${scenarioName}/${pageName}/${pageOccurence.toString()}/${stepInPageOccurrence.toString()}`;
        return this.httpClient.get<RelatedIssueSummary[]>(url);
    }

    getForStepsOverview(build: BuildInfo, useCaseName: string, scenarioName: string): Observable<RelatedIssueSummary[]> {
        const url = Url.encodeComponents `rest/branch/${build.branchName}/issue/related/${build.buildName}/${useCaseName}/${scenarioName}`;
        return this.httpClient.get<RelatedIssueSummary[]>(url);
    }

    getForScenariosOverview(build: BuildInfo, useCaseName: string): Observable<RelatedIssueSummary[]> {
        const url = Url.encodeComponents `rest/branch/${build.branchName}/issue/related/${build.buildName}/${useCaseName}`;
        return this.httpClient.get<RelatedIssueSummary[]>(url);
    }
}

angular.module('scenarioo.services')
    .factory('RelatedIssueResource', downgradeInjectable(RelatedIssueResource));
