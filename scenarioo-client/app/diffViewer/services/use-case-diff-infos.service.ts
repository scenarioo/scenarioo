import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IUseCaseDiffInfo} from '../../generated-types/backend-types';
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
        const encodedBaseBranch = encodeURIComponent(baseBranchName);
        const encodedBaseBuild = encodeURIComponent(baseBuildName);
        const encodedComparison = encodeURIComponent(comparisonName);

        return this.http
            .get<IUseCaseDiffInfo[]>(`rest/diffViewer/baseBranchName/${encodedBaseBranch}/baseBuildName/${encodedBaseBuild}/comparisonName/${encodedComparison}/useCaseDiffInfos`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('UseCaseDiffInfosResource', downgradeInjectable(UseCaseDiffInfosService));
