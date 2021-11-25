import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';

/**
 * Delete after full migration.
 * This service allows to access the AngularJS root scope in Angular components.
 */
@Injectable()
export class RootScopeService {

    $watch(watchExpression: string | (() => any),
           listener: ((newVal?: any, oldVal?: any, scope?: any) => any),
           objectEquality?: boolean) {
        return undefined;
    }

}
