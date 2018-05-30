'use strict';

import { by, element, ElementFinder } from "protractor";

export default class BreadcrumbPage {

    private static breadcrumbs: ElementFinder = element(by.css('.breadcrumb'));

    static async clickOnBreadcrumb(breadcrumbId) {
        return this.breadcrumbs.element(by.id(breadcrumbId)).click();
    };

    static async assertBreadcrumbElementText(breadcrumbId, useCaseName) {
        const useCaseElement =  this.breadcrumbs.element(by.id(breadcrumbId));
        return expect(useCaseElement.getText()).toContain(useCaseName);
    };

    static async assertThatTooltipIsShown(toolTipId, toolTipText){
        const toolTipElement =  this.breadcrumbs.element(by.id(toolTipId));
        const toolTipAttribute = await toolTipElement.getAttribute('uib-tooltip');

        return expect(toolTipAttribute).toBe(toolTipText);
    };

}

