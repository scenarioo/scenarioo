import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {ILabels} from '../../generated-types/backend-types';
import {UseCaseSummary} from './useCasesResource.service';

declare var angular: angular.IAngularStatic;

export interface RelatedIssueSummary {
    name: string;
}

@Injectable()
export class RelatedIssueResource {

    constructor(private httpClient: HttpClient) {
    }

    get(branchName: {}, buildName: {}, useCaseName: {}, scenarioName: {}, pageName: {}, pageOccurence: {}, stepInPageOccurrence: {}): Observable<RelatedIssueSummary[]> {
        return this.httpClient.get<RelatedIssueSummary[]>(`branch/${branchName}/issue/related/${buildName}/${useCaseName}/${scenarioName}/${pageName}/${pageOccurence}/${stepInPageOccurrence}`);
    }

    getForScenariosOverview(branchName: {}, buildName: {}, useCaseName: {}): Observable<RelatedIssueSummary[]> {
        return this.httpClient.get<RelatedIssueSummary[]>(`branch/${branchName}/issue/related/${buildName}/${useCaseName}`);
    }
}

angular.module('scenarioo.services')
    .factory('RelatedIssueResource', downgradeInjectable(RelatedIssueResource));
