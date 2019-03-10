import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

export interface LabelConfiguration {
    name: string;
    backgroundColor: string;
    foregroundColor: string;
}

@Injectable()
export class LabelConfigurationsListResource {
    url = 'rest/labelconfigurations/list';

    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<LabelConfiguration[]> {
        return this.httpClient.get<LabelConfiguration[]>(this.url);
    }
}

angular.module('scenarioo.services')
    .factory('LabelConfigurationsListResource', downgradeInjectable(LabelConfigurationsListResource));
