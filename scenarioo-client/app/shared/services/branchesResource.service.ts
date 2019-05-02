import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {IBranchBuilds} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BranchesResource {
    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<IBranchBuilds[]> {
        return this.httpClient.get<IBranchBuilds[]>('rest/branches');
    }
}

angular.module('scenarioo.services')
    .factory('BranchesResource', downgradeInjectable(BranchesResource));
