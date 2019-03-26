import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class BranchAliasService {

    branchesLoadedSubject = new Subject<boolean>();
    branchesLoaded$ = this.branchesLoadedSubject.asObservable();

}

angular.module('scenarioo.services')
    .factory('BranchAliasService', downgradeInjectable(BranchAliasService));
