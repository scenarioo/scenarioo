import {Injectable} from '@angular/core';
import {downgradeInjectable} from '@angular/upgrade/static';
import {ConfigResource} from '../shared/services/configResource.service';
import {Configuration} from '../shared/services/applicationStatus.service';
import {Subject} from 'rxjs';
import {RootScopeService} from '../shared/rootscope.service';

declare var angular: angular.IAngularStatic;

@Injectable()
export class MigratedConfigService {

    configData: Configuration;

    private configLoadedSubject = new Subject<boolean>();
    configLoaded$ = this.configLoadedSubject.asObservable();

    constructor(private configResource: ConfigResource, private rootScopeService: RootScopeService) {
    }

    getRawConfigDataCopy() {
        // TODO: this needs to be ported to an angular-safe deep-clone version, angular.copy will not be available after migration is complete
        return angular.copy(this.configData);
    }

    load(): void {
        this.configResource.get().subscribe((response) => {
            this.configData = response;
            this.rootScopeService.buildStateToClassMapping = this.configData.buildstates;
            this.configLoadedSubject.next(true);
        });
    }

    getBuildStateToClassMapping(): { [key: string]: string } {
        return this.configData.buildstates;
    }

    getScenarioPropertiesInOverview(): string[] {
        const stringValue = this.configData.scenarioPropertiesInOverview;

        let propertiesStringArray = [];
        if (stringValue && typeof stringValue === 'string' && stringValue.length > 0) {
            propertiesStringArray = stringValue.split(',');
        }

        const properties = new Array(propertiesStringArray.length);

        for (let i = 0; i < propertiesStringArray.length; i++) {
            properties[i] = propertiesStringArray[i].trim();
        }

        return properties;
    }

    isLoaded() {
        return this.configData.defaultBuildName != null;
    }

    updateConfiguration(newConfig: Configuration, successCallback) {
        this.configResource.save(newConfig).subscribe(() => {
            if (successCallback) {
                this.load();
                successCallback();
            }
        });
    }

    defaultBranchAndBuild() {
        if (this.configData) {
            return {
                branch: this.configData.defaultBranchName,
                build: this.configData.defaultBuildName,
            };
        }
    }

    scenarioPropertiesInOverview() {
        return this.getScenarioPropertiesInOverview();
    }

    applicationName() {
        return this.configData.applicationName;
    }

    applicationInformation() {
        return this.configData.applicationInformation;
    }

    buildStateToClassMapping() {
        return this.getBuildStateToClassMapping();
    }

    expandPagesInScenarioOverview() {
        return this.configData.expandPagesInScenarioOverview;
    }

    branchSelectionListOrder() {
        return this.configData.branchSelectionListOrder;
    }

    diffViewerDiffImageColor(): string {
        // this ugly code converts hex values of the form `0x123ab5` to `#123ab5`
        return '#' + ('00000' + this.configData.diffImageColor).toString().substr(-6);
    }
}

angular.module('scenarioo.services').factory('MigratedConfigService', downgradeInjectable(MigratedConfigService));
