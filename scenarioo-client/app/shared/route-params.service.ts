import {Injectable} from '@angular/core';

/**
 * Delete after full migration.
 * This service allows to access the AngularJS location service in Angular components.
 */
@Injectable()
export class RouteParamsService {
    useCaseName: string;
    scenarioName: string;
    searchTerm: string;
    objectType: string;
    objectName: string;
    pageName: string;
    pageOccurrence: string;
    stepInPageOccurrence: string;
    issueId: string;
    scenarioSketchId: string;
    stepSketchId: string;
}
