'use strict';


var initialized = false;

function getRoute(route) {
    browser.get(browser.params.baseUrl + '/#' + route);
    browser.waitForAngular();
}

/**
 * Initialize local storage to previously visited for web tests to run without about dialog open.
 */
function initLocalStorage() {
    console.log('Initializing local storage for user revisiting scenarioo again');
    getRoute('/');
    var setPreviouslyVisitedInLocalStorage = browser.executeScript(function() {
        var injector = angular.element(document.body).injector();
        var scLocalStorage = injector.get('scLocalStorage');
        scLocalStorage.set('scenariooPreviouslyVisited', 'true');
    });
    setPreviouslyVisitedInLocalStorage.then(function () {
        var visited = browser.executeScript(function() {
            var injector = angular.element(document.body).injector();
            var scLocalStorage = injector.get('scLocalStorage');
            return scLocalStorage.get('scenariooPreviouslyVisited');
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
    console.log('Clear local storage for user visiting for the first time');
    getRoute('/');
    var clearLocalStorageScript = browser.executeScript(function() {
        var injector = angular.element(document.body).injector();
        var scLocalStorage = injector.get('scLocalStorage');
        return scLocalStorage.clearAll();
    });
    clearLocalStorageScript.then(function() {
        var visited = browser.executeScript(function() {
            var injector = angular.element(document.body).injector();
            var scLocalStorage = injector.get('scLocalStorage');
            return scLocalStorage.get('scenariooPreviouslyVisited');
        });
        expect(visited).toBe(null);
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

    refreshBrowser: function() {
        browser.navigate().refresh();
    }

};

module.exports = e2eUtils;
