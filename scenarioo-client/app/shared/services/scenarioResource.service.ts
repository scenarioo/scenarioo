import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

interface ScenarioStatistics {
    numberOfSteps: number;
    numberOfPages: number;
}

interface PageSummary {
    name: string;
    pageOccurrence: number;
}

interface StepDescription {
    index: number;
    title: string;
    status: string;
    screenshotFileName: string;
    labels: Labels;
    details: Details;
}

interface PageWithSteps {
    page: PageSummary;
    steps: StepDescription[];

}

interface Scenario {
    description: string;
    details: Details;
    labels: Labels;
    name: string;
    status: string;
}

interface Details {
    [key: string]: any;
}

export interface Labels {
    labels: string[];
    empty: boolean;
}

interface UseCase {
    description: string;
    details: Details;
    labels: Labels;
    name: string;
    status: string;
}

interface ScenarioSummary {
    scenario: Scenario;
    numberOfSteps: number;
}

export interface ScenarioDetails {
    scenario: Scenario;
    scenarioStatistics: ScenarioStatistics;
    useCase: UseCase;
    pagesAndSteps: PageWithSteps[];
}

export interface UseCaseScenarios {
    useCase: UseCase;
    scenarios: ScenarioSummary[];
}

@Injectable()
export class ScenarioResource {

    constructor(private httpClient: HttpClient) {
    }

    getUseCaseScenarios(build: BuildInfo, usecaseName: string): Observable<UseCaseScenarios> {
        return this.httpClient.get<UseCaseScenarios>(`rest/branch/${build.branchName}/build/${build.buildName}/usecase/${usecaseName}/scenario`);
    }

    get(build: BuildInfo, usecaseName: string, scenarioName?: string): Observable<ScenarioDetails> {
        return this.httpClient.get<ScenarioDetails>(`rest/branch/${build.branchName}/build/${build.buildName}/usecase/${usecaseName}/scenario/${scenarioName}`);

    }
}

angular.module('scenarioo.services')
    .factory('ScenarioResource', downgradeInjectable(ScenarioResource));
