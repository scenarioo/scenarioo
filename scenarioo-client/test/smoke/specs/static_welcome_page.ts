'use strict';

import { scenario, step, useCase } from 'scenarioo-js';
import {browser, by, element} from 'protractor';

useCase('Webserver running')
    .description('The webserver started successfully.')
    .describe(() => {

        scenario('Visit Smoketest')
            .description('The User sees the static smoketest page.')
            .it(async () => {
                await browser.waitForAngularEnabled(false);
                await browser.get(browser.params.baseUrl + "/smoketest.html");
                const h1Element = element(by.tagName("h1"));
                await expect(h1Element.getText()).toContain("Welcome to Scenarioo");
            });

    });
