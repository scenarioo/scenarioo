import {Injectable, OnInit} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {IConfiguration} from '../generated-types/backend-types';
import {ConfigResource} from '../shared/services/configResource.service';
import {downgradeInjectable} from '@angular/upgrade/static';
import {map, tap} from 'rxjs/operators';


@Injectable()
export class ConfigurationService implements OnInit {

    private configuration = new Subject<IConfiguration>();

    // TODO remove eventually. It's hard to migrate such code. Thus this workaround.
    private _config: IConfiguration;

    private updateConfigurationSubject = configuration => {
        this.configuration.next(configuration);
        this._config = configuration;
    };

    constructor(private configResource: ConfigResource) {
    }

    ngOnInit(): void {
        this.configResource.get().subscribe(this.updateConfigurationSubject);
    }

    updateConfiguration(configuration: IConfiguration): Observable<IConfiguration> {
        return this.configResource.save(configuration)
            .pipe(tap(this.updateConfigurationSubject))
    }

    getConfiguration(): Observable<IConfiguration> {
        return this.configuration.asObservable();
    }

    applicationName(): Observable<string> {
        return this.configuration.asObservable().pipe(
            map(configuration => configuration.applicationName));
    }

    applicationInformation(): Observable<string> {
        return this.configuration.asObservable().pipe(
            map(configuration => configuration.applicationInformation)
        );
    }

    // TODO these ugly methods cause other ugly code
    getRawCopy(): IConfiguration {
        return this._config;
    }

    defaultBranchAndBuild() {
        return {
            branch: this._config.defaultBranchName,
            build: this._config.defaultBuildName,
        };
    }

    scenarioPropertiesInOverview() {
        const stringValue = this._config.scenarioPropertiesInOverview;

        let propertiesStringArray = [];
        if (angular.isString(stringValue) && stringValue.length > 0) {
            propertiesStringArray = stringValue.split(',');
        }

        const properties = new Array(propertiesStringArray.length);

        for (let i = 0; i < propertiesStringArray.length; i++) {
            properties[i] = propertiesStringArray[i].trim();
        }

        return properties;
    }

    expandPagesInScenarioOverview(): boolean {
        return this._config.expandPagesInScenarioOverview;
    }

    branchSelectionListOrder() {
        return this._config.branchSelectionListOrder;
    }

    diffViewerDiffImageColor() {
        // this ugly code comverts hex values of the form `0x123ab5` to `#123ab5`
        return '#' + ('00000' + this._config.diffImageColor).toString().substr(-6);
    }

}

angular.module('scenarioo.services')
    .factory('ConfigurationService', downgradeInjectable(ConfigurationService));
