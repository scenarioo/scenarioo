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

'use strict';

describe('Service :: Config', function () {

    var HostnameAndPort;

    var BUILD_STATE_FAILED = 'failed',
        BUILD_STATE_SUCCESS = 'success',
        BUILD_STATE_WARNING = 'warning',
        DUMMY_CONFIG_RESPONSE = {
            'dataDirectory': 'webtestDocuContentExample',
            'configuration': {
                'defaultBuildName': 'current',
                'scenarioPropertiesInOverview': 'userProfile, configuration',
                'applicationInformation': 'This is my personal copy of Scenarioo :-)',
                'buildstates': {
                    BUILD_STATE_FAILED: 'label-important',
                    BUILD_STATE_SUCCESS: 'label-success',
                    BUILD_STATE_WARNING: 'label-warning'
                },
                'defaultBranchName': 'trunk'
            }
        };

    beforeEach(angular.mock.module('scenarioo.services'));

    beforeEach(inject(function(_HostnameAndPort_) {
        HostnameAndPort = _HostnameAndPort_;
    }));

    it('should inject Config', inject(function (Config) {
        expect(Config).not.toBeUndefined();
    }));

    it('should be able to load config from server', inject(function (Config, $rootScope, $httpBackend) {
        spyOn($rootScope, '$broadcast').and.callThrough();

        loadConfigFromService(Config, $httpBackend);

        expect($rootScope.$broadcast).toHaveBeenCalledWith(Config.CONFIG_LOADED_EVENT);

        expect(Config.scenarioPropertiesInOverview()).toBeDefined();
        expect(Config.applicationInformation()).toBe(DUMMY_CONFIG_RESPONSE.configuration.applicationInformation);
    }));

    it('contains build state to css class mapping as a map', inject(function (Config, $httpBackend) {
        loadConfigFromService(Config, $httpBackend);

        var buildStateToClassMapping = Config.buildStateToClassMapping();

        expect(buildStateToClassMapping).toBeDefined();
        expect(getSize(buildStateToClassMapping)).toBe(3);
        expect(buildStateToClassMapping[BUILD_STATE_FAILED]).toBe(DUMMY_CONFIG_RESPONSE.configuration.buildstates[BUILD_STATE_FAILED]);
        expect(buildStateToClassMapping[BUILD_STATE_WARNING]).toBe(DUMMY_CONFIG_RESPONSE.configuration.buildstates[BUILD_STATE_WARNING]);
        expect(buildStateToClassMapping[BUILD_STATE_SUCCESS]).toBe(DUMMY_CONFIG_RESPONSE.configuration.buildstates[BUILD_STATE_SUCCESS]);
    }));

    it('contains additional columns for scenario overview', inject(function (Config, $httpBackend) {
        loadConfigFromService(Config, $httpBackend);

        var columns = Config.scenarioPropertiesInOverview();

        expect(columns).toBeDefined();
        expect(getSize(columns)).toBe(2);
        expect(columns[0]).toBe('userProfile');
        expect(columns[1]).toBe('configuration');
    }));

    function loadConfigFromService(Config, $httpBackend) {
        $httpBackend.when('GET', HostnameAndPort.forTest() + 'rest/configuration').respond(DUMMY_CONFIG_RESPONSE);
        Config.load();
        $httpBackend.flush();
    }

    function getSize(object) {
        var size = 0, key;
        for (key in object) {
            if (object.hasOwnProperty(key)) {
                size++;
            }
        }
        return size;
    }

});
