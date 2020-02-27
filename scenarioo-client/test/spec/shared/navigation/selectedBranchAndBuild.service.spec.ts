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

import {Observable, of, ReplaySubject} from 'rxjs';
import {IConfiguration} from '../../../../app/generated-types/backend-types';
import {SelectedBranchAndBuildService} from '../../../../app/shared/navigation/selectedBranchAndBuild.service';
import {RoutingWrapperService} from '../../../../app/shared/routing-wrapper.service';

declare var angular: angular.IAngularStatic;

describe('SelectedBranchAndBuildService', () => {

    let selectedBranchAndBuildService;
    let ConfigurationService;
    let localStorageService;
    let routingWrapperService;
    let $location;
    let $rootScope;
    const BRANCH_COOKIE = 'branch_cookie';
    const BUILD_COOKIE = 'build_cookie';
    const BRANCH_URL = 'branch_url';
    const BUILD_URL = 'build_url';
    const BRANCH_CONFIG = 'branch_config';
    const BUILD_CONFIG = 'build_config';

    const DUMMY_CONFIG_RESPONSE = {
        defaultBuildName: BUILD_CONFIG,
        defaultBranchName: BRANCH_CONFIG,
        scenarioPropertiesInOverview: 'userProfile, configuration',
        applicationInformation: 'This is my personal copy of Scenarioo :-)',
        buildstates: {
            BUILD_STATE_FAILED: 'label-important',
            BUILD_STATE_SUCCESS: 'label-success',
            BUILD_STATE_WARNING: 'label-warning',
        },
    };
    const ConfigResourceMock = {
        get: () => of(DUMMY_CONFIG_RESPONSE),
    };
    const ConfigurationServiceMock = {
        configuration : new ReplaySubject<IConfiguration>(1),
        _config : {
            defaultBuildName: undefined,
            defaultBranchName: undefined},

        getConfiguration(): Observable<IConfiguration> {
            return this.configuration.asObservable();
        },
        updateConfiguration(): void {
            this._config = DUMMY_CONFIG_RESPONSE;
            this.configuration.next(DUMMY_CONFIG_RESPONSE);
        },
        defaultBranchAndBuild() {
            return {
                branch: this._config.defaultBranchName,
                build: this._config.defaultBuildName,
            };
        },
    };
    const LocalStorageServiceMock = {
        get(): string | null {
            return null;
        },
        set(key: string, value: string): void {

        },
    };

    beforeEach(angular.mock.module('scenarioo.services'));
    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.
        $provide.value('ConfigResource', ConfigResourceMock);
        $provide.value('ConfigurationService', ConfigurationServiceMock);

        $provide.value('LocalStorageService', LocalStorageServiceMock);
    }));

    beforeEach(inject((_ConfigurationService_, _LocalStorageService_, _$location_, _$rootScope_) => {
        ConfigurationService = _ConfigurationService_;
        localStorageService = _LocalStorageService_;
        $location = _$location_;
        $rootScope = _$rootScope_;

        routingWrapperService = new RoutingWrapperService($location, $rootScope);

        $location.url('/new/path/');

        selectedBranchAndBuildService = new SelectedBranchAndBuildService(localStorageService, ConfigurationService, routingWrapperService);
    }));

    describe('when the config is not yet loaded', () => {
        it('has undefined values if no cookies or url parameters are set', () => {
            spyOn(localStorageService, 'get').and.returnValue(null);
            expect(selectedBranchAndBuildService.selected()[selectedBranchAndBuildService.BRANCH_KEY]).toBeUndefined();
            expect(selectedBranchAndBuildService.selected()[selectedBranchAndBuildService.BUILD_KEY]).toBeUndefined();
        });

        it('has the cookie values if cookies are set', () => {
            spyOn(localStorageService, 'get').and.returnValues(BRANCH_COOKIE, BUILD_COOKIE);

            const selectedBranchAndBuild = selectedBranchAndBuildService.selected();

            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_COOKIE);
        });

        it('has the url parameter values, if cookies and url parameters are set', () => {
            spyOn(localStorageService, 'set');
            setBranchAndBuildInUrlParameters();

            const selectedBranchAndBuild = selectedBranchAndBuildService.selected();

            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
            expect(localStorageService.set).toHaveBeenCalledWith(selectedBranchAndBuildService.BRANCH_KEY, BRANCH_URL);
            expect(localStorageService.set).toHaveBeenCalledWith(selectedBranchAndBuildService.BUILD_KEY, BUILD_URL);
        });
    });

    describe('when the config is loaded', () => {
        // TODO: Works in isolation, but not if run with other tests
        xit('uses the default values from the configuration, if no cookies or url parameters are set', () => {
            // branchAndBuildInLocalStorageIsNotSet();
            spyOn(localStorageService, 'set');
            branchAndBuildInUrlParametersIsNotSet();

            loadConfigFromService();
            const selectedBranchAndBuild = selectedBranchAndBuildService.selected();

            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_CONFIG);
            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_CONFIG);
            expect(localStorageService.set).toHaveBeenCalledWith(selectedBranchAndBuildService.BRANCH_KEY, BRANCH_CONFIG);
            expect(localStorageService.set).toHaveBeenCalledWith(selectedBranchAndBuildService.BUILD_KEY, BUILD_CONFIG);
            expect($location.search()[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_CONFIG);
            expect($location.search()[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_CONFIG);
        });

        // TODO: Works in isolation, but not if run with other tests
        xit('uses the cookie values if they were already set, but only because there are no url parameters set', () => {
            spyOn(localStorageService, 'get').and.returnValues(BRANCH_COOKIE, BUILD_COOKIE);

            loadConfigFromService();
            const selectedBranchAndBuild = selectedBranchAndBuildService.selected();

            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_COOKIE);
            expect($location.search()[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect($location.search()[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_COOKIE);
        });

        it('uses the url parameter values if they are set, with priority over the cookie values', () => {
            spyOn(localStorageService, 'set');
            spyOn(localStorageService, 'get').and.returnValues(BRANCH_COOKIE, BUILD_COOKIE);
            setBranchAndBuildInUrlParameters();
            loadConfigFromService();

            const selectedBranchAndBuild = selectedBranchAndBuildService.selected();

            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
            expect($location.search()[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect($location.search()[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
        });
    });

    describe('when url parameter changes', () => {
        it('updates the selection', () => {
            spyOn(localStorageService, 'get').and.returnValue(null);
            setBranchAndBuildInUrlParameters();
            $rootScope.$apply();

            const selectedBranchAndBuild = selectedBranchAndBuildService.selected();

            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(selectedBranchAndBuild[selectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
        });
    });

    describe('when branch and build selection changes to a new valid state', () => {
        // TODO: works in isolation, but not if run with the other tests.
        xit('all registered callbacks are called', (done: DoneFn) => {
            branchAndBuildInUrlParametersIsNotSet();

            let selectedFromCallback;

            selectedBranchAndBuildService.callOnSelectionChange((selected) => {
                selectedFromCallback = selected;
                expect(selectedFromCallback.branch).toBe(BRANCH_URL);
                expect(selectedFromCallback.build).toBe(BUILD_URL);
                done();
            });

            selectedBranchAndBuildService.selected(); // nothing should change here

            expect(selectedFromCallback).toBeUndefined();

            // TODO: Doesn't work with calling $rootScope.$apply() two times
            /*$location.url('/new/path/?branch=' + BRANCH_URL); // still nothing should change, because build is missing
            $rootScope.$apply();

            expect(selectedFromCallback).toBeUndefined();*/

            setBranchAndBuildInUrlParameters();
            $rootScope.$apply();
        });
    });

    describe('when a callback is registered and valid data is already available', () => {
        it('calls the callback immediately', (done: DoneFn) => {
            spyOn(localStorageService, 'get').and.returnValue(null);

            $location.url('/new/path/?branch=' + BRANCH_URL + '&build=' + BUILD_URL);
            $rootScope.$apply();

            let selectedFromCallback;

            selectedBranchAndBuildService.callOnSelectionChange((selected) => {
                selectedFromCallback = selected;
                expect(selectedFromCallback.branch).toBe(BRANCH_URL);
                expect(selectedFromCallback.build).toBe(BUILD_URL);
                done();
            });

        });
    });

    function setBranchAndBuildInCookie() {
        localStorageService.set('branch', BRANCH_COOKIE);
        localStorageService.set('build', BUILD_COOKIE);
    }

    function setBranchAndBuildInUrlParameters() {
        $location.url('/new/path/?branch=' + BRANCH_URL + '&build=' + BUILD_URL);
    }

    function loadConfigFromService() {
        ConfigurationService.updateConfiguration();
    }

    function branchAndBuildInUrlParametersIsNotSet() {
        expect($location.search()[selectedBranchAndBuildService.BRANCH_KEY]).toBeUndefined();
        expect($location.search()[selectedBranchAndBuildService.BUILD_KEY]).toBeUndefined();
    }

});
