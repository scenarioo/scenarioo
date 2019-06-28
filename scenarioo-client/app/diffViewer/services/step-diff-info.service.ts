import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IScenarioDiffInfo, IStepDiffInfo, IUseCaseDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import handleError from '../../shared/utils/httpErrorHandling';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class StepDiffInfoService {
    constructor(private http: HttpClient) {
    }

    get(baseBranchName: string, baseBuildName: string, comparisonName: string, useCaseName: string, scenarioName: string, stepIndex: string): Observable<IStepDiffInfo> {
        return this.http
            .get<IStepDiffInfo>(`rest/diffViewer/baseBranchName/${baseBranchName}/baseBuildName/${baseBuildName}/comparisonName/${comparisonName}/useCaseName/${useCaseName}/scenarioName/${scenarioName}/stepIndex/${stepIndex}/stepDiffInfo`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('StepDiffInfoResource', downgradeInjectable(StepDiffInfoService));
