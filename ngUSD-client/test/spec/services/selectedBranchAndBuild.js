'use strict';

describe('Service: SelectedBranchAndBuild', function () {

    var SelectedBranchAndBuild, Config, $cookieStore, $location, CONFIG_LOADED_EVENT, $rootScope, $httpBackend;
    var BRANCH_COOKIE = 'branch_cookie';
    var BUILD_COOKIE = 'build_cookie';
    var BRANCH_URL = 'branch_url';
    var BUILD_URL = 'build_url';
    var BRANCH_CONFIG = 'branch_config';
    var BUILD_CONFIG = 'build_config';

    var DUMMY_CONFIG_RESPONSE = {
        'defaultBuildName': BUILD_CONFIG,
        'defaultBranchName': BRANCH_CONFIG,
        'testDocumentationDirPath': 'webtestDocuContentExample',
        'scenarioPropertiesInOverview': 'userProfile, configuration',
        'applicationInformation': 'This is my personal copy of Scenarioo :-)',
        'buildstates': {
            BUILD_STATE_FAILED: 'label-important',
            BUILD_STATE_SUCCESS: 'label-success',
            BUILD_STATE_WARNING: 'label-warning'
        }
    };

    beforeEach(angular.mock.module('ngUSDClientApp.services'));

    beforeEach(inject(function(_SelectedBranchAndBuild_, _Config_, _$cookieStore_, _$location_, _$rootScope_, _$httpBackend_) {
        SelectedBranchAndBuild = _SelectedBranchAndBuild_;
        Config = _Config_;
        $cookieStore = _$cookieStore_;
        $location = _$location_;
        $rootScope = _$rootScope_;
        $httpBackend = _$httpBackend_;
    }));

    it('has undefined branch and build cookies by default', function () {
        expect($cookieStore.get(SelectedBranchAndBuild.BRANCH_KEY)).toBeUndefined();
        expect($cookieStore.get(SelectedBranchAndBuild.BUILD_KEY)).toBeUndefined();
    });

    describe('when the config is not yet loaded', function() {
        it('has undefined values if no cookies or url parameters are set', inject(function(SelectedBranchAndBuild) {
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY]).toBeUndefined();
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY]).toBeUndefined();
        }));

        it('has the cookie values if cookies are set', function() {
            setBranchAndBuildInCookie();

            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_COOKIE);
            expect($cookieStore.get(SelectedBranchAndBuild.BRANCH_KEY)).toBe(BRANCH_COOKIE);
            expect($cookieStore.get(SelectedBranchAndBuild.BUILD_KEY)).toBe(BUILD_COOKIE);
        });

        it('has the url parameter values, if cookies and url parameters are set', function() {
            setBranchAndBuildInCookie();
            setBranchAndBuildInUrlParameters();

            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_URL);
            expect($cookieStore.get(SelectedBranchAndBuild.BRANCH_KEY)).toBe(BRANCH_URL);
            expect($cookieStore.get(SelectedBranchAndBuild.BUILD_KEY)).toBe(BUILD_URL);
        });
    });

    describe('when the config is loaded', function() {
        it('uses the default values from the configuration, if no cookies or url parameters are set', function() {
            expectBranchAndBuildInCookieIsNotSet();
            expectBranchAndBuildInUrlParametersIsNotSet();

            loadConfigFromService();

            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_CONFIG);
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_CONFIG);
            expect($cookieStore.get(SelectedBranchAndBuild.BRANCH_KEY)).toBe(BRANCH_CONFIG);
            expect($cookieStore.get(SelectedBranchAndBuild.BUILD_KEY)).toBe(BUILD_CONFIG);
            expect($location.search()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_CONFIG);
            expect($location.search()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_CONFIG);
        });

        it('uses the cookie values if they were already set, but only because there are no url parameters set', function() {
            setBranchAndBuildInCookie();

            loadConfigFromService();

            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_COOKIE);
            expect($cookieStore.get(SelectedBranchAndBuild.BRANCH_KEY)).toBe(BRANCH_COOKIE);
            expect($cookieStore.get(SelectedBranchAndBuild.BUILD_KEY)).toBe(BUILD_COOKIE);
            expect($location.search()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_COOKIE);
            expect($location.search()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_COOKIE);
        });

        it('uses the url parameter values if they are set, with priority over the cookie values', function() {
            setBranchAndBuildInCookie();
            setBranchAndBuildInUrlParameters();

            loadConfigFromService();

            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_URL);
            expect($cookieStore.get(SelectedBranchAndBuild.BRANCH_KEY)).toBe(BRANCH_URL);
            expect($cookieStore.get(SelectedBranchAndBuild.BUILD_KEY)).toBe(BUILD_URL);
            expect($location.search()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_URL);
            expect($location.search()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_URL);
        });
    });

    describe('when url parameter changes', function() {
        it('updates the selection', function() {
            expectBranchAndBuildInCookieIsNotSet();
            expectBranchAndBuildInUrlParametersIsNotSet();

            setBranchAndBuildInUrlParameters();

            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BRANCH_KEY]).toBe(BRANCH_URL);
            expect(SelectedBranchAndBuild.selected()[SelectedBranchAndBuild.BUILD_KEY]).toBe(BUILD_URL);
        });
    });

    describe('when branch and build selection changes to a new valid state', function() {
        it('all registered callbacks are called', function() {
            expectBranchAndBuildInCookieIsNotSet();
            expectBranchAndBuildInUrlParametersIsNotSet();

            var selectedFromCallback;

            SelectedBranchAndBuild.callOnSelectionChange(function(selected) {
                selectedFromCallback = selected;
            });

            expect(selectedFromCallback).toBeUndefined();

            SelectedBranchAndBuild.selected(); // nothing should change here

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

    describe('when a callback is registered and valid data is already available', function() {
       it('calls the callback immediately', function() {
           expectBranchAndBuildInCookieIsNotSet();
           expectBranchAndBuildInUrlParametersIsNotSet();

           $location.url('/new/path/?branch=' + BRANCH_URL + '&build=' + BUILD_URL);
           $rootScope.$apply();

           var selectedFromCallback;

           SelectedBranchAndBuild.callOnSelectionChange(function(selected) {
               selectedFromCallback = selected;
           });

           // here no further change happens, but the callback was called anyway (immediately when it was registered).

           expect(selectedFromCallback.branch).toBe(BRANCH_URL);
           expect(selectedFromCallback.build).toBe(BUILD_URL);
       });
    });

    function setBranchAndBuildInCookie() {
        $cookieStore.put('branch', BRANCH_COOKIE);
        $cookieStore.put('build', BUILD_COOKIE);
    }

    function setBranchAndBuildInUrlParameters() {
        $location.url('/new/path/?branch=' + BRANCH_URL + '&build=' + BUILD_URL);
    }

    function loadConfigFromService() {
        $httpBackend.when('GET', 'http://localhost:8080/ngusd/rest/configuration').respond(DUMMY_CONFIG_RESPONSE);
        Config.load();
        $httpBackend.flush();
    }

    function expectBranchAndBuildInCookieIsNotSet() {
        expect($cookieStore.get(SelectedBranchAndBuild.BRANCH_KEY)).toBeUndefined();
        expect($cookieStore.get(SelectedBranchAndBuild.BUILD_KEY)).toBeUndefined();
    }

    function expectBranchAndBuildInUrlParametersIsNotSet() {
        expect($location.search()[SelectedBranchAndBuild.BRANCH_KEY]).toBeUndefined();
        expect($location.search()[SelectedBranchAndBuild.BUILD_KEY]).toBeUndefined();
    }

});
