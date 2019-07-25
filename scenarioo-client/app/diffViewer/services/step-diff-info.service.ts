import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IScenarioDiffInfo, IStepDiffInfo, IUseCaseDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import handleError from '../../shared/utils/httpErrorHandling';
import {downgradeInjectable} from '@angular/upgrade/static';
import {StepDiffInfo} from '../types/StepDiffInfo';

declare var angular: angular.IAngularStatic;

@Injectable()
export class StepDiffInfoService {
    constructor(private http: HttpClient) {
    }

    get(baseBranchName: string, baseBuildName: string, comparisonName: string, useCaseName: string, scenarioName: string, stepIndex: string): Observable<StepDiffInfo> {
        return this.http
            .get<IStepDiffInfo>(`rest/diffViewer/baseBranchName/${baseBranchName}/baseBuildName/${baseBuildName}/comparisonName/${comparisonName}/useCaseName/${useCaseName}/scenarioName/${scenarioName}/stepIndex/${stepIndex}/stepDiffInfo`)
            .pipe(map((response: IStepDiffInfo) => new StepDiffInfo(response)),
                catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('StepDiffInfoResource', downgradeInjectable(StepDiffInfoService));
