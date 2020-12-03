import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IScenarioDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import handleError from '../../shared/utils/httpErrorHandling';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class StepDiffInfosService {
    constructor(private http: HttpClient) {
    }

    get(baseBranchName: string, baseBuildName: string, comparisonName: string, useCaseName: string, scenarioName: string): Observable<IScenarioDiffInfo[]> {
        const encodedBaseBranch = encodeURIComponent(baseBranchName);
        const encodedBaseBuild = encodeURIComponent(baseBuildName);
        const encodedComparison = encodeURIComponent(comparisonName);
        const encodedUseCase = encodeURIComponent(useCaseName);
        const encodedScenario = encodeURIComponent(scenarioName);

        return this.http
            .get<IScenarioDiffInfo[]>(`rest/diffViewer/baseBranchName/${encodedBaseBranch}/baseBuildName/${encodedBaseBuild}/comparisonName/${encodedComparison}/useCaseName/${encodedUseCase}/scenarioName/${encodedScenario}/stepDiffInfos`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('StepDiffInfosResource', downgradeInjectable(StepDiffInfosService));
