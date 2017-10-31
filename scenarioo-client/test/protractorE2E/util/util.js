'use strict';

var scenarioo = require('scenarioo-js');

var initialized = false;

function getRoute(route) {
    var flow = protractor.promise.controlFlow();
    var url = browser.params.baseUrl + '/#' + route;
    flow.execute(function() {
        console.log('Going to navigate to route ' + url);
    });
    browser.get(url);
    flow.execute(function() {
        console.log('Going to wait for Angular');
    });
    browser.waitForAngular();
    flow.execute(function() {
        console.log('Found Angular');
    });
}

/**
 * Initialize local storage to previously visited for web tests to run without about dialog open.
 */
function initLocalStorage() {
    console.log('Initializing local storage for user revisiting scenarioo again');
    getRoute('/');
    step('Clearing local storage');
    var setPreviouslyVisitedInLocalStorage = browser.executeScript(function () {
        var injector = angular.element(document.body).injector();
        var LocalStorageService = injector.get('LocalStorageService');
        LocalStorageService.set('scenariooPreviouslyVisited', 'true');
        LocalStorageService.set('scenarioo-searchIncludeHtml', 'false');
    });
    setPreviouslyVisitedInLocalStorage.then(function () {
        var visited = browser.executeScript(function () {
            var injector = angular.element(document.body).injector();
            var LocalStorageService = injector.get('LocalStorageService');
            return LocalStorageService.get('scenariooPreviouslyVisited');
        });
        expect(visited).toEqual('true');
    });
}

/**
 * Initialize local storage to previously visited for web tests to run without about dialog open (only if not previously intialized allready).
 */
function initLocalStorageIfRequired() {
    if (!initialized) {
        initLocalStorage();
        initialized = true;
    }
}

/**
 * Clear local storage to not previously visited for web tests to run with about dialog open.
 *
 * To realy see the dialog, a restart of the application is needed after (e.g. by page refresh!), this method only clears the storage.
 */
function clearLocalStorage() {
    var flow = protractor.promise.controlFlow();
    flow.execute(function() {
        console.log('Clear local storage for user visiting for the first time');
    });
    getRoute('/');
    var clearLocalStorageScript = browser.executeScript(function () {
        var injector = angular.element(document.body).injector();
        var LocalStorageService = injector.get('LocalStorageService');
        return LocalStorageService.clearAll();
    });
    flow.execute(function() {
        console.log('Cleared local storage');
    });
    clearLocalStorageScript.then(function () {
        var visited = browser.executeScript(function () {
            var injector = angular.element(document.body).injector();
            var LocalStorageService = injector.get('LocalStorageService');
            return LocalStorageService.get('scenariooPreviouslyVisited');
        });
        expect(visited).toEqual(null);
    });
    flow.execute(function() {
        console.log('Asserted local storage');
    });
}

var e2eUtils = {

    initLocalStorage: initLocalStorageIfRequired,

    clearLocalStorage: clearLocalStorage,

    getRoute: getRoute,

    assertRoute: function (route) {
        browser.getCurrentUrl().then(function (url) {
            var currentUrlWithoutSearch = url.split('?')[0];
            expect(currentUrlWithoutSearch).toBe(browser.params.baseUrl + '/#' + route);
        });
    },

    assertRouteNot: function (route) {
        expect(browser.getCurrentUrl()).not.toBe(browser.params.baseUrl + '/#' + route);
    },

    assertTextPresentInElement: function (element, expectedText) {
        expect(element.getText()).toContain(expectedText);
    },

    assertElementNotPresentInDom: function (by) {
        browser.findElements(by).then(function (elements) {
            expect(elements.length).toBe(0);
        });
    },

    generateRandomString: function (length) {
        return Math.random().toString(36).replace(/[^a-z]+/g, '').substring(0, length);
    },

    selectOption: function (selector, item) {
        var selectList, desiredOption;

        selectList = element(selector);
        selectList.click();

        selectList.findElements(by.tagName('option'))
            .then(function findMatchingOption(options) {
                options.some(function (option) {
                    option.getText().then(function doesOptionMatch(text) {
                        if (item === text) {
                            desiredOption = option;
                            return true;
                        }
                    });
                });
            })
            .then(function clickOption() {
                if (desiredOption) {
                    desiredOption.click();
                }
            });
    },

    clickBrowserBackButton: function () {
        browser.navigate().back();
    },

    refreshBrowser: function () {
        browser.navigate().refresh();
    }

};

module.exports = e2eUtils;
