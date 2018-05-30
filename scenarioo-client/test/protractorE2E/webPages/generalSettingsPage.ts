'use strict';

import { by, element, ElementFinder } from 'protractor';
import * as Utils from '../util/util';

export default class GeneralSettingsPage {

    private static searchNotConfiguredMessage: ElementFinder = element(by.id('sc-search-not-configured-message'));
    private static configuredSearchEndpoint: ElementFinder = element(by.id('sc-configured-search-endpoint'));
    private static searchEndpointIsReachable: ElementFinder = element(by.id('sc-search-endpoint-is-reachable'));
    private static searchEndpointIsNotReachable: ElementFinder = element(by.id('sc-search-endpoint-is-not-reachable'));

    static async goToPage() {
        return Utils.navigateToRoute('/manage?tab=configuration');
    }

    static async assertSearchEndpointConfiguredAndReachable() {
        await expect(this.searchNotConfiguredMessage.isDisplayed()).toBeFalsy();
        await expect(this.configuredSearchEndpoint.getText()).toBe('localhost:9300');
        await expect(this.searchEndpointIsNotReachable.isDisplayed()).toBeFalsy();
        return expect(this.searchEndpointIsReachable.isDisplayed()).toBeTruthy();
    }

}
