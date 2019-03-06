import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {catchError} from 'rxjs/operators';
import {_throw} from 'rxjs/observable/throw';

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
            .pipe(catchError(BranchAliasesResource.handleError));
    }

    save(branchAliases: BranchAlias[]) {
        return this.httpClient.post('rest/branchaliases', branchAliases, {})
            .pipe(catchError(BranchAliasesResource.handleError));

    }

    private static handleError(error: HttpErrorResponse) {
        if (error.error instanceof ErrorEvent) {
            // A client-side or network error occurred. Handle it accordingly.
            console.error('An error occurred:', error.error.message);
        } else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong,
            console.error(
                `Backend returned code ${error.status}, ` +
                `body was: ${error.error}`);
        }
        // return an observable with a user-facing error message
        return _throw(
            'Something bad happened; please try again later.');
    }
}

angular.module('scenarioo.services')
    .factory('BranchAliasesResource', downgradeInjectable(BranchAliasesResource));
