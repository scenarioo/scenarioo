import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {catchError, tap} from 'rxjs/operators';
import handleError from '../utils/httpErrorHandling';
import {IBranchAlias} from '../../generated-types/backend-types';
import {ConfigurationService} from '../../services/configuration.service';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BranchAliasesResource {
    constructor(private httpClient: HttpClient,
                private configurationService: ConfigurationService) {
    }

    get(): Observable<IBranchAlias[]> {
        return this.httpClient.get<IBranchAlias[]>('rest/branchaliases')
            .pipe(catchError(handleError));
    }

    save(branchAliases: IBranchAlias[]): Observable<void> {
        return this.httpClient.post<void>('rest/branchaliases', branchAliases, {})
        // Arrow function is needed here, otherwise the this object in the configurationService will be wrong
            .pipe(tap(() => {
                this.configurationService.loadConfigurationFromBackend();
            }))
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('BranchAliasesResource', downgradeInjectable(BranchAliasesResource));
