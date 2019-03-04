/* scenarioo-client
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('scenarioo.services').service('ConfigService', (ConfigResource, $rootScope) => {

    const CONFIG_LOADED_EVENT = 'configLoaded';

    let configData: any = {};

    function getValue(key) {
        return configData[key];
    }

    function doLoad() {
        ConfigResource.get().subscribe(response => {
            configData = response;
            $rootScope.buildStateToClassMapping = configData.buildstates;
            $rootScope.getStatusStyleClass = (buildStatus) => {
                const styleClassFromMapping = $rootScope.buildStateToClassMapping[buildStatus];
                if (angular.isUndefined(styleClassFromMapping)) {
                    return 'label-warning';
                } else {
                    return styleClassFromMapping;
                }
            };

            $rootScope.$broadcast(CONFIG_LOADED_EVENT);
        });
    }

    function getBuildStateToClassMapping() {
        return configData.buildstates;
    }

    function getScenarioPropertiesInOverview() {
        const stringValue = getValue('scenarioPropertiesInOverview');

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

    return {
        CONFIG_LOADED_EVENT,

        getRawConfigDataCopy() {
            return angular.copy(configData);
        },

        /**
         * Will fire event 'configLoaded'
         */
        load() {
            doLoad();
        },

        isLoaded() {
            return angular.isDefined(configData.defaultBuildName);
        },

        updateConfiguration(newConfig, successCallback) {
            ConfigResource.save(newConfig).subscribe(() => {
                if (successCallback) {
                    doLoad();
                    successCallback();
                }
            })
        },

        defaultBranchAndBuild() {
            return {
                branch: getValue('defaultBranchName'),
                build: getValue('defaultBuildName'),
            };
        },

        scenarioPropertiesInOverview() {
            return getScenarioPropertiesInOverview();
        },

        applicationName() {
            return getValue('applicationName');
        },

        applicationInformation() {
            return getValue('applicationInformation');
        },

        buildStateToClassMapping() {
            return getBuildStateToClassMapping();
        },

        expandPagesInScenarioOverview() {
            return getValue('expandPagesInScenarioOverview');
        },

        branchSelectionListOrder() {
            return getValue('branchSelectionListOrder');
        },

        diffViewerDiffImageColor() {
            // this ugly code comverts hex values of the form `0x123ab5` to `#123ab5`
            return '#' + ('00000' + getValue('diffImageColor')).toString().substr(-6);
        },

    };
});
