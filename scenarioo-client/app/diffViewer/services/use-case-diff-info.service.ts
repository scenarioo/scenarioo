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
        const encodedBaseBranch = encodeURIComponent(baseBranchName);
        const encodedBaseBuild = encodeURIComponent(baseBuildName);
        const encodedComparison = encodeURIComponent(comparisonName);
        const encodedUseCase = encodeURIComponent(useCaseName);

        return this.http
            .get<IUseCaseDiffInfo>(`rest/diffViewer/baseBranchName/${encodedBaseBranch}/baseBuildName/${encodedBaseBuild}/comparisonName/${encodedComparison}/useCaseName/${encodedUseCase}/useCaseDiffInfo`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('UseCaseDiffInfoResource', downgradeInjectable(UseCaseDiffInfoService));
