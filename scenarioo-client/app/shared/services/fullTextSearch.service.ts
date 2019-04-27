import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Observable} from 'rxjs';

declare var angular: angular.IAngularStatic;

@Injectable()
export class FullTextSearchService {

    constructor(private httpClient: HttpClient) {
    }

    search(buildInfo: BuildInfo, query: string, includeHtmlAsString: string): Observable<any> {
        // TODO: Return typed results.
        return this.httpClient
            .get<any>(`rest/branch/${buildInfo.branchName}/build/${buildInfo.buildName}/search`,
                {
                    params: {
                        q: query,
                        includeHtml: includeHtmlAsString,
                    },
                });
    }
}

angular.module('scenarioo.services')
    .factory('FullTextSearchService', downgradeInjectable(FullTextSearchService));
