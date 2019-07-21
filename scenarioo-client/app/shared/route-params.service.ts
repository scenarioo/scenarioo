import {Injectable} from '@angular/core';

/**
 * Delete after full migration.
 * This service allows to access the AngularJS location service in Angular components.
 */
@Injectable()
export class RouteParamsService {
    useCaseName: string;
    stepNavigation;
}
