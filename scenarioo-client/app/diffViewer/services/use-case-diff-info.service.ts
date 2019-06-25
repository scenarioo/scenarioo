import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IUseCaseDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import handleError from '../../shared/utils/httpErrorHandling';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class UseCaseDiffInfoService {
    constructor(private http: HttpClient) {
    }

    get(baseBranchName: string, baseBuildName: string, comparisonName: string, useCaseName: string): Observable<IUseCaseDiffInfo> {
        return this.http
            .get<IUseCaseDiffInfo>(`rest/diffViewer/baseBranchName/${baseBranchName}/baseBuildName/${baseBuildName}/comparisonName/${comparisonName}/useCaseName/${useCaseName}/useCaseDiffInfo`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('UseCaseDiffInfoResource', downgradeInjectable(UseCaseDiffInfoService));
