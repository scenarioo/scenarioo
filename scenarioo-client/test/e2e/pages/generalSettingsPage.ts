'use strict';

import {by, element} from 'protractor';
import * as Utils from '../util';

class GeneralSettingsPage {

    private configuredSearchEndpoint = element(by.id('sc-configured-search-endpoint'));
    private configuredSearchClusterName = element(by.id('sc-configured-search-cluster-name'));
    private searchEndpointIsReachable = element(by.id('sc-search-endpoint-is-reachable'));

    async goToPage() {
        return Utils.navigateToRoute('/manage?tab=configuration');
    }

    async assertSearchEndpointConfiguredAndReachable() {
        await expect(this.configuredSearchEndpoint.getText()).toBe('localhost:9300');
        await expect(this.configuredSearchClusterName.getText()).toBe('elasticsearch');
        await expect(this.searchEndpointIsReachable.isDisplayed()).toBeTruthy();
    }

}

export default new GeneralSettingsPage();
