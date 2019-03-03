import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {downgradeInjectable} from "@angular/upgrade/static";

declare var angular: angular.IAngularStatic;

interface BranchAlias {
    name: string;
    referencedBranch: string;
    description: string;
}

@Injectable()
export class BranchesResource {
    constructor(private httpClient: HttpClient) {

    }

    query(): Observable<BranchAlias[]> {
        return this.httpClient.get<any>("rest/branches");
    }
}

angular.module('scenarioo.services')
    .factory('BranchesResource', downgradeInjectable(BranchesResource));
