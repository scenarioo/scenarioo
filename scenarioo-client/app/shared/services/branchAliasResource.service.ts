import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {catchError} from 'rxjs/operators';
import handleError from '../utils/httpErrorHandling';

declare var angular: angular.IAngularStatic;

export interface BranchAlias {
    name: string;
    referencedBranch: string;
    description: string;
}

@Injectable()
export class BranchAliasesResource {
    constructor(private httpClient: HttpClient) {

    }

    get(): Observable<BranchAlias[]> {
        return this.httpClient.get<BranchAlias[]>('rest/branchaliases')
            .pipe(catchError(handleError));
    }

    save(branchAliases: BranchAlias[]): Observable<void> {
        return this.httpClient.post('rest/branchaliases', branchAliases, {})
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('BranchAliasesResource', downgradeInjectable(BranchAliasesResource));
