'use strict';

import { by, element, ElementFinder } from 'protractor';
import * as Utils from '../util';

class GeneralSettingsPage {

    private searchNotConfiguredMessage = element(by.id('sc-search-not-configured-message'));
    private configuredSearchEndpoint = element(by.id('sc-configured-search-endpoint'));
    private configuredSearchClusterName = element(by.id('sc-configured-search-cluster-name'));
    private searchEndpointIsReachable = element(by.id('sc-search-endpoint-is-reachable'));
    private searchEndpointIsNotReachable = element(by.id('sc-search-endpoint-is-not-reachable'));

    async goToPage() {
        return Utils.navigateToRoute('/manage?tab=configuration');
    }

    async assertSearchEndpointConfiguredAndReachable() {
        await expect(this.searchNotConfiguredMessage.isDisplayed()).toBeFalsy();
        await expect(this.configuredSearchEndpoint.getText()).toBe('localhost:9305');
        await expect(this.configuredSearchClusterName.getText()).toBe('elasticsearch');
        await expect(this.searchEndpointIsNotReachable.isDisplayed()).toBeFalsy();
        await expect(this.searchEndpointIsReachable.isDisplayed()).toBeTruthy();
    }

}

export default new GeneralSettingsPage();
