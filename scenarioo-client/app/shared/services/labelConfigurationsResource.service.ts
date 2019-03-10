import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {LabelConfiguration} from './labelConfigurationsListResource.service';

declare var angular: angular.IAngularStatic;

export interface LabelConfigurationMap {
    [key: string]: LabelConfiguration;
}

@Injectable()
export class LabelConfigurationsResource {
    url = 'rest/labelconfigurations';

    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<LabelConfigurationMap> {
        return this.httpClient.get<LabelConfigurationMap>(this.url);
    }

    save(labelConfigs: LabelConfigurationMap) {
        return this.httpClient.post<LabelConfigurationMap>(this.url, labelConfigs);
    }
}

angular.module('scenarioo.services')
    .factory('LabelConfigurationsResource', downgradeInjectable(LabelConfigurationsResource));

