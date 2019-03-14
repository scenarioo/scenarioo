import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {downgradeInjectable} from "@angular/upgrade/static";
import {BuildInfo} from "./comparisonCreateResource.service";

declare var angular: angular.IAngularStatic;


@Injectable()
export class ObjectIndexListResource {
    url = 'rest/builds/buildImportSummaries';

    constructor(private httpClient: HttpClient) {
    }

    get(build: BuildInfo, objectType, objectName): Observable<any[]> {
        return this.httpClient.get<any[]>(`rest/branch/${build.branchName}/build/${build.buildName}/object/${objectType}/name?name=${objectName}`);
    }
}

angular.module('scenarioo.services')
    .factory('ObjectIndexListResource', downgradeInjectable(ObjectIndexListResource));
