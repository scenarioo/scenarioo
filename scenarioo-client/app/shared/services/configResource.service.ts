import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {IConfiguration} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ConfigResource {
    private url = 'rest/configuration';

    constructor(private httpClient: HttpClient) {

    }

    get(): Observable<IConfiguration> {
        return this.httpClient.get<IConfiguration>(this.url);
    }

    save(config: IConfiguration): Observable<void> {
        return this.httpClient.post<void>(this.url, config);
    }
}

// TODO remove downgrade as we should not have any references to it
angular.module('scenarioo.services')
    .factory('ConfigResource', downgradeInjectable(ConfigResource));
