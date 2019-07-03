import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
declare var angular: angular.IAngularStatic;

// TODO duplicated with LabelResource
@Injectable()
export class LabelConfigurationService {

    constructor(private http: HttpClient) {
    }

    get(): Observable<any> {
        return this.http.get('rest/labelconfigurations');
    }
}

angular.module('scenarioo.services')
    .factory('labelConfigurationService', downgradeInjectable(LabelConfigurationService));
