'use strict';


var initialized = false;

function getRoute(route) {
    browser.get(browser.params.baseUrl + '/#' + route);
    browser.waitForAngular();
}

function initLocalStorage() {
    console.log('Initializing local storage');
    getRoute('/');
    var clearLocalStorage = browser.executeScript('localStorage.setItem("ls.scenariooPreviouslyVisited", "true");');
    clearLocalStorage.then(function () {
        var visited = browser.executeScript('return localStorage.getItem("ls.scenariooPreviouslyVisited");');
        expect(visited).toEqual('true');
    });
}

function initLocalStorageIfRequired() {
    if (!initialized) {
        initLocalStorage();
        initialized = true;
    }
}


var e2eUtils = {

    initLocalStorage: initLocalStorageIfRequired,

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
    }

};

module.exports = e2eUtils;
