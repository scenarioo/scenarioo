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

import * as angular from "angular";
import {Observable} from "rxjs";

describe('GeneralSettingsController', () => {

    let $rootScope, $controller, ConfigService, $httpBackend, $scope, ConfigCtrl, TestData;

    let BranchResourceMock = {
        query: () => {
        }
    };
    let SearchEngineStatusMock = {
        isSearchEngineRunning: () => {
        }
    };
    let ApplicationStatusMock = {
        getApplicationStatus: () => {
        }
    };
    let ConfigResourceMock = {
        get: () => {
        }
    };


    beforeEach(angular.mock.module('scenarioo.controllers'));
    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.

        $provide.value("BranchesResource", BranchResourceMock);

        $provide.value("SearchEngineStatusService", SearchEngineStatusMock);
        $provide.value("ApplicationStatusService", ApplicationStatusMock);
        $provide.value("ConfigResource", ConfigResourceMock);
    }));

    beforeEach(inject((_$rootScope_, _$controller_,
                       _SearchEngineStatusService_,
                       _ApplicationStatusService_,
                       _ConfigService_, _$httpBackend_, _TestData_) => {
            $rootScope = _$rootScope_;
            $controller = _$controller_;
            ConfigService = _ConfigService_;
            $httpBackend = _$httpBackend_;
            TestData = _TestData_;

            spyOn(BranchResourceMock, 'query')
                .and.returnValue(Observable.of(TestData.BRANCHES));
            spyOn(SearchEngineStatusMock, 'isSearchEngineRunning')
                .and.returnValue(Observable.of({'searchEngineRunning': false}));
            spyOn(ApplicationStatusMock, 'getApplicationStatus')
                .and.returnValue(Observable.of({
                'searchEngineRunning': false,
                'version': TestData.VERSION,
                'configuration': angular.copy(TestData.CONFIG)
            }));

            spyOn(ConfigResourceMock, 'get')
                .and.returnValue(Observable.of(angular.copy(TestData.CONFIG)));

            $httpBackend.whenGET('rest/version').respond(TestData.VERSION);
            $httpBackend.whenGET('rest/branch/branch_123/build/build_123/searchEngine').respond(404, false);

            $scope = $rootScope.$new();
            ConfigCtrl = $controller('GeneralSettingsController', {
                $scope: $scope,
                ConfigService: ConfigService
            });
            ConfigService.load();
        }
    ))
    ;

    describe('when page is loaded', () => {
        it('loads and displays the config from the server', () => {
            expect(ConfigCtrl).toBeDefined();

            expect(ConfigCtrl.configuration).toEqual(TestData.CONFIG);
        });

        it('loads all branches and builds', () => {
            expect(ConfigCtrl.branches.length).toEqual(3);
            expect(ConfigCtrl.configuredBranch.branch.name).toEqual('trunk');
        });
    });

    describe('when reset button is clicked', () => {
        it('resets the config to the loaded values', () => {
            changeAllValues();

            ConfigCtrl.resetConfiguration();

            expect(ConfigCtrl.configuration).toEqual(TestData.CONFIG);
        });
    });

    describe('when the save button is clicked', () => {
        it('saves the edited config', () => {
            spyOn(ConfigService, 'updateConfiguration');

            changeAllValues();

            ConfigCtrl.updateConfiguration();

            expect(ConfigService.updateConfiguration).toHaveBeenCalled();
        });
    });

    function changeAllValues() {
        ConfigCtrl.configuration.defaultBuildName = 'new build';
        ConfigCtrl.configuration.defaultBranchName = 'new branch';
        ConfigCtrl.configuration.scenarioPropertiesInOverview = 'abc';
        ConfigCtrl.configuration.applicationInformation = 'new information';
        ConfigCtrl.configuration.testDocumentationDirPath = 'new path';
    }

});
