import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {IFlatLabelConfiguration} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

@Injectable()
export class LabelConfigurationsListResource {
    private url = 'rest/labelconfigurations/list';

    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<IFlatLabelConfiguration[]> {
        return this.httpClient.get<IFlatLabelConfiguration[]>(this.url);
    }
}

angular.module('scenarioo.services')
    .factory('LabelConfigurationsListResource', downgradeInjectable(LabelConfigurationsListResource));
