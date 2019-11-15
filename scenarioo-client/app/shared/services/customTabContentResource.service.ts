import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';
import {ICustomObjectTabTree} from '../../generated-types/backend-types';
import encodeUri from '../utils/httpUriEncoder';

declare var angular: angular.IAngularStatic;

@Injectable()
export class CustomTabContentResource {
    constructor(private httpClient: HttpClient) {

    }

    get(buildInfo: BuildInfo, tabId: number): Observable<ICustomObjectTabTree> {
        return this.httpClient.get<ICustomObjectTabTree>(encodeUri(['rest', 'branches', buildInfo.branchName, 'builds', buildInfo.buildName, 'customTabObjects', tabId.toString()]));
    }
}

angular.module('scenarioo.services')
    .factory('CustomTabContentResource', downgradeInjectable(CustomTabContentResource));
