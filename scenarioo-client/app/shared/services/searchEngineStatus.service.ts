import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

export interface SearchEngineStatus {
    running: boolean;
    endpointConfigured: boolean;
    endpoint: string;
}

@Injectable()
export class SearchEngineStatusService {
    constructor(private httpClient: HttpClient) {

    }

    isSearchEngineRunning(): Observable<SearchEngineStatus> {
        return this.httpClient.get<SearchEngineStatus>('rest/searchEngineStatus');
    }
}

angular.module('scenarioo.services')
    .factory('SearchEngineStatusService', downgradeInjectable(SearchEngineStatusService));
