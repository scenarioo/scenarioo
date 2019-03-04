import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {downgradeInjectable} from "@angular/upgrade/static";
import {Configuration} from "./applicationStatus.service";

declare var angular: angular.IAngularStatic;


@Injectable()
export class ConfigResource {
    url = "rest/configuration";

    constructor(private httpClient: HttpClient) {

    }

    get(): Observable<Configuration> {
        return this.httpClient.get<Configuration>(this.url);
    }

    save(config: Configuration): Observable<Configuration> {
        return this.httpClient.post<Configuration>(this.url, config);
    }
}

angular.module('scenarioo.services')
    .factory('ConfigResource', downgradeInjectable(ConfigResource));
