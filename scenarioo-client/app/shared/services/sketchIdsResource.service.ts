import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {ISketchIds} from '../../generated-types/backend-types';
import {Url} from '../utils/url';

declare var angular: angular.IAngularStatic;

@Injectable()
export class SketchIdsResource {

    constructor(private httpClient: HttpClient) {
    }

    get(branchName: string, issueId: number): Observable<ISketchIds> {
        const url = Url.encodeComponents `rest/branch/${branchName}/issue/${issueId.toString()}/ids`;
        return this.httpClient.get<ISketchIds>(url);
    }
}

angular.module('scenarioo.services')
    .factory('SketchIdsResource', downgradeInjectable(SketchIdsResource));
