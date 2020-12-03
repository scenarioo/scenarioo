import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {ISketchIds} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

@Injectable()
export class SketchIdsResource {

    constructor(private httpClient: HttpClient) {
    }

    get(branchName: string, issueId: number): Observable<ISketchIds> {
        const encodedBranch = encodeURIComponent(branchName);
        return this.httpClient.get<ISketchIds>(`rest/branch/${encodedBranch}/issue/${issueId}/ids`);
    }
}

angular.module('scenarioo.services')
    .factory('SketchIdsResource', downgradeInjectable(SketchIdsResource));
