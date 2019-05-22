import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {catchError} from 'rxjs/operators';
import handleError from '../utils/httpErrorHandling';
import {IBranchAlias} from '../../generated-types/backend-types';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BranchAliasesResource {
    constructor(private httpClient: HttpClient) {

    }

    get(): Observable<IBranchAlias[]> {
        return this.httpClient.get<IBranchAlias[]>('rest/branchaliases')
            .pipe(catchError(handleError));
    }

    save(branchAliases: IBranchAlias[]): Observable<void> {
        return this.httpClient.post<void>('rest/branchaliases', branchAliases, {})
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('BranchAliasesResource', downgradeInjectable(BranchAliasesResource));
