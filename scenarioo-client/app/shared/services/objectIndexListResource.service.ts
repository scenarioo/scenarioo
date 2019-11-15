import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import encodeUrl from '../utils/httpUrlEncoder';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ObjectIndexListResource {

    constructor(private httpClient: HttpClient) {
    }

    get(build: BuildInfo, objectType, objectName): Observable<any[]> {
        return this.httpClient.get<any[]>(`${encodeUrl(['rest', 'branch', build.branchName, 'build', build.buildName, 'object', objectType, 'name'])}?name=${encodeUrl([objectName])}`);
    }
}

angular.module('scenarioo.services')
    .factory('ObjectIndexListResource', downgradeInjectable(ObjectIndexListResource));
