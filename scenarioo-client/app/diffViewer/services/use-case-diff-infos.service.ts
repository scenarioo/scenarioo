import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IBuildDiffInfo, IUseCaseDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import handleError from '../../shared/utils/httpErrorHandling';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class UseCaseDiffInfosService {
    constructor(private http: HttpClient) {
    }

    get(baseBranchName: string, baseBuildName: string, comparisonName: string): Observable<IUseCaseDiffInfo[]> {
        return this.http
            .get<IUseCaseDiffInfo[]>(`rest/diffViewer/baseBranchName/${baseBranchName}/baseBuildName/${baseBuildName}/comparisonName/${comparisonName}/useCaseDiffInfos`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('UseCaseDiffInfosResource', downgradeInjectable(UseCaseDiffInfosService));
