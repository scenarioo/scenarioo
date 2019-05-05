import {Injectable, OnInit} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {IConfiguration} from '../generated-types/backend-types';
import {ConfigResource} from '../shared/services/configResource.service';
import {downgradeInjectable} from '@angular/upgrade/static';
import {map} from 'rxjs/operators';


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

    updateConfiguration(configuration: IConfiguration) {
        this.configResource.save(configuration).subscribe(this.updateConfigurationSubject);
    }

    getConfiguration(): Observable<IConfiguration> {
        return this.configuration.asObservable();
    }

    applicationName(): Observable<string> {
        return this.configuration.asObservable().pipe(
            map(configuration => configuration.applicationName));
    }

    // TODO this ugly method causes other ugly code
    defaultBranchAndBuild() {
        return {
            branch: this._config.defaultBranchName,
            build: this._config.defaultBuildName,
        };
    }
}

angular.module('scenarioo.services')
    .factory('ConfigurationService', downgradeInjectable(ConfigurationService));
