import {Injectable} from '@angular/core';

/**
 * Delete after full migration.
 * This service allows to access the AngularJS root scope in Angular components,
 * which is needed until full migration is done.
 */
@Injectable()
export class RootScopeService {

    /**
     * See https://docs.angularjs.org/api/ng/type/$rootScope.Scope#$watch
     */
    $watch(watchExpression: (() => any) | string, listener: () => any, objectEquality?: boolean): any {}

}
