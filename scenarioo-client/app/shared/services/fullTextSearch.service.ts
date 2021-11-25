import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {Observable} from 'rxjs';
import {Url} from '../utils/url';

declare var angular: angular.IAngularStatic;

@Injectable()
export class FullTextSearchService {

    constructor(private httpClient: HttpClient) {
    }

    search(buildInfo: BuildInfo, query: string, includeHtmlAsString: string): Observable<any> {
        // TODO: Return typed results.
        const url = Url.encodeComponents `rest/branch/${buildInfo.branchName}/build/${buildInfo.buildName}/search`;
        return this.httpClient
            .get<any>(url,
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
