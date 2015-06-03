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

angular.module('scenarioo.services').service('Config', function (ConfigResource, $rootScope) {

    var CONFIG_LOADED_EVENT = 'configLoaded';

    var configData = {};

    function getValue(key) {
        return configData[key];
    }

    function doLoad() {
        ConfigResource.get({}, function (response) {
            configData = response;
            $rootScope.buildStateToClassMapping = configData.buildstates;
            $rootScope.getStatusStyleClass = function(buildStatus) {
                var styleClassFromMapping = $rootScope.buildStateToClassMapping[buildStatus];
                if (angular.isUndefined(styleClassFromMapping)) {
                    return 'label-warning';
                }
                else {
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
        var stringValue =  getValue('scenarioPropertiesInOverview');

        var propertiesStringArray = [];
        if(angular.isString(stringValue) && stringValue.length > 0) {
            propertiesStringArray = stringValue.split(',');
        }

        var properties = new Array(propertiesStringArray.length);

        for (var i = 0; i < propertiesStringArray.length; i++) {
            properties[i] = propertiesStringArray[i].trim();
        }

        return properties;
    }

    var serviceInstance = {
        CONFIG_LOADED_EVENT: CONFIG_LOADED_EVENT,

        getRawConfigDataCopy: function () {
            return angular.copy(configData);
        },

        /**
         * Will fire event 'configLoaded'
         */
        load: function () {
            doLoad();
        },

        isLoaded: function () {
            return angular.isDefined(configData.defaultBuildName);
        },

        updateConfiguration: function (newConfig, successCallback) {
            newConfig.$save(function () {
                if (successCallback) {
                    doLoad();
                    successCallback();
                }
            });
        },

        defaultBranchAndBuild: function() {
            return {
                branch: getValue('defaultBranchName'),
                build: getValue('defaultBuildName')
            };
        },

        scenarioPropertiesInOverview: function () {
            return getScenarioPropertiesInOverview();
        },

        applicationName: function () {
            return getValue('applicationName');
        },

        applicationInformation: function () {
            return getValue('applicationInformation');
        },

        buildStateToClassMapping: function () {
            return getBuildStateToClassMapping();
        },

        expandPagesInScenarioOverview: function() {
            return getValue('expandPagesInScenarioOverview');
        }
    };

    return serviceInstance;
});