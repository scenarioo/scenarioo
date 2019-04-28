import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {IBuildImportSummary} from "../../generated-types/backend-types";

declare var angular: angular.IAngularStatic;


@Injectable()
export class BuildImportStatesResource {
    private url = 'rest/builds/buildImportSummaries';

    constructor(private httpClient: HttpClient) {
    }

    get(): Observable<IBuildImportSummary[]> {
        return this.httpClient.get<IBuildImportSummary[]>(this.url);
    }
}

angular.module('scenarioo.services')
    .factory('BuildImportStatesResource', downgradeInjectable(BuildImportStatesResource));
