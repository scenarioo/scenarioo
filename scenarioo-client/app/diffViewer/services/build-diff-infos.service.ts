import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {IBuildDiffInfo} from '../../generated-types/backend-types';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';
import handleError from '../../shared/utils/httpErrorHandling';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BuildDiffInfosService {
    constructor(private http: HttpClient) {
    }

    get(baseBranchName: string, baseBuildName: string): Observable<IBuildDiffInfo[]> {
        const encodedBaseBranch = encodeURIComponent(baseBranchName);
        const encodedBaseBuild = encodeURIComponent(baseBuildName);

        return this.http
            .get<IBuildDiffInfo[]>(`rest/diffViewer/baseBranchName/${encodedBaseBranch}/baseBuildName/${encodedBaseBuild}/buildDiffInfos`)
            .pipe(catchError(handleError));
    }
}

angular.module('scenarioo.services')
    .factory('BuildDiffInfosResource', downgradeInjectable(BuildDiffInfosService));
