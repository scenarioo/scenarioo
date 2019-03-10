import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {Build} from './branchesResource.service';
import {BuildImportSummary} from './buildImportStatesResource.service';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BuildReimportResource {

    constructor(private httpClient: HttpClient) {
    }

    get(branchName: string, buildName: string): Observable<any> {
        // Returns just OK or NOT_FOUND.
        return this.httpClient.get(`rest/builds/${branchName}/${buildName}/import`);
    }
}

angular.module('scenarioo.services')
    .factory('BuildReimportResource', downgradeInjectable(BuildReimportResource));
