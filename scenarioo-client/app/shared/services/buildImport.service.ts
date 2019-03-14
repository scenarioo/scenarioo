import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BuildImportService {
    url = 'rest/builds/updateAndImport';

    constructor(private httpClient: HttpClient) {

    }

    updateData(): Observable<any> {
        return this.httpClient.get(this.url);
    }
}

angular.module('scenarioo.services')
    .factory('BuildImportService', downgradeInjectable(BuildImportService));
