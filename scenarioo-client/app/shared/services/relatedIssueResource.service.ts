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
        const encodedBranch = encodeURIComponent(build.branchName);
        const encodedBuild = encodeURIComponent(build.buildName);
        const encodedUseCase = encodeURIComponent(useCaseName);
        const encodedScenario = encodeURIComponent(scenarioName);
        const encodedPage = encodeURIComponent(pageName);

        return this.httpClient.get<RelatedIssueSummary[]>(`rest/branch/${encodedBranch}/issue/related/${encodedBuild}/${encodedUseCase}/${encodedScenario}/${encodedPage}/${pageOccurence}/${stepInPageOccurrence}`);
    }

    getForStepsOverview(build: BuildInfo, useCaseName: string, scenarioName: string): Observable<RelatedIssueSummary[]> {
        const encodedBranch = encodeURIComponent(build.branchName);
        const encodedBuild = encodeURIComponent(build.buildName);
        const encodedUseCase = encodeURIComponent(useCaseName);
        const encodedScenario = encodeURIComponent(scenarioName);

        return this.httpClient.get<RelatedIssueSummary[]>(`rest/branch/${encodedBranch}/issue/related/${encodedBuild}/${encodedUseCase}/${encodedScenario}`);
    }

    getForScenariosOverview(build: BuildInfo, useCaseName: string): Observable<RelatedIssueSummary[]> {
        const encodedBranch = encodeURIComponent(build.branchName);
        const encodedBuild = encodeURIComponent(build.buildName);
        const encodedUseCase = encodeURIComponent(useCaseName);

        return this.httpClient.get<RelatedIssueSummary[]>(`rest/branch/${encodedBranch}/issue/related/${encodedBuild}/${encodedUseCase}`);
    }
}

angular.module('scenarioo.services')
    .factory('RelatedIssueResource', downgradeInjectable(RelatedIssueResource));
