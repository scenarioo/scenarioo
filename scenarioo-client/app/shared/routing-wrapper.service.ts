import {Injectable} from '@angular/core';
import {LocationService} from './location.service';
import {ReplaySubject} from 'rxjs';
import {RootScopeService} from './rootScope.service';

/*
    Wrapper service for routing specific actions. Should implement the Angular specific routing actions once ready to migrate to Routing.
    Specific types will need to be set as well (e.g. instead of QueryParamMap, use the types specific to Angular)
 */

interface QueryParamMap {
    [key: string]: string;
}

@Injectable()
export class RoutingWrapperService {
    private readonly queryParamChange$ = new ReplaySubject<QueryParamMap>(1);

    constructor(private locationService: LocationService, private rootScopeService: RootScopeService) {
        this.rootScopeService.$watch(() => this.locationService.search(), (newParams) => this.queryParamChange$.next(newParams), true);
    }

    setQueryParam(key: string, value: string): void {
        this.locationService.search(key, value);
    }

    getAllQueryParams(): QueryParamMap {
        return this.locationService.search();
    }

    onQueryParamsChanged(callback: any): void {
        this.queryParamChange$.subscribe((newParams) => callback(newParams));
    }
}
