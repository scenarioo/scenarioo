import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

@Injectable()
export class SketchIdsResource {

    constructor(private httpClient: HttpClient) {
    }

    get(branchName: string, issueId: number): Observable<any> {
        return this.httpClient.get(`rest/branch/${branchName}/issue/${issueId}/ids`);
    }
}

angular.module('scenarioo.services')
    .factory('SketchIdsResource', downgradeInjectable(SketchIdsResource));
