import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';
import {BuildInfo} from './comparisonCreateResource.service';

declare var angular: angular.IAngularStatic;

interface CustomObjectTabTreeNodeDetails {
    description: string;
}

interface CustomObjectTabTreeNode {
    name: string;
    type: string;
    details?: CustomObjectTabTreeNodeDetails;
    children: CustomObjectTabTreeNode[];
}

interface CustomObjectTabTree {
    tree: CustomObjectTabTreeNode[];
}

@Injectable()
export class CustomTabContentResource {
    constructor(private httpClient: HttpClient) {

    }

    get(buildInfo: BuildInfo, tabId: number): Observable<CustomObjectTabTree> {
        return this.httpClient.get<CustomObjectTabTree>(`rest/branches/${buildInfo.branchName}/builds/${buildInfo.buildName}/customTabObjects/${tabId}`);
    }
}

angular.module('scenarioo.services')
    .factory('CustomTabContentResource', downgradeInjectable(CustomTabContentResource));
