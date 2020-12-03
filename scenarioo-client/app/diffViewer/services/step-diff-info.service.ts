import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IStepDiffInfo} from '../../generated-types/backend-types';
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
        const encodedBaseBranch = encodeURIComponent(baseBranchName);
        const encodedBaseBuild = encodeURIComponent(baseBuildName);
        const encodedComparison = encodeURIComponent(comparisonName);
        const encodedUseCase = encodeURIComponent(useCaseName);
        const encodedScenario = encodeURIComponent(scenarioName);

        return this.http
            .get<IStepDiffInfo>(`rest/diffViewer/baseBranchName/${encodedBaseBranch}/baseBuildName/${encodedBaseBuild}/comparisonName/${encodedComparison}/useCaseName/${encodedUseCase}/scenarioName/${encodedScenario}/stepIndex/${stepIndex}/stepDiffInfo`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('StepDiffInfoResource', downgradeInjectable(StepDiffInfoService));
