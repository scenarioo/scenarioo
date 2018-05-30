'use strict';

import {browser, by, element, protractor} from 'protractor';

// Just to make TypeScript happy
declare var angular: any;

export async function navigateToRoute(route) {
    await browser.get(browser.params.baseUrl + '/#' + route);
    await browser.waitForAngular();
}

/**
 * Initialize local storage to "previously visited" for web tests to run without about dialog open.
 */
export async function startScenariooRevisited() {
    await navigateToRoute('/');
    await browser.executeScript(() => {
        const injector = angular.element(document.body).injector();
        const LocalStorageService: any = injector.get('LocalStorageService');
        LocalStorageService.clearAll();
        LocalStorageService.set('scenariooPreviouslyVisited', 'true');
    });
    const visited = await browser.executeScript(() => {
        const injector = angular.element(document.body).injector();
        const LocalStorageService: any = injector.get('LocalStorageService');
        return LocalStorageService.get('scenariooPreviouslyVisited');
    });
    await expect(visited).toEqual('true');
}

/**
 * Start Scenarioo as a user that has never visited it before (visited cookie will not be set).
 */
export async function startScenariooFirstTimeVisit() {
    await clearLocalStorage();
    await refreshBrowser(); // reload needed to restart without cookies.
}

/**
 * Clear local storage to to simulate "first time user" for web tests to run with about dialog open.
 *
 * To really see the dialog, a restart of the application is needed after (e.g. by page refresh!),
 * this method only clears the storage.
 */
export async function clearLocalStorage() {
    await navigateToRoute('/');
    await browser.executeScript(() => {
        const injector = angular.element(document.body).injector();
        const LocalStorageService: any = injector.get('LocalStorageService');
        return LocalStorageService.clearAll();
    });
    const visited = await browser.executeScript(() => {
        const injector = angular.element(document.body).injector();
        const LocalStorageService: any = injector.get('LocalStorageService');
        return LocalStorageService.get('scenariooPreviouslyVisited');
    });
    await expect(visited).toBe(null);
}

export async function assertRoute(route) {
    const url = await browser.getCurrentUrl();
    const currentUrlWithoutSearch = url.split('?')[0];
    await expect(currentUrlWithoutSearch).toBe(browser.params.baseUrl + '/#' + route);
}

export async function assertPageIsDisplayed(path: string) {
    return assertRoute(path);
}

export async function assertTextPresentInElement(element, expectedText) {
    await expect(await element.getText()).toContain(expectedText);
}

export async function assertElementNotPresentInDom(by) {
    const elements = await browser.findElements(by);
    await expect(await elements.length).toBe(0);
}

export async function clickBrowserBackButton() {
    return browser.navigate().back();
}

export async function refreshBrowser() {
    return browser.navigate().refresh();
}

export async function assertElementIsEnabled(elementId) {
    return expect(element(by.id(elementId)).getAttribute('disabled')).toBeFalsy();
}

export async function assertElementIsDisabled(elementId) {
    return expect(element(by.id(elementId)).getAttribute('disabled')).toBeTruthy();
}

export async function clickElementById(elementId) {
    return element(by.id(elementId)).click();
}

export async function type(value) {
    return element(by.css('body')).sendKeys(value);
}

export async function waitForElementVisible(e) {
    const EC = protractor.ExpectedConditions;
    await browser.wait(EC.visibilityOf(e), 5000);
    return expect(e.isDisplayed()).toBeTruthy();
}

export async function assertNumberOfTableRows(tableElement, expectedNumer) {
    return expect(tableElement.all(by.css('tbody tr')).count()).toBe(expectedNumer);
}
