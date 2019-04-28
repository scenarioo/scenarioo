import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {IBuildDiffInfo} from "../../generated-types/backend-types";

declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonsResource {
    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<IBuildDiffInfo[]> {
        return this.httpClient.get<IBuildDiffInfo[]>('rest/comparisons');
    }
}

angular.module('scenarioo.services')
    .factory('ComparisonsResource', downgradeInjectable(ComparisonsResource));
