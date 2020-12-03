import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IStepDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import handleError from '../../shared/utils/httpErrorHandling';
import {downgradeInjectable} from '@angular/upgrade/static';
import {Url} from '../../shared/utils/url';

declare var angular: angular.IAngularStatic;

@Injectable()
export class StepDiffInfoService {
    constructor(private http: HttpClient) {
    }

    get(baseBranchName: string, baseBuildName: string, comparisonName: string, useCaseName: string, scenarioName: string, stepIndex: string): Observable<IStepDiffInfo> {
        const url = Url.encodeComponents `rest/diffViewer/baseBranchName/${baseBranchName}/baseBuildName/${baseBuildName}/comparisonName/${comparisonName}/useCaseName/${useCaseName}/scenarioName/${scenarioName}/stepIndex/${stepIndex}/stepDiffInfo`;
        return this.http
            .get<IStepDiffInfo>(url)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('StepDiffInfoResource', downgradeInjectable(StepDiffInfoService));
