import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

export interface Branch {
    name: string;
    description: string;
    details: any;
}

export interface BuildDetails {
    [key: string]: object;
}

export interface Build {
    name: string;
    revision: string;
    date: string; // TODO: DateTime
    status: string;
    details: BuildDetails;
}

export interface BuildLink {
    displayName: string;
    build: Build;
    linkName: string;
}

export interface BranchBuild {
    alias: boolean;
    isAlias: boolean;
    branch: Branch;
    builds: BuildLink;

}

@Injectable()
export class BranchesResource {
    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<BranchBuild[]> {
        return this.httpClient.get<BranchBuild[]>('rest/branches');
    }
}

angular.module('scenarioo.services')
    .factory('BranchesResource', downgradeInjectable(BranchesResource));
