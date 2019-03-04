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

import {Observable} from "rxjs";
import * as angular from "angular";

describe('SelectedBranchAndBuildService', () => {

    let SelectedBranchAndBuildService, ConfigService, LocalStorageService,
        $location, $rootScope;
    const BRANCH_COOKIE = 'branch_cookie';
    const BUILD_COOKIE = 'build_cookie';
    const BRANCH_URL = 'branch_url';
    const BUILD_URL = 'build_url';
    const BRANCH_CONFIG = 'branch_config';
    const BUILD_CONFIG = 'build_config';

    const DUMMY_CONFIG_RESPONSE = {
        'defaultBuildName': BUILD_CONFIG,
        'defaultBranchName': BRANCH_CONFIG,
        'scenarioPropertiesInOverview': 'userProfile, configuration',
        'applicationInformation': 'This is my personal copy of Scenarioo :-)',
        'buildstates': {
            BUILD_STATE_FAILED: 'label-important',
            BUILD_STATE_SUCCESS: 'label-success',
            BUILD_STATE_WARNING: 'label-warning'
        }
    };
    let ConfigResourceMock = {
        get: () => Observable.of(DUMMY_CONFIG_RESPONSE)
    };

    beforeEach(angular.mock.module('scenarioo.services'));
    beforeEach(angular.mock.module('scenarioo.services', ($provide) => {
        // TODO: Remove after AngularJS Migration.
        $provide.value("ConfigResource", ConfigResourceMock);
    }));

    beforeEach(inject((_SelectedBranchAndBuildService_, _ConfigService_,
                       _LocalStorageService_, _$location_, _$rootScope_) => {
        SelectedBranchAndBuildService = _SelectedBranchAndBuildService_;
        ConfigService = _ConfigService_;
        LocalStorageService = _LocalStorageService_;

        $location = _$location_;
        $rootScope = _$rootScope_;
    }));

    it('has undefined branch and build cookies by default', () => {
        branchAndBuildInLocalStorageIsNotSet();
    });

    describe('when the config is not yet loaded', () => {
        it('has undefined values if no cookies or url parameters are set', () => {
            LocalStorageService.clearAll();
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]).toBeUndefined();
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]).toBeUndefined();
        });

        it('has the cookie values if cookies are set', () => {
            setBranchAndBuildInCookie();

            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_COOKIE);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BRANCH_KEY)).toBe(BRANCH_COOKIE);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BUILD_KEY)).toBe(BUILD_COOKIE);
        });

        it('has the url parameter values, if cookies and url parameters are set', () => {
            setBranchAndBuildInCookie();
            setBranchAndBuildInUrlParameters();

            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BRANCH_KEY)).toBe(BRANCH_URL);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BUILD_KEY)).toBe(BUILD_URL);
        });
    });

    describe('when the config is loaded', () => {
        it('uses the default values from the configuration, if no cookies or url parameters are set', () => {
            branchAndBuildInLocalStorageIsNotSet();
            branchAndBuildInUrlParametersIsNotSet();

            loadConfigFromService();

            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_CONFIG);
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_CONFIG);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BRANCH_KEY)).toBe(BRANCH_CONFIG);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BUILD_KEY)).toBe(BUILD_CONFIG);
            expect($location.search()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_CONFIG);
            expect($location.search()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_CONFIG);
        });

        it('uses the cookie values if they were already set, but only because there are no url parameters set', () => {
            setBranchAndBuildInCookie();

            loadConfigFromService();

            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_COOKIE);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BRANCH_KEY)).toBe(BRANCH_COOKIE);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BUILD_KEY)).toBe(BUILD_COOKIE);
            expect($location.search()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect($location.search()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_COOKIE);
        });

        it('uses the url parameter values if they are set, with priority over the cookie values', () => {
            setBranchAndBuildInCookie();
            setBranchAndBuildInUrlParameters();

            loadConfigFromService();

            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BRANCH_KEY)).toBe(BRANCH_URL);
            expect(LocalStorageService.get(SelectedBranchAndBuildService.BUILD_KEY)).toBe(BUILD_URL);
            expect($location.search()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect($location.search()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
        });
    });

    describe('when url parameter changes', () => {
        it('updates the selection', () => {
            branchAndBuildInLocalStorageIsNotSet();
            branchAndBuildInUrlParametersIsNotSet();

            setBranchAndBuildInUrlParameters();

            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(SelectedBranchAndBuildService.selected()[SelectedBranchAndBuildService.BUILD_KEY]).toBe(BUILD_URL);
        });
    });

    describe('when branch and build selection changes to a new valid state', () => {
        it('all registered callbacks are called', () => {
            branchAndBuildInLocalStorageIsNotSet();
            branchAndBuildInUrlParametersIsNotSet();

            let selectedFromCallback;

            SelectedBranchAndBuildService.callOnSelectionChange(selected => {
                selectedFromCallback = selected;
            });

            expect(selectedFromCallback).toBeUndefined();

            SelectedBranchAndBuildService.selected(); // nothing should change here

            expect(selectedFromCallback).toBeUndefined();

            $location.url('/new/path/?branch=' + BRANCH_URL); // still nothing should change, because build is missing
            $rootScope.$apply();

            expect(selectedFromCallback).toBeUndefined();

            $location.url('/new/path/?branch=' + BRANCH_URL + '&build=' + BUILD_URL);
            $rootScope.$apply();

            expect(selectedFromCallback.branch).toBe(BRANCH_URL);
            expect(selectedFromCallback.build).toBe(BUILD_URL);
        });
    });

    describe('when a callback is registered and valid data is already available', () => {
        it('calls the callback immediately', () => {
            branchAndBuildInLocalStorageIsNotSet();
            branchAndBuildInUrlParametersIsNotSet();

            $location.url('/new/path/?branch=' + BRANCH_URL + '&build=' + BUILD_URL);
            $rootScope.$apply();

            let selectedFromCallback;

            SelectedBranchAndBuildService.callOnSelectionChange(selected => {
                selectedFromCallback = selected;
            });

            // here no further change happens, but the callback was called anyway (immediately when it was registered).

            expect(selectedFromCallback.branch).toBe(BRANCH_URL);
            expect(selectedFromCallback.build).toBe(BUILD_URL);
        });
    });

    function setBranchAndBuildInCookie() {
        LocalStorageService.set('branch', BRANCH_COOKIE);
        LocalStorageService.set('build', BUILD_COOKIE);
    }

    function setBranchAndBuildInUrlParameters() {
        $location.url('/new/path/?branch=' + BRANCH_URL + '&build=' + BUILD_URL);
    }

    function loadConfigFromService() {
        ConfigService.load();
    }

    function branchAndBuildInLocalStorageIsNotSet() {
        LocalStorageService.clearAll();
        expect(LocalStorageService.get(SelectedBranchAndBuildService.BRANCH_KEY)).toBeNull();
        expect(LocalStorageService.get(SelectedBranchAndBuildService.BUILD_KEY)).toBeNull();
    }

    function branchAndBuildInUrlParametersIsNotSet() {
        expect($location.search()[SelectedBranchAndBuildService.BRANCH_KEY]).toBeUndefined();
        expect($location.search()[SelectedBranchAndBuildService.BUILD_KEY]).toBeUndefined();
    }

});
