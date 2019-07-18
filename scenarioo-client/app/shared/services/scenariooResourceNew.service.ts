import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';

declare var angular: angular.IAngularStatic;

@Injectable()
export class ScenariooResourceNewService {

    /*
    $resource(url, paramDefaults, actions) {
        return $resource('rest' + url, paramDefaults, actions);
    }
    */

}

angular.module('scenarioo.services')
    .factory('ScenariooResourceNewService', downgradeInjectable(ScenariooResourceNewService));
