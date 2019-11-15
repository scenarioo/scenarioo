import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import encodeUrl from '../utils/httpUrlEncoder';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BuildReimportResource {

    constructor(private httpClient: HttpClient) {
    }

    get(branchName: string, buildName: string): Observable<any> {
        // Returns just OK or NOT_FOUND.
        return this.httpClient.get(`${encodeUrl(['rest', 'builds', branchName, buildName, 'import'])}`, {responseType: 'text'});
    }
}

angular.module('scenarioo.services')
    .factory('BuildReimportResource', downgradeInjectable(BuildReimportResource));
