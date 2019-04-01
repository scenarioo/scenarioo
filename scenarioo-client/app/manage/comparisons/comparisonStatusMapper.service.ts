import {Injectable} from "@angular/core";
import {downgradeInjectable} from "@angular/upgrade/static";
declare var angular: angular.IAngularStatic;

@Injectable()
export class ComparisonStatusMapperService{
    styleClassesForComparisonStatus = {
        'QUEUED_FOR_PROCESSING': 'label-info',
        'PROCESSING': 'label-primary',
        'SKIPPED': 'label-default',
        'SUCCESS': 'label-success',
        'FAILED': 'label-danger'
    };

    getStyleClassForComparisonStatus(status): string {
        const styleClassFromMapping = this.styleClassesForComparisonStatus[status];
        if(angular.isUndefined(styleClassFromMapping)) {
            return 'label-warning';
        }
        else {
            return styleClassFromMapping;
        }
    }
}

angular.module('scenarioo.services')
    .factory('ComparisonStatusMapperService', downgradeInjectable(ComparisonStatusMapperService));
