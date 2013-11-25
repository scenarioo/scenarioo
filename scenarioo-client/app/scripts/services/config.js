
'use strict';

angular.module('scenarioo.services').service('Config', function (ConfigResource, $rootScope) {

    var CONFIG_LOADED_EVENT = 'configLoaded';

    var configData = {};

    function getValue(key) {
        if (angular.isUndefined(configData[key])) {
            // TODO fix this, should not have commented out code
            // throw 'scenariooError :: Key ' + key + ' not present in configData';
        }
        return configData[key];
    }

    function doLoad() {
        ConfigResource.get({}, function (response) {
            configData = response;
            $rootScope.buildStateToClassMapping = configData.buildstates;
            $rootScope.$broadcast(CONFIG_LOADED_EVENT);
        });
    }

    function getBuildStateToClassMapping() {
        return configData.buildstates;
    }

    function getScenarioPropertiesInOverview() {
        var stringValue =  getValue('scenarioPropertiesInOverview');

        var propertiesStringArray = [];
        if(angular.isString(stringValue)) {
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
            var configToStore = angular.copy(newConfig);

            ConfigResource.save({}, configToStore, function () {
                if (successCallback) {
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

        applicationInformation: function () {
            return getValue('applicationInformation');
        },

        buildStateToClassMapping: function () {
            return getBuildStateToClassMapping();
        }

    };

    return serviceInstance;
});