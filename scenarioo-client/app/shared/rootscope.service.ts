import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class RootScopeService {

    buildStateToClassMapping = {};

    getStatusStyleClass(buildStatus: string) {
        const styleClassFromMapping = this.buildStateToClassMapping[buildStatus];
        if (styleClassFromMapping == null) {
            return 'label-warning';
        } else {
            return styleClassFromMapping;
        }
    }

}

angular.module('scenarioo.services').factory('RootScopeService', downgradeInjectable(RootScopeService));
