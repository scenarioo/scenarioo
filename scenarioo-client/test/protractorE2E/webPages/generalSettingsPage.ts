'use strict';
import {by, element, ElementFinder} from "protractor";

var util = require('util');

export default class GeneralSettingsPage {

    private path: string = '/manage?tab=configuration';
    private searchNotConfiguredMessage: ElementFinder = element(by.id('sc-search-not-configured-message'));
    private configuredSearchEndpoint: ElementFinder = element(by.id('sc-configured-search-endpoint'));
    private searchEndpointIsReachable: ElementFinder = element(by.id('sc-search-endpoint-is-reachable'));
    private searchEndpointIsNotReachable: ElementFinder = element(by.id('sc-search-endpoint-is-not-reachable'));

    async assertSearchEndpointConfiguredAndReachable() {
        expect(this.searchNotConfiguredMessage.isDisplayed()).toBeFalsy();
        expect(this.configuredSearchEndpoint.getText()).toBe('localhost:9300');
        expect(this.searchEndpointIsNotReachable.isDisplayed()).toBeFalsy();
        expect(this.searchEndpointIsReachable.isDisplayed()).toBeTruthy();
    };

}
